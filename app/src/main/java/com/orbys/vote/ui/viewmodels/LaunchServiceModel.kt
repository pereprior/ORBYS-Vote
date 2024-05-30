package com.orbys.vote.ui.viewmodels

import android.content.Context
import android.content.Intent
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.orbys.vote.R
import com.orbys.vote.core.extensions.QUESTION_ENDPOINT
import com.orbys.vote.core.extensions.getCount
import com.orbys.vote.core.extensions.minutesToMillis
import com.orbys.vote.databinding.ServiceLaunchQuestionBinding
import com.orbys.vote.domain.models.AnswerType
import com.orbys.vote.domain.models.Question
import com.orbys.vote.domain.usecases.ClearClientListUseCase
import com.orbys.vote.domain.usecases.GetHttpServiceUseCase
import com.orbys.vote.domain.usecases.GetQuestionUseCase
import com.orbys.vote.domain.usecases.GetUsersListUseCase
import com.orbys.vote.domain.usecases.SetTimeOutUseCase
import com.orbys.vote.ui.components.graphics.GraphicView
import com.orbys.vote.ui.components.qr.setQrOptions
import com.orbys.vote.ui.services.FloatingViewService
import com.orbys.vote.ui.view.MainActivity
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Modelo que gestiona los datos que se muestran en la vista flotante
 *
 * @param context Contexto de la aplicación
 * @param getQuestionUseCase Caso de uso para obtener la pregunta del repositorio
 * @param clearClientListUseCase Caso de uso para limpiar la lista de usuarios
 * @param setTimeOutUseCase Caso de uso para cambiar el estado del temporizador
 * @param getUsersListUseCase Caso de uso para obtener la lista de usuarios
 * @param getHttpServiceUseCase Caso de uso para obtener el servicio http
 */
class LaunchServiceModel @Inject constructor(
    private val context: Context,
    private val getQuestionUseCase: GetQuestionUseCase,
    private val clearClientListUseCase: ClearClientListUseCase,
    private val setTimeOutUseCase: SetTimeOutUseCase,
    private val getUsersListUseCase: GetUsersListUseCase,
    private val getHttpServiceUseCase: GetHttpServiceUseCase
) {

    /** Devuelve una instancia del servicio que inicia el servidor http */
    fun getHttpService() = getHttpServiceUseCase()

    /**
     * Añade los elementos de la vista y la lógica de la pregunta lanzada
     *
     * @param binding Enlace con la vista
     */
    fun bind(binding: ServiceLaunchQuestionBinding) {
        val question = getQuestionUseCase()

        with(binding) {
            banner.closeButton.visibility = View.GONE
            questionText.text = question.question

            qrContainer.setQrOptions(context, QUESTION_ENDPOINT, )

            setTimer(question.timer)

            setUsersCount()

            setGraphicAnswersCount(question)
        }

        // Ponemos el servidor escuchando respuestas
        setTimeOutUseCase(false)
    }

    /**
     * Función que determina si el cliente puede seguir contestando una pregunta o no
     *
     * @param time Tiempo de espera para la pregunta
     */
    private fun ServiceLaunchQuestionBinding.setTimer(time: Int?) {
        // Si el tiempo de espera no es nulo ni 0, se muestra el temporizador
        if (time!! > 0)
            setTimerCount(time)

        // Botón para finalizar la pregunta
        timeOutButton.setOnClickListener {
            setTimeOutUseCase(true)
            timer.cancelTimer()

            stopService()
        }
    }

    /**
     * Función que muestra y gestiona el temporizador en la vista flotante
     *
     * @param timeInMinutes Tiempo de espera para la pregunta en minutos
     */
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

    /** Función que gestiona el contador de usuarios que han respondido a la pregunta */
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

    /**
     * Función que gestiona el gráfico de respuestas de la pregunta
     *
     * @param question Pregunta a la que pertenecen las respuestas
     */
    @OptIn(DelicateCoroutinesApi::class)
    private fun ServiceLaunchQuestionBinding.setGraphicAnswersCount(question: Question) {
        val maxAnswersNumber = 5
        // Si la pregunta es numérica, el tamaño del gráfico será fijo ya que pueden haber hasta 9999 respuestas diferentes
        if (question.answerType == AnswerType.NUMERIC) {
            scrollView.layoutParams.height = context.resources.getDimensionPixelSize(R.dimen.graphic_line_size) * maxAnswersNumber

        // Si no es numérica, el tamaño del gráfico varia dependiendo del número de respuestas de la pregunta
        } else scrollView.layoutParams.height = context.resources.getDimensionPixelSize(R.dimen.graphic_line_size) * question.answers.value.size

        GlobalScope.launch {
            // Recogemos los cambios en el numero de respuestas de la pregunta
            question.answers.collect { answers ->
                barView.clearBars()

                // Por cada respuesta nueva añadimos una barra al grafico
                answers.forEach { answer ->
                    val bar = GraphicView.Bar(answer.answer, answer.getCount())
                    barView.addBar(bar)

                    GlobalScope.launch {
                        // Recoge los cambios en contador para cada respuesta
                        answer.count.collect { count ->
                            bar.length = count
                            barView.invalidate()
                        }
                    }

                }
            }
        }

    }

    /** Función que detiene el servicio flotante y vuelve a abrir la actividad principal */
    private fun stopService() {
        with(context) {
            val intent = Intent(this, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
                putExtra("SHOW_DOWNLOAD_FRAGMENT", true)
            }

            // Iniciamos la actividad principal
            startActivity(intent)

            // Limpiamos la lista de usuarios
            clearClientListUseCase()

            // Detenemos el servicio
            stopService(Intent(this, FloatingViewService::class.java))
        }
    }

}