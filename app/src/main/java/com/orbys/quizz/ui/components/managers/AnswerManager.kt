package com.orbys.quizz.ui.components.managers

import android.content.Context
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
    context: Context, layout: LinearLayout
) {
    protected val answerFields = mutableListOf<EditText>()

    // Añadir un campo de texto a la vista
    abstract fun addAnswerField()

    // Crear un campo de texto para la respuesta
    protected abstract fun createAnswerField(): EditText

    init {
        // Creamos el titulo para el formulario de respuestas
        val textView = TextView(context).apply {
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            text = context.getString(R.string.question_answer_title)
        }

        layout.addView(textView)
    }

    // Comprueba si hay alguna respuesta vacia
    fun anyAnswerIsEmpty()= answerFields.any { it.text.isEmpty() }

    // Devuelve una lista con el texto de las respuestas
    fun getAnswersText() = answerFields.map { it.text.toString() }

    // Devuelve el flow con la lista de respuestas
    fun getAnswers() = MutableStateFlow(answerFields.map { Answer(it.text.toString()) })
}