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
import com.orbys.quizz.domain.models.Answer

class AnswerFieldsManager(
    private val context: Context,
    private val layout: LinearLayout,
) {
    companion object {
        const val MAX_LENGTH = 20
        const val MIN_ANSWERS = 2
        const val MAX_ANSWERS = 5
        const val FIELD_LENGTH = 200
    }

    private val answerFields = mutableListOf<EditText>()

    fun anyAnswerIsEmpty() = answerFields.any { it.text.isEmpty() }
    fun getAnswersText() = answerFields.map { it.text.toString() }
    fun getAnswers() = answerFields.map { Answer(it.text.toString()) }

    fun setAddAnswersButtons() {
        val addButton = createButton(android.R.drawable.ic_input_add)

        addButton.setOnClickListener {
            if (answerFields.size < MAX_ANSWERS) addAnswersField()
            else Toast.makeText(context, R.string.max_answers_error, Toast.LENGTH_SHORT).show()
        }

        layout.addView(addButton)
    }

    fun addAnswersField() {
        val answerField = createAnswerField()
        val deleteButton = createButton(android.R.drawable.ic_menu_delete)
        val newAnswer = createAnswer(answerField, deleteButton)

        layout.addView(newAnswer)
        answerFields.add(answerField)
    }

    private fun createAnswer(answer: EditText, button: ImageButton) = LinearLayout(context).apply {
        orientation = LinearLayout.HORIZONTAL
        gravity = Gravity.CENTER
        addView(answer)
        addView(button)

        button.setOnClickListener {
            if(answerFields.size > MIN_ANSWERS) {
                layout.removeView(this)
                answerFields.remove(answer)
            } else Toast.makeText(context, R.string.min_answers_error, Toast.LENGTH_SHORT).show()
        }
    }

    private fun createAnswerField() = EditText(context).apply {
        layoutParams = LinearLayout.LayoutParams(
            FIELD_LENGTH,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        hint = context.getString(R.string.question_answer_hint)
        inputType = InputType.TYPE_CLASS_TEXT
        id = View.generateViewId()
        filters = arrayOf(InputFilter.LengthFilter(MAX_LENGTH))
    }

    private fun createButton(resId: Int) = ImageButton(context).apply {
        val drawable = ContextCompat.getDrawable(context, resId)
        val originalIconSize = maxOf(drawable?.intrinsicWidth ?: 0, drawable?.intrinsicHeight ?: 0)
        val desiredIconSize = context.resources.getDimensionPixelSize(R.dimen.icon_size)
        val scale = desiredIconSize.toFloat() / originalIconSize

        layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )

        foregroundGravity = Gravity.CENTER
        background = ContextCompat.getDrawable(context, android.R.color.transparent)
        scaleX = scale
        scaleY = scale
        setColorFilter(ContextCompat.getColor(context, R.color.blue_selected), PorterDuff.Mode.SRC_IN)
        setImageDrawable(drawable)
    }

}