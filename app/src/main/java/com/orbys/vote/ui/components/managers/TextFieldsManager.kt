package com.orbys.vote.ui.components.managers

import android.content.Context
import android.text.InputType
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.orbys.vote.R
import com.orbys.vote.core.extensions.showToastWithCustomView

/**
 * Clase que extiende de [AnswerFieldsManager].
 * Se encarga de crear y gestionar los campos para crear respuestas de tipo numérico.
 *
 * @param context Contexto de la aplicación.
 * @param layout Vista donde se mostrarán los campos de texto.
 * @param hintText Texto de ayuda que se mostrará en los campos de texto.
 * @param maxLength Cantidad máxima de caracteres que se pueden introducir en cada campo.
 * @param minAnswers Número mínimo de campos para introducir respuestas (por defecto 2).
 * @param maxAnswers Número máximo de campos para introducir respuestas (por defecto 2).
 */
class TextFieldsManager(
    private val context: Context,
    private val layout: LinearLayout,
    private val hintText: String = context.getString(R.string.question_answer_hint),
    private val maxLength: Int = 30,
    private val minAnswers: Int = 2,
    private val maxAnswers: Int = 2
): AnswerFieldsManager(context) {

    override val fieldsType = InputType.TYPE_CLASS_TEXT

    override fun addAnswerField() {
        val answerField = createAnswerField(hintText, maxLength)
        val deleteButton = deleteButton(R.drawable.ic_delete)
        val answerLayout = createAnswerLayout(answerField, deleteButton)

        layout.addView(answerLayout)
        answerFields.add(answerField)
    }

    /** Crea un layout que contiene el campo de texto y un botón para eliminar el propio campo */
    private fun createAnswerLayout(answer: EditText, button: ImageButton) = RelativeLayout(context).apply {
        layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT,
        )
        addView(answer)
        addView(button)

        button.setOnClickListener {
            // Si no se ha superado el número mínimo de respuestas, se elimina el campo
            if(answerFields.size >= minAnswers) {
                layout.removeView(this)
                answerFields.remove(answer)
            } else context.showToastWithCustomView(context.getString(R.string.min_answers_error), Toast.LENGTH_SHORT)
        }
    }

    /** Crea un botón para eliminar un campo de texto */
    private fun deleteButton(resId: Int) = ImageButton(context).apply {
        val params = RelativeLayout.LayoutParams(
            RelativeLayout.LayoutParams.WRAP_CONTENT,
            RelativeLayout.LayoutParams.WRAP_CONTENT
        )
        params.addRule(RelativeLayout.ALIGN_PARENT_END)
        params.addRule(RelativeLayout.CENTER_VERTICAL)
        layoutParams = params
        background = ContextCompat.getDrawable(context, android.R.color.transparent)
        setImageResource(resId)
        setPadding(12,12,12,12)
    }

    /** Función para establecer un botón para que añada nuevos campos para crear más preguntas */
    fun setAddButtonListener(button: Button) {
        button.setOnClickListener {
            // Si no se ha llegado al límite de respuestas, se añade un nuevo campo
            if (answerFields.size > maxAnswers) context.showToastWithCustomView(context.getString(R.string.max_answers_error))
            else addAnswerField()
        }
    }

}