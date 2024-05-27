package com.orbys.vote.ui.viewmodels

import android.content.Context
import android.content.Intent
import android.view.View
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import com.orbys.vote.R
import com.orbys.vote.core.extensions.QUESTION_ENDPOINT
import com.orbys.vote.core.extensions.getCount
import com.orbys.vote.core.extensions.getServerUrl
import com.orbys.vote.core.extensions.minutesToMillis
import com.orbys.vote.core.extensions.setExpandOnClick
import com.orbys.vote.core.extensions.showToastWithCustomView
import com.orbys.vote.databinding.ServiceLaunchQuestionBinding
import com.orbys.vote.domain.models.AnswerType
import com.orbys.vote.domain.models.Bar
import com.orbys.vote.domain.models.Question
import com.orbys.vote.domain.usecases.ClearUsersListUseCase
import com.orbys.vote.domain.usecases.GetHttpServiceUseCase
import com.orbys.vote.domain.usecases.GetQuestionUseCase
import com.orbys.vote.domain.usecases.GetUsersListUseCase
import com.orbys.vote.domain.usecases.SetTimeOutUseCase
import com.orbys.vote.ui.components.qr.QRCodeGenerator
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
 * @param getQuestionUseCase Caso de uso para obtener la pregunta del repositorio
 * @param clearUsersListUseCase Caso de uso para limpiar la lista de usuarios
 * @param setTimeOutUseCase Caso de uso para cambiar el estado del temporizador
 * @param getUsersListUseCase Caso de uso para obtener la lista de usuarios
 * @param getHttpServiceUseCase Caso de uso para obtener el servicio http
 */
class LaunchServiceManager @Inject constructor(
    private val context: Context, private val getQuestionUseCase: GetQuestionUseCase, 
    private val clearUsersListUseCase: ClearUsersListUseCase, private val setTimeOutUseCase: SetTimeOutUseCase, 
    private val getUsersListUseCase: GetUsersListUseCase, private val getHttpServiceUseCase: GetHttpServiceUseCase
) {

    fun bind(binding: ServiceLaunchQuestionBinding) {
        val question = getQuestionUseCase()

        with(binding) {
            // Información de la pregunta
            setQuestionElements(question)

            // Opciones para responder la pregunta
            setQrOptions()

            // Recuento de usuarios que han respondido
            setUsersCount()

            // Grafico de barras con el recuento de respuestas
            setGraphicAnswersCount(question)

            // Ponemos el servidor escuchando respuestas
            setTimeOutUseCase(false)
        }
    }

    // Devuelve una instancia del servicio http
    fun getHttpService() = getHttpServiceUseCase()


    /**
     * Añade a los elementos de la vista la pregunta lanzada en el widget
     *
     * @param question Pregunta lanzada
     */
    private fun ServiceLaunchQuestionBinding.setQuestionElements(question: Question) {
        // Titulo de la pregunta
        questionText.text = question.question
        // Ocultamos el botón de cerrar la aplicación en el widget
        banner.closeButton.visibility = View.GONE

        // Si el tiempo de espera no es nulo se muestra el temporizador
        if (question.timeOut!! > 0)
            setTimerCount(question.timeOut)

        // Accion para finalizar una pregunta
        timeOutButton.setOnClickListener {
            setTimeOutUseCase(true)
            timer.cancelTimer()

            stopService()
        }
    }

    /**
     * Añade a los elementos de la vista las diferentes opciones para responder la pregunta
     */
    private fun ServiceLaunchQuestionBinding.setQrOptions() {
        with(qrContainer) {
            val hotspotUrl = getServerUrl(QUESTION_ENDPOINT, true)
            setQrCode(!hotspotUrl.isNullOrEmpty())

            respondContainer.setOnClickListener { setQrCode() }
            respondHotspotContainer.setOnClickListener { setQrCode( true) }
        }
    }

    private fun ServiceLaunchQuestionBinding.setQrCode(
        isHotspot: Boolean = false, endpoint: String = QUESTION_ENDPOINT
    ) {
        with(qrContainer) {
            val url = getServerUrl(endpoint, isHotspot)
            if (url.isNullOrEmpty()) {
                context.showToastWithCustomView(context.getString(R.string.no_network_error), Toast.LENGTH_LONG)
                return
            }

            val qrGenerator = QRCodeGenerator(context)
            val qrCodeBitmap = qrGenerator.generateUrlQrCode(url, true)

            if (isHotspot) {
                lanQrCode.visibility = View.GONE
                lanQrText.visibility = View.GONE
                step1HotspotContainer.visibility = View.VISIBLE
                step2HotspotContainer.visibility = View.VISIBLE

                hotspotQrText.text = url
                otherQrCode.setExpandOnClick()
                hotspotQrCode.apply {
                    setImageBitmap(qrCodeBitmap)
                    setExpandOnClick()
                }
            } else {
                step1HotspotContainer.visibility = View.GONE
                step2HotspotContainer.visibility = View.GONE

                lanQrCode.apply {
                    visibility = View.VISIBLE
                    setImageBitmap(qrCodeBitmap)
                }
                lanQrText.apply {
                    visibility = View.VISIBLE
                    text = url
                }
            }
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
        if (question.answerType == AnswerType.NUMERIC) {
            // Tamaño fijo para la pregunta de tipo numérico debido a que puede tener más de 5 respuestas
            scrollView.layoutParams.height = context.resources.getDimensionPixelSize(R.dimen.graphic_line_size) * 5
            // Tamaño variable dependiendo del número de respuestas para las preguntas con respuestas fijas
        } else scrollView.layoutParams.height = context.resources.getDimensionPixelSize(R.dimen.graphic_line_size) * question.answers.value.size

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

    @OptIn(DelicateCoroutinesApi::class)
    private fun ServiceLaunchQuestionBinding.setTimerCount(timeInMinutes: Int) {
        // Muestra el temporizador
        timer.visibility = ConstraintLayout.VISIBLE

        // Convertimos los minutos a milisegundos
        val timeInMillis = timeInMinutes.minutesToMillis()

        // Iniciamos el temporizador
        timer.setTimeInMillis(timeInMillis)
        timer.startCountDown()

        // Lanzamos una corrutina para recoger los cambios en el estado del temporizador
        GlobalScope.launch {
            timer.isFinished.collect { setTimeOutUseCase(it) }
        }
    }

    private fun stopService() {
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
            putExtra("SHOW_DOWNLOAD_FRAGMENT", true)
        }

        // Iniciamos la actividad principal
        context.startActivity(intent)

        // Limpiamos la lista de usuarios
        clearUsersListUseCase()

        // Detenemos el servicio
        context.stopService(Intent(context, FloatingViewService::class.java))
    }

}