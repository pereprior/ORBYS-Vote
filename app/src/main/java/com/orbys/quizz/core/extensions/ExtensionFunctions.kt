package com.orbys.quizz.core.extensions

import android.content.Context
import android.content.Intent
import android.text.InputFilter
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.orbys.quizz.R
import com.orbys.quizz.data.services.HttpService
import com.orbys.quizz.domain.models.Answer
import com.orbys.quizz.domain.models.AnswerType
import com.orbys.quizz.domain.models.Question
import com.orbys.quizz.ui.services.FloatingViewService
import com.orbys.quizz.ui.view.fragments.cards.NumericCard
import com.orbys.quizz.ui.view.fragments.cards.OtherCard
import com.orbys.quizz.ui.view.fragments.cards.StarsCard
import com.orbys.quizz.ui.view.fragments.cards.YesNoCard

// Devuelve el valor del flow con la lista de respuestas de una pregunta
fun Question.getAnswers() = answers.value

// Devuelve el nombre del tipo de las respuestas de una pregunta
fun Question.getAnswerType() = answerType.name

// Devuelve el nombre del tipo de las respuestas de una pregunta
fun Question.getCardType() = when (answerType) {
    AnswerType.BOOLEAN -> YesNoCard()
    AnswerType.STARS -> StarsCard()
    AnswerType.NUMERIC -> NumericCard()
    AnswerType.OTHER -> OtherCard()
    else -> null
}

// Devuelve el valor del flow con la respuesta seleccionada de una pregunta
fun Answer.getCount() = count.value

// Pasar de minutos a segundos
fun Int.minutesToSeconds(): Int = this * 60

// Pasar de segundos a milisegundos
fun Int.secondsToMillis(): Long = this * 1000L

/**
 * Limita el número de líneas y de caracteres de un EditText.
 *
 * @param maxLines El número máximo de líneas.
 */
fun EditText.limitLines(maxLines: Int) {
    this.filters = arrayOf(
        // Limita el numero de caracteres por linea
        InputFilter.LengthFilter(maxLines*55),
        InputFilter { source, start, end, dest, dstart, _ ->
            for (index in start until end) {
                // Si ya hay tres lineas y se presiona enter, no se añade el salto de linea
                if (source[index] == '\n' && dest.subSequence(0, dstart).count { it == '\n' } >= (maxLines - 1))
                    return@InputFilter ""
            }
            null
        }
    )
}

// Cierra los servicios activos de la actividad
fun Fragment.stopActiveServices(isHttpFragment: Boolean = false) {
    if (!isHttpFragment) { activity?.stopService(Intent(activity, HttpService::class.java)) }
    activity?.stopService(Intent(activity, FloatingViewService::class.java))
}

/**
 * Muestra un mensaje informativo.
 *
 * @param message El mensaje que muestra.
 * @param duration El tiempo que dura el mensaje.
 */
fun Context.showToastWithCustomView(
    message: String, duration: Int = Toast.LENGTH_SHORT, textSize: Float = 8f
) {
    val inflater = this.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    val customToastView = inflater.inflate(R.layout.toast_custom, null)

    val toastText = customToastView.findViewById<TextView>(R.id.custom_toast_text)
    toastText.text = message
    toastText.textSize = textSize

    val toast = Toast(this)
    toast.duration = duration
    toast.setGravity(Gravity.BOTTOM, 0, 200)
    toast.view = customToastView
    toast.show()
}