package com.orbys.vote.ui.components.managers

import android.content.Context
import android.graphics.PorterDuff
import android.text.InputType
import android.view.Gravity
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import com.orbys.vote.R
import com.orbys.vote.core.extensions.showToastWithCustomView

/**
 * Clase que crea y gestiona un formulario de respuestas de texto.
 *
 * @param context Contexto de la aplicación.
 * @param layout Layout donde se mostrarán las respuestas.
 * @param hintText Texto de ayuda que se mostrará en los campos de texto.
 * @param maxLength Cantidad máxima de caracteres.
 * @param minAnswers Número mínimo de respuestas.
 * @param maxAnswers Número máximo de respuestas.
 */
class TextAnswersManager(
    private val context: Context, private val layout: LinearLayout, private val hintText: String = context.getString(R.string.question_answer_hint),
    private val maxLength: Int = 40, private val minAnswers: Int = 1, private val maxAnswers: Int = 1
): AnswerManager(context, layout) {

    override val type = InputType.TYPE_CLASS_TEXT

    override fun addAnswerField() {
        val answerField = createAnswerField(hintText, maxLength)
        val deleteButton = deleteButton(R.drawable.ic_delete)

        val answerLayout = createAnswerLayout(answerField, deleteButton)

        layout.gravity = Gravity.END
        layout.addView(answerLayout)
        answerFields.add(answerField)
    }

    fun addButtonForAddAnswers() {
        // Creamos un boton para añadir nuevos campos para más respuestas
        val addButton = addButton()

        addButton.setOnClickListener {
            // Si no se ha llegado al límite de respuestas, se añade un nuevo campo
            if (answerFields.size < maxAnswers) {
                layout.removeView(addButton)
                addAnswerField()
                layout.addView(addButton)
            } else context.showToastWithCustomView(context.getString(R.string.max_answers_error), Toast.LENGTH_SHORT)
        }

        layout.addView(addButton)
    }

    // Crea un botón con un icono escalado
    private fun addButton() = Button(context).apply {
        val params = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            80
        )
        params.setMargins(1, 8, 0, 8)
        layoutParams = params
        background = AppCompatResources.getDrawable(context, R.drawable.bg_textbox).apply {
            this!!.setColorFilter(ContextCompat.getColor(context, R.color.blue_selected_60), PorterDuff.Mode.SRC_ATOP)
        }
        text = context.getString(R.string.add_answer_button_text)
        textSize = 9f
        setPadding(12,12,12,12)
    }

    /**
     * Crea un layout que contiene un campo de texto y un botón para eliminarlo.
     *
     * @param answer Campo de texto.
     * @param button Botón para eliminar el campo de texto.
     */
    private fun createAnswerLayout(answer: EditText, button: ImageButton) = RelativeLayout(context).apply {
        layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        gravity = Gravity.CENTER_VERTICAL
        addView(answer)
        addView(button)

        button.setOnClickListener {
            // Si no se ha superado el número mínimo de respuestas, se elimina el campo
            if(answerFields.size > minAnswers) {
                layout.removeView(this)
                answerFields.remove(answer)
            } else context.showToastWithCustomView(context.getString(R.string.min_answers_error), Toast.LENGTH_SHORT)
        }
    }

    private fun deleteButton(resId: Int) = ImageButton(context).apply {
        val params = RelativeLayout.LayoutParams(
            RelativeLayout.LayoutParams.WRAP_CONTENT,
            RelativeLayout.LayoutParams.WRAP_CONTENT
        )
        params.addRule(RelativeLayout.ALIGN_PARENT_END)
        layoutParams = params
        background = ContextCompat.getDrawable(context, android.R.color.transparent)
        setImageResource(resId)
        setPadding(12,12,12,12)
    }

}