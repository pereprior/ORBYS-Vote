package com.orbys.quizz.core.extensions

import android.content.Intent
import android.text.InputFilter
import android.widget.EditText
import androidx.fragment.app.Fragment
import com.orbys.quizz.data.services.HttpService
import com.orbys.quizz.domain.models.Answer
import com.orbys.quizz.domain.models.Question
import com.orbys.quizz.ui.services.FloatingViewService

// Funciones de extension para los modelos de datos del dominio
fun Question.getAnswers() = answers.value
fun Question.getAnswerType() = answerType.name
fun Answer.getCount() = count.value

// Funciones de extension de números
fun Int.minutesToSeconds(): Int = this * 60
fun Int.secondsToMillis(): Long = this * 1000L

// Funciones de extension para los elementos de la vista
fun EditText.limitLines(maxLines: Int) {
    this.filters = arrayOf(
        InputFilter.LengthFilter(maxLines*33),
        InputFilter { source, start, end, dest, dstart, _ ->
            for (index in start until end) {
                if (source[index] == '\n' && dest.subSequence(0, dstart).count { it == '\n' } >= (maxLines - 1))
                    return@InputFilter ""
            }
            null
        }
    )
}
fun Fragment.stopActiveServices() {
    // Cierra los servicios activos
    activity?.stopService(Intent(activity, HttpService::class.java))
    activity?.stopService(Intent(activity, FloatingViewService::class.java))
}