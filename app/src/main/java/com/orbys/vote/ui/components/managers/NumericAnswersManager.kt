package com.orbys.vote.ui.components.managers

import android.content.Context
import android.text.InputType
import android.widget.LinearLayout
import com.orbys.vote.R

/**
 * Clase que crea y gestiona un formulario de respuestas numéricas.
 *
 * @param context Contexto de la aplicación.
 * @param layout Layout donde se mostrarán las respuestas.
 * @param hintText Texto de ayuda que se mostrará en los campos de texto.
 * @param maxLength Cantidad máxima de digitos.
 */
class NumericAnswersManager(
    private val context: Context, private val layout: LinearLayout,
    private val hintText: String = context.getString(R.string.question_answer_hint), private val maxLength: Int = 4
): AnswerManager(context, layout) {

    override val type = InputType.TYPE_CLASS_NUMBER

    override fun addAnswerField() {
        val answerField = createAnswerField(hintText, maxLength)

        layout.addView(answerField)
        answerFields.add(answerField)
    }

}