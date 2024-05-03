package com.orbys.quizz.ui.components.managers

import android.content.Context
import android.text.InputFilter
import android.text.InputType
import android.view.Gravity
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import com.orbys.quizz.R

/**
 * Clase que crea y gestiona un formulario de respuestas numéricas.
 *
 * @param context Contexto de la aplicación.
 * @param layout Layout donde se mostrarán las respuestas.
 * @param hintText Texto de ayuda que se mostrará en los campos de texto.
 * @param maxLength Cantidad máxima de digitos.
 */
class NumericAnswersManager(
    private val context: Context,
    private val layout: LinearLayout,
    private val hintText: String = context.getString(R.string.question_answer_hint),
    private val maxLength: Int = 4
): AnswerManager(context, layout) {

    override fun addAnswerField() {
        val answerField = createAnswerField()

        layout.addView(answerField)
        answerFields.add(answerField)
    }

    override fun createAnswerField() = EditText(context).apply {
        layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        gravity = Gravity.CENTER
        hint = hintText
        inputType = InputType.TYPE_CLASS_NUMBER
        id = View.generateViewId()
        filters = arrayOf(InputFilter.LengthFilter(maxLength))
    }

}