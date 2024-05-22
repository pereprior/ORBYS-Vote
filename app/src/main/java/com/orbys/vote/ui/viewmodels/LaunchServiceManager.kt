package com.orbys.vote.ui.viewmodels

import android.content.Context
import android.content.Intent
import android.view.View
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import com.orbys.vote.R
import com.orbys.vote.core.extensions.getCount
import com.orbys.vote.core.extensions.minutesToSeconds
import com.orbys.vote.core.extensions.secondsToMillis
import com.orbys.vote.core.extensions.showImageDialog
import com.orbys.vote.core.extensions.showToastWithCustomView
import com.orbys.vote.core.managers.NetworkManager.Companion.QUESTION_ENDPOINT
import com.orbys.vote.databinding.ServiceLaunchQuestionBinding
import com.orbys.vote.domain.models.AnswerType
import com.orbys.vote.domain.models.Bar
import com.orbys.vote.domain.models.Question
import com.orbys.vote.domain.usecases.ClearUsersListUseCase
import com.orbys.vote.domain.usecases.GetHttpServiceUseCase
import com.orbys.vote.domain.usecases.GetQuestionUseCase
import com.orbys.vote.domain.usecases.GetServerUrlUseCase
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
 * @param getServerUrlUseCase Caso de uso para obtener la url del servidor
 * @param getQuestionUseCase Caso de uso para obtener la pregunta del repositorio
 * @param clearUsersListUseCase Caso de uso para limpiar la lista de usuarios
 * @param setTimeOutUseCase Caso de uso para cambiar el estado del temporizador
 * @param getUsersListUseCase Caso de uso para obtener la lista de usuarios
 * @param getHttpServiceUseCase Caso de uso para obtener el servicio http
 */
class LaunchServiceManager @Inject constructor(
    private val context: Context, private val getServerUrlUseCase: GetServerUrlUseCase,
    private val getQuestionUseCase: GetQuestionUseCase, private val clearUsersListUseCase: ClearUsersListUseCase,
    private val setTimeOutUseCase: SetTimeOutUseCase, private val getUsersListUseCase: GetUsersListUseCase,
    private val getHttpServiceUseCase: GetHttpServiceUseCase
) {
    fun getHttpService() = getHttpServiceUseCase()

    fun setQuestionElements(
        binding: ServiceLaunchQuestionBinding, endpoint: String = QUESTION_ENDPOINT
    ) {
        val question = getQuestionUseCase()

        with(binding) {
            // Información de la pregunta
            setQuestionElements(question)

            // Opciones para responder la pregunta
            setQrOptions(endpoint)

            // Recuento de usuarios que han respondido
            setUsersCount()

            // Grafico de barras con el recuento de respuestas
            setGraphicAnswersCount(question)

            // Ponemos el servidor escuchando respuestas
            setTimeOutUseCase(false)
        }
    }

    private fun ServiceLaunchQuestionBinding.setQrOptions(endpoint: String) {
        val hotspotUrl = getServerUrlUseCase(endpoint, true)

        setQrCode(endpoint, !hotspotUrl.isNullOrEmpty())

        respondContainer.setOnClickListener { setQrCode(endpoint) }
        respondHotspotContainer.setOnClickListener { setQrCode(endpoint, true) }
    }

    private fun ServiceLaunchQuestionBinding.setQrCode(
        endpoint: String, isHotspot: Boolean = false
    ) {
        val url = getServerUrlUseCase(endpoint, isHotspot)

        if (url.isNullOrEmpty()) {
            context.showToastWithCustomView(context.getString(R.string.no_network_error), Toast.LENGTH_LONG)
            return
        } else {
            val qrGenerator = QRCodeGenerator(context)
            val qrCodeBitmap = qrGenerator.generateUrlQrCode(url, true, 456, 456)
            val qrCodeImage = qrGenerator.generateBitmapFromImage(R.drawable.qr, false, 456, 456)

            lanQrCode.apply {
                visibility = if (isHotspot) View.GONE else View.VISIBLE
                setImageBitmap(if (isHotspot) null else qrCodeBitmap)
            }

            lanQrText.apply {
                visibility = if (isHotspot) View.GONE else View.VISIBLE
                text = if (isHotspot) null else url
            }

            hotspotQrText.text = if (isHotspot) url else null
            hotspotQrCode.apply {
                setImageBitmap(if (isHotspot) qrCodeBitmap else null)
                setOnClickListener { showImageDialog(qrCodeBitmap) }
            }

            otherQrCode.apply {
                setOnClickListener { showImageDialog(qrCodeImage) }
            }

            step1HotspotContainer.visibility = if (isHotspot) View.VISIBLE else View.GONE
            step2HotspotContainer.visibility = if (isHotspot) View.VISIBLE else View.GONE
        }

    }

    private fun ServiceLaunchQuestionBinding.setQuestionElements(question: Question) {
        // Titulo y tipo de la pregunta
        questionText.text = question.question
        banner.closeButton.visibility = View.GONE

        // Si el tiempo de espera no es nulo se muestra el temporizador
        if (question.timeOut!! > 0)
            setTimerCount(question.timeOut)

        if (question.answerType == AnswerType.NUMERIC) {
            // Tamaño fijo para la pregunta de tipo numérico debido a que puede tener más de 5 respuestas
            scrollView.layoutParams.height = context.resources.getDimensionPixelSize(R.dimen.graphic_line_size) * 5
            // Tamaño variable dependiendo del número de respuestas para las preguntas con respuestas fijas
        } else scrollView.layoutParams.height = context.resources.getDimensionPixelSize(R.dimen.graphic_line_size) * question.answers.value.size

        // Boton para finalizar la pregunta
        timeOutButton.setOnClickListener {
            setTimeOutUseCase(true)
            timer.cancelTimer()

            stopService()
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