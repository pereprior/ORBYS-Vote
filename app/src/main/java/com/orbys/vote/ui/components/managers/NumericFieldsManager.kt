package com.orbys.vote.ui.components.managers

import android.content.Context
import android.text.InputType
import android.widget.LinearLayout
import com.orbys.vote.R

/**
 * Clase que extiende de [AnswerFieldsManager].
 * Se encarga de crear y gestionar los campos para crear respuestas de tipo numérico.
 *
 * @param context Contexto de la aplicación.
 * @param layout Vista donde se mostrarán los campos de texto.
 * @param hintText Texto de ayuda que se mostrará en los campos de texto.
 * @param maxLength Cantidad máxima de dígitos que se pueden introducir en cada campo.
 */
class NumericFieldsManager(
    private val context: Context,
    private val layout: LinearLayout,
    private val hintText: String = context.getString(R.string.question_answer_hint),
    private val maxLength: Int = 4
): AnswerFieldsManager(context) {

    override val fieldsType = InputType.TYPE_CLASS_NUMBER

    override fun addAnswerField() {
        val answerField = createAnswerField(hintText, maxLength)

        answerFields.add(answerField)
        layout.addView(answerField)
    }

}