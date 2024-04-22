package com.orbys.quizz.ui.components.managers

import android.content.Context
import android.text.InputFilter
import android.text.InputType
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import com.orbys.quizz.R
import com.orbys.quizz.domain.models.Answer

class AnswerFieldsManager(
    private val context: Context,
    private val layout: LinearLayout,
) {
    companion object {
        private const val MAX_LENGTH = 20
    }

    private val answerFields = mutableListOf<EditText>()

    fun answersNumber() = answerFields.size
    fun anyAnswerIsEmpty() = answerFields.any { it.text.isEmpty() }
    fun getAnswersText() = answerFields.map { it.text.toString() }
    fun getAnswers() = answerFields.map { Answer(it.text.toString()) }

    fun addAnswerField() {
        val newAnswerField = createAnswerField()
        layout.addView(newAnswerField)
        answerFields.add(newAnswerField)
    }

    fun removeAnswerField() {
        val lastAnswerField = answerFields.last()
        layout.removeView(lastAnswerField)
        answerFields.remove(lastAnswerField)
    }

    private fun createAnswerField() = EditText(context).apply {
        layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        hint = context.getString(R.string.question_answer_hint)
        inputType = InputType.TYPE_CLASS_TEXT
        id = View.generateViewId()
        filters = arrayOf(InputFilter.LengthFilter(MAX_LENGTH))
    }

}