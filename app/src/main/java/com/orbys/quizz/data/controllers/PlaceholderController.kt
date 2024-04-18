package com.orbys.quizz.data.controllers

import android.content.Context
import com.orbys.quizz.R
import com.orbys.quizz.data.constants.ANSWER_PLACEHOLDER
import com.orbys.quizz.data.constants.LOGIN_TITLE_PLACEHOLDER
import com.orbys.quizz.data.constants.QUESTION_PLACEHOLDER
import com.orbys.quizz.data.constants.SEND_BUTTON_PLACEHOLDER
import com.orbys.quizz.domain.models.Question
import javax.inject.Inject

class PlaceholderController @Inject constructor(
    private val context: Context
) {

    fun replace(content: String, question: Question): String = content
        .replaceSaveButtonName()
        .replaceLoginTitleName()
        .replaceQuestionNames(question)
        .replaceAnswersNames(question)
        .replaceOtherFunctions(question)

    private fun String.replaceSaveButtonName(): String =
        replace(SEND_BUTTON_PLACEHOLDER, context.getString(R.string.save_button_placeholder))

    private fun String.replaceLoginTitleName(): String =
        replace(LOGIN_TITLE_PLACEHOLDER, context.getString(R.string.login_title_placeholder))

    private fun String.replaceQuestionNames(question: Question): String =
        replace(QUESTION_PLACEHOLDER, question.question)

    private fun String.replaceAnswersNames(question: Question): String {
        var result = this
        question.answers.forEachIndexed { index, answer ->
            result = result.replace("$ANSWER_PLACEHOLDER$index]", answer.answer.toString())
        }
        return result
    }

    private fun String.replaceOtherFunctions(question: Question): String {
        val answersToString = question.answers.joinToString(",") { it.answer.toString() }
        val multipleChoices = if (question.isMultipleChoices) "multiple" else "single"
        val multipleAnswers = if (question.isMultipleAnswers) "multiple" else "single"

        return this
            .replace("ANSWERS_STRING_PLACEHOLDER", answersToString)
            .replace("MULTIPLE_CHOICES_PLACEHOLDER", multipleChoices)
            .replace("MULTIPLE_ANSWERS_PLACEHOLDER", multipleAnswers)
            .replace("MULTIPLE_VOTING_ALERT_PLACEHOLDER", context.getString(R.string.multiple_voting_alert_placeholder))
    }

}