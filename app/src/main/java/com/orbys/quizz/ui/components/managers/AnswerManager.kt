package com.orbys.quizz.ui.components.managers

import android.content.Context
import android.text.InputFilter
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import com.orbys.quizz.R
import com.orbys.quizz.domain.models.Answer
import kotlinx.coroutines.flow.MutableStateFlow

/**
 * Clase abstracta que se encarga de añadir a la vista un formulario personalizado de respuestas.
 *
 * @param context Contexto de la aplicación.
 * @param layout Layout donde se mostrarán las respuestas.
 */
abstract class AnswerManager(
    private val context: Context, layout: LinearLayout
) {
    protected val answerFields = mutableListOf<EditText>()
    protected abstract val inputType: Int

    // Añadir un campo de texto a la vista
    abstract fun addAnswerField()

    init {
        // Creamos el titulo para el formulario de respuestas
        val textView = TextView(context).apply {
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            text = context.getString(R.string.question_answer_title)
            textSize = 11f
        }

        layout.addView(textView)
    }

    // Crear un campo de texto para la respuesta
    fun createAnswerField(hintText: String, maxLength: Int) = EditText(context).apply {
        val params = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        params.setMargins(1, 8, 0, 8)
        layoutParams = params
        background = context.getDrawable(R.drawable.bg_textbox)
        hint = hintText
        inputType = inputType
        id = View.generateViewId()
        filters = arrayOf(InputFilter.LengthFilter(maxLength))
        setPadding(12,12,12,12)
    }

    // Comprueba si hay alguna respuesta vacia
    fun anyAnswerIsEmpty()= answerFields.any { it.text.isEmpty() }

    // Devuelve una lista con el texto de las respuestas
    fun getAnswersText() = answerFields.map { it.text.toString() }

    // Devuelve el flow con la lista de respuestas
    fun getAnswers() = MutableStateFlow(answerFields.map { Answer(it.text.toString()) })
}