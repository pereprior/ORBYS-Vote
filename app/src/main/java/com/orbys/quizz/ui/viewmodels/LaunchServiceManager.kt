package com.orbys.quizz.ui.viewmodels

import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.orbys.quizz.R
import com.orbys.quizz.core.extensions.getCount
import com.orbys.quizz.core.extensions.minutesToSeconds
import com.orbys.quizz.core.extensions.secondsToMillis
import com.orbys.quizz.core.managers.NetworkManager.Companion.QUESTION_ENDPOINT
import com.orbys.quizz.databinding.ServiceLaunchQuestionBinding
import com.orbys.quizz.domain.models.Bar
import com.orbys.quizz.domain.models.Question
import com.orbys.quizz.domain.usecases.ClearUsersListUseCase
import com.orbys.quizz.domain.usecases.GetQuestionUseCase
import com.orbys.quizz.domain.usecases.GetServerUrlUseCase
import com.orbys.quizz.domain.usecases.GetUsersListUseCase
import com.orbys.quizz.domain.usecases.SetTimeOutUseCase
import com.orbys.quizz.ui.components.QRCodeGenerator
import com.orbys.quizz.ui.services.FloatingViewService
import com.orbys.quizz.ui.view.MainActivity
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
 * @param serverUrl Caso de uso para obtener la url del servidor
 * @param getQuestion Caso de uso para obtener la pregunta del repositorio
 * @param clearUsers Caso de uso para limpiar la lista de usuarios
 * @param setTimeOut Caso de uso para cambiar el estado del temporizador
 * @param getUsers Caso de uso para obtener la lista de usuarios
 */
class LaunchServiceManager @Inject constructor(
    private val context: Context,
    private val serverUrl: GetServerUrlUseCase,
    private val getQuestion: GetQuestionUseCase,
    private val clearUsers: ClearUsersListUseCase,
    private val setTimeOut: SetTimeOutUseCase,
    private val getUsers: GetUsersListUseCase
) {

    fun printQrCode(
        endpoint: String = QUESTION_ENDPOINT,
        qrCode: ImageView,
        qrUrl: TextView
    ) {
        val url = serverUrl(endpoint)

        // Generamos el qr a partir de la url del servidor http
        qrCode.setImageBitmap(QRCodeGenerator(context).encodeAsBitmap(url, true))
        qrUrl.text = url
    }

    fun bindWidget(binding: ServiceLaunchQuestionBinding) {
        val question = getQuestion()

        with(binding) {
            // Titulo y tipo de la pregunta
            questionTypeIcon.setImageResource(question.icon)
            questionText.text = question.question

            closeButton.setOnClickListener { stopService() }

            // Si el tiempo de espera no es nulo se muestra el temporizador
            if (question.timeOut!! > 0) {
                setTimerCount(question.timeOut)
                Log.d("LaunchServiceManager2", "SIIIII")
            } else {
                Log.d("LaunchServiceManager2", "NOOO")
            }

            // Boton para finalizar la pregunta
            timeOutButton.setOnClickListener {
                setTimeOut(true)
                timer.cancelTimer()

                stopService(true)
            }

            // Recuento de usuarios que han respondido
            setUsersCount()

            // Grafico de barras con el recuento de respuestas
            setGraphicAnswersCount(question)
        }

        // Ponemos el servidor escuchando respuestas
        setTimeOut(false)
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun ServiceLaunchQuestionBinding.setTimerCount(timeInMinutes: Int) {
        // Muestra el temporizador
        chronometerTitle.visibility = ConstraintLayout.VISIBLE
        timer.visibility = ConstraintLayout.VISIBLE

        // Convertimos los minutos a milisegundos
        val timeInSeconds = timeInMinutes.minutesToSeconds()
        val timeInMillis = timeInSeconds.secondsToMillis()

        // Iniciamos el temporizador
        timer.setTimeInMillis(timeInMillis)
        timer.startCountDown()

        // Lanzamos una corrutina para recoger los cambios en el estado del temporizador
        GlobalScope.launch {
            timer.isFinished.collect { setTimeOut(it) }
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun ServiceLaunchQuestionBinding.setUsersCount() {
        // Recogemos los cambios en el número de usuarios que han respondido
        GlobalScope.launch {
            getUsers().collect { usersList ->
                withContext(Dispatchers.Main) {
                    respondedUsersCount.text = context.getString(R.string.users_count_title) + usersList.size
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
                    val bar = Bar(answer.answer, height = answer.getCount())
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
        val intent = Intent(context, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        if (isDownloadFragment) intent.putExtra("SHOW_DOWNLOAD_FRAGMENT", true)

        // Iniciamos la actividad principal
        context.startActivity(intent)

        // Limpiamos la lista de usuarios
        clearUsers()

        // Detenemos el servicio
        context.stopService(Intent(context, FloatingViewService::class.java))
    }

}