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
import com.orbys.quizz.domain.models.Answer
import kotlinx.coroutines.flow.MutableStateFlow

class AnswerFieldsManager(
    private val context: Context,
    private val layout: LinearLayout,
    private val hintText: String = context.getString(R.string.question_answer_hint),
    private val maxLength: Int = 30,
    private val minAnswers: Int = 1,
    private val maxAnswers: Int = 1,
    private val fieldLength: Int = LinearLayout.LayoutParams.WRAP_CONTENT,
    private val numericAnswer: Boolean = false
): AnswerManager(context, layout) {

    private val answerFields = mutableListOf<EditText>()

    fun anyAnswerIsEmpty() = answerFields.any { it.text.isEmpty() }
    fun getAnswersText() = answerFields.map { it.text.toString() }
    fun getAnswers() = MutableStateFlow(answerFields.map { Answer(it.text.toString()) })

    fun setAddAnswersButtons() {
        val addButton = createButton(android.R.drawable.ic_input_add)

        addButton.setOnClickListener {
            if (answerFields.size < maxAnswers) addAnswerField()
            else context.showToastWithCustomView(context.getString(R.string.max_answers_error), Toast.LENGTH_SHORT)
        }

        layout.addView(addButton)
    }

    fun addAnswerField() {
        val answerField = createAnswerField()

        if (maxAnswers != minAnswers) {
            val deleteButton = createButton(android.R.drawable.ic_menu_delete)
            val answerLayout = createAnswerLayout(answerField, deleteButton)
            layout.addView(answerLayout)
        } else {
            layout.addView(answerField)
        }

        answerFields.add(answerField)
    }

    private fun createAnswerLayout(answer: EditText, button: ImageButton) = LinearLayout(context).apply {
        orientation = LinearLayout.HORIZONTAL
        gravity = Gravity.CENTER_VERTICAL
        addView(button)
        addView(answer)

        button.setOnClickListener {
            if(answerFields.size > minAnswers) {
                layout.removeView(this)
                answerFields.remove(answer)
            } else context.showToastWithCustomView(context.getString(R.string.min_answers_error), Toast.LENGTH_SHORT)
        }
    }

    private fun createAnswerField() = EditText(context).apply {
        layoutParams = LinearLayout.LayoutParams(
            fieldLength,
            LinearLayout.LayoutParams.WRAP_CONTENT
        ).apply {
            marginEnd = context.resources.getDimensionPixelSize(R.dimen.medium_margin)
        }
        hint = hintText
        inputType = if (numericAnswer) InputType.TYPE_CLASS_NUMBER else InputType.TYPE_CLASS_TEXT
        id = View.generateViewId()
        filters = arrayOf(InputFilter.LengthFilter(maxLength))
    }

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