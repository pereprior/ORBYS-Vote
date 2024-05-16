package com.orbys.vote.ui.viewmodels

import android.content.Context
import android.content.Intent
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import com.orbys.vote.core.extensions.getCount
import com.orbys.vote.core.extensions.minutesToSeconds
import com.orbys.vote.core.extensions.secondsToMillis
import com.orbys.vote.core.managers.NetworkManager.Companion.QUESTION_ENDPOINT
import com.orbys.vote.databinding.ServiceLaunchQuestionBinding
import com.orbys.vote.domain.models.Bar
import com.orbys.vote.domain.models.Question
import com.orbys.vote.domain.usecases.ClearUsersListUseCase
import com.orbys.vote.domain.usecases.GetHttpServiceUseCase
import com.orbys.vote.domain.usecases.GetQuestionUseCase
import com.orbys.vote.domain.usecases.GetServerUrlUseCase
import com.orbys.vote.domain.usecases.GetUsersListUseCase
import com.orbys.vote.domain.usecases.SetTimeOutUseCase
import com.orbys.vote.ui.components.QRCodeGenerator
import com.orbys.vote.ui.services.FloatingViewService
import com.orbys.vote.ui.view.MainActivity
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Clase que gestiona los datos que se muestran en la vista flotante
 *
 * @param context Contexto de la aplicación
 * @param getServerUrlUseCase Caso de uso para obtener la url del servidor
 * @param getQuestionUseCase Caso de uso para obtener la pregunta del repositorio
 * @param clearUsersListUseCase Caso de uso para limpiar la lista de usuarios
 * @param setTimeOutUseCase Caso de uso para cambiar el estado del temporizador
 * @param getUsersListUseCase Caso de uso para obtener la lista de usuarios
 * @param getHttpServiceUseCase Caso de uso para obtener el servicio http
 */
class LaunchServiceManager @Inject constructor(
    private val context: Context, private val getServerUrlUseCase: GetServerUrlUseCase,
    private val getQuestionUseCase: GetQuestionUseCase,
    private val clearUsersListUseCase: ClearUsersListUseCase,
    private val setTimeOutUseCase: SetTimeOutUseCase,
    private val getUsersListUseCase: GetUsersListUseCase,
    private val getHttpServiceUseCase: GetHttpServiceUseCase
) {

    fun getHttpService() = getHttpServiceUseCase()

    fun setQuestionElements(
        endpoint: String = QUESTION_ENDPOINT,
        binding: ServiceLaunchQuestionBinding
    ) {
        val question = getQuestionUseCase()
        var url = getServerUrlUseCase(endpoint)!!

        with(binding) {
            lanQrCode.setImageBitmap(QRCodeGenerator(context).generateUrlQrCode(url, true))
            lanQrText.text = url

            // Agregar la vista al FrameLayout
            respondContainer.setOnClickListener {
                url = getServerUrlUseCase(endpoint) ?: return@setOnClickListener

                lanQrCode.visibility = LinearLayout.VISIBLE
                lanQrText.visibility = LinearLayout.VISIBLE
                hotspotQrCode.visibility = LinearLayout.GONE
                hotspotQrText.visibility = LinearLayout.GONE

                lanQrCode.setImageBitmap(QRCodeGenerator(context).generateUrlQrCode(url, true))
                lanQrText.text = url
            }

            respondHotspotContainer.setOnClickListener {
                url = getServerUrlUseCase(endpoint, true) ?: return@setOnClickListener

                lanQrCode.visibility = LinearLayout.GONE
                lanQrText.visibility = LinearLayout.GONE
                hotspotQrCode.visibility = LinearLayout.VISIBLE
                hotspotQrText.visibility = LinearLayout.VISIBLE

                hotspotQrCode.setImageBitmap(QRCodeGenerator(context).generateUrlQrCode(url, true))
                hotspotQrText.text = url
            }

            // Información de la pregunta
            setQuestionElements(question)

            // Recuento de usuarios que han respondido
            setUsersCount()

            // Grafico de barras con el recuento de respuestas
            setGraphicAnswersCount(question)

            // Ponemos el servidor escuchando respuestas
            setTimeOutUseCase(false)
        }
    }

    private fun ServiceLaunchQuestionBinding.setQuestionElements(question: Question) {
        // Titulo y tipo de la pregunta
        questionText.text = question.question

        closeWidgetButton.setOnClickListener { stopService() }

        // Si el tiempo de espera no es nulo se muestra el temporizador
        if (question.timeOut!! > 0)
            setTimerCount(question.timeOut)

        // Boton para finalizar la pregunta
        timeOutButton.setOnClickListener {
            setTimeOutUseCase(true)
            timer.cancelTimer()

            stopService(true)
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun ServiceLaunchQuestionBinding.setTimerCount(timeInMinutes: Int) {
        // Muestra el temporizador
        timer.visibility = ConstraintLayout.VISIBLE

        // Convertimos los minutos a milisegundos
        val timeInSeconds = timeInMinutes.minutesToSeconds()
        val timeInMillis = timeInSeconds.secondsToMillis()

        // Iniciamos el temporizador
        timer.setTimeInMillis(timeInMillis)
        timer.startCountDown()

        // Lanzamos una corrutina para recoger los cambios en el estado del temporizador
        GlobalScope.launch {
            timer.isFinished.collect { setTimeOutUseCase(it) }
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun ServiceLaunchQuestionBinding.setUsersCount() {
        // Recogemos los cambios en el número de usuarios que han respondido
        GlobalScope.launch {
            getUsersListUseCase().collect { usersList ->
                withContext(Dispatchers.Main) {
                    respondedUsersCount.text = usersList.size.toString()
                }
            }
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun ServiceLaunchQuestionBinding.setGraphicAnswersCount(question: Question) {
        GlobalScope.launch {
            // Recogemos los cambios en el numero de respuestas de la pregunta
            question.answers.collect { answers ->
                barView.clearBars()

                // Por cada respuesta nueva añadimos una barra al grafico
                answers.forEach { answer ->
                    val bar = Bar(answer.answer, answer.getCount())
                    barView.addBar(bar)

                    GlobalScope.launch {
                        // Recoge los cambios en contador para cada respuesta
                        answer.count.collect { count ->
                            bar.height = count
                            barView.invalidate()
                        }
                    }

                }
            }
        }

    }

    private fun stopService(isDownloadFragment: Boolean = false) {
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
            if (isDownloadFragment) putExtra("SHOW_DOWNLOAD_FRAGMENT", true)
        }

        // Iniciamos la actividad principal
        context.startActivity(intent)

        // Limpiamos la lista de usuarios
        clearUsersListUseCase()

        // Detenemos el servicio
        context.stopService(Intent(context, FloatingViewService::class.java))
    }

}