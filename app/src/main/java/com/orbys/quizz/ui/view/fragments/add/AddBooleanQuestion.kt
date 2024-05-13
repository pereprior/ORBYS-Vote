package com.orbys.quizz.ui.view.fragments.add

import com.orbys.quizz.R
import com.orbys.quizz.domain.models.Answer
import com.orbys.quizz.domain.models.AnswerType
import com.orbys.quizz.domain.models.Question
import kotlinx.coroutines.flow.MutableStateFlow

class AddBooleanQuestion : AddFragment() {

    override val answerType = AnswerType.BOOLEAN

    override fun createQuestionFromInput() = Question(
        question = binding.questionQuestion.text.toString(),
        answers = MutableStateFlow(
            listOf(
                Answer(this.getString(R.string.true_answer_placeholder)),
                Answer(this.getString(R.string.false_answer_placeholder))
            )
        ),
        answerType = answerType,
        isAnonymous = binding.anonymousQuestionOption.isChecked,
        timeOut = binding.timeoutInput.text.toString().toIntOrNull() ?: 0
    )

}