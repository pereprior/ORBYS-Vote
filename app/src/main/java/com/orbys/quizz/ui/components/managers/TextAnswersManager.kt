package com.orbys.quizz.ui.components.managers

import android.content.Context
import android.graphics.PorterDuff
import android.text.InputFilter
import android.text.InputType
import android.view.Gravity
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.orbys.quizz.R
import com.orbys.quizz.core.extensions.showToastWithCustomView

/**
 * Clase que crea y gestiona un formulario de respuestas de texto.
 *
 * @param context Contexto de la aplicación.
 * @param layout Layout donde se mostrarán las respuestas.
 * @param hintText Texto de ayuda que se mostrará en los campos de texto.
 * @param maxLength Cantidad máxima de caracteres.
 * @param minAnswers Número mínimo de respuestas.
 * @param maxAnswers Número máximo de respuestas.
 * @param fieldLength Longitud de los campos de texto.
 */
class TextAnswersManager(
    private val context: Context,
    private val layout: LinearLayout,
    private val hintText: String = context.getString(R.string.question_answer_hint),
    private val maxLength: Int = 33,
    private val minAnswers: Int = 1,
    private val maxAnswers: Int = 1,
    private val fieldLength: Int = LinearLayout.LayoutParams.WRAP_CONTENT
): AnswerManager(context, layout) {

    override fun addAnswerField() {
        val answerField = createAnswerField()
        val deleteButton = createButton(android.R.drawable.ic_menu_delete)
        val answerLayout = createAnswerLayout(answerField, deleteButton)

        layout.addView(answerLayout)
        answerFields.add(answerField)
    }

    override fun createAnswerField() = EditText(context).apply {
        layoutParams = LinearLayout.LayoutParams(
            fieldLength,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        hint = hintText
        inputType = InputType.TYPE_CLASS_TEXT
        id = View.generateViewId()
        filters = arrayOf(InputFilter.LengthFilter(maxLength))
    }

    fun addButtonForAddAnswers() {
        // Creamos un boton para añadir nuevos campos para más respuestas
        val addButton = createButton(android.R.drawable.ic_input_add)

        addButton.setOnClickListener {
            // Si no se ha llegado al límite de respuestas, se añade un nuevo campo
            if (answerFields.size < maxAnswers) addAnswerField()
            else context.showToastWithCustomView(context.getString(R.string.max_answers_error), Toast.LENGTH_SHORT)
        }

        layout.addView(addButton)
    }

    /**
     * Crea un layout que contiene un campo de texto y un botón para eliminarlo.
     *
     * @param answer Campo de texto.
     * @param button Botón para eliminar el campo de texto.
     */
    private fun createAnswerLayout(answer: EditText, button: ImageButton) = LinearLayout(context).apply {
        orientation = LinearLayout.HORIZONTAL
        gravity = Gravity.CENTER_VERTICAL
        addView(button)
        addView(answer)

        button.setOnClickListener {
            // Si no se ha superado el número mínimo de respuestas, se elimina el campo
            if(answerFields.size > minAnswers) {
                layout.removeView(this)
                answerFields.remove(answer)
            } else context.showToastWithCustomView(context.getString(R.string.min_answers_error), Toast.LENGTH_SHORT)
        }
    }

    // Crea un botón con un icono escalado
    private fun createButton(resId: Int) = ImageButton(context).apply {
        val drawable = ContextCompat.getDrawable(context, resId)
        val originalSize = maxOf(drawable?.intrinsicWidth ?: 0, drawable?.intrinsicHeight ?: 0)
        val size = context.resources.getDimensionPixelSize(R.dimen.icon_size)
        val scale = size.toFloat() / originalSize

        layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )

        background = ContextCompat.getDrawable(context, android.R.color.transparent)
        scaleX = scale
        scaleY = scale
        setColorFilter(ContextCompat.getColor(context, R.color.blue_selected), PorterDuff.Mode.SRC_IN)
        setImageDrawable(drawable)
    }

}