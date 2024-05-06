package com.orbys.quizz.core.extensions

import android.content.Intent
import android.text.InputFilter
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.orbys.quizz.R
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
fun Fragment.stopActiveServices(isHttpFragment: Boolean = false) {
    // Cierra los servicios activos
    if (isHttpFragment) { activity?.stopService(Intent(activity, HttpService::class.java)) }
    activity?.stopService(Intent(activity, FloatingViewService::class.java))
}
fun Fragment.replaceMainActivityBindingFunctions(
    titleRedId: Int,
    closeButtonVisibility: Int = View.GONE,
    backButtonVisibility: Int = View.GONE,
    backButtonNavFragment: Fragment? = null
) {
    // Reemplaza las funciones de la actividad principal
    val backButton = activity?.findViewById<ImageButton>(R.id.back_button)
    val closeButton = activity?.findViewById<ImageButton>(R.id.close_button)
    val title = activity?.findViewById<TextView>(R.id.title)

    title?.text = getString(titleRedId)
    closeButton?.visibility = closeButtonVisibility
    backButton?.visibility = backButtonVisibility

    if (backButtonVisibility == View.VISIBLE && backButtonNavFragment != null) {
        backButton?.setOnClickListener {
            activity?.supportFragmentManager?.beginTransaction()?.apply {
                replace(R.id.fragment_container, backButtonNavFragment)
                commit()
            }
        }
    }

}
