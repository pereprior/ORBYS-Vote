package com.orbys.quizz.ui.view.fragments.add

import com.orbys.quizz.R
import com.orbys.quizz.domain.models.Answer
import com.orbys.quizz.domain.models.AnswerType
import com.orbys.quizz.domain.models.Question
import com.orbys.quizz.ui.view.fragments.cards.BooleanCard
import kotlinx.coroutines.flow.MutableStateFlow

class AddBooleanQuestion : AddFragment() {

    override val titleResId: Int = R.string.truefalse_question_type_title
    override val iconResId: Int = R.drawable.ic_boolean
    override val cardType = BooleanCard()

    override fun createQuestionFromInput() = Question(
        question = binding.questionQuestion.text.toString(),
        icon = iconResId,
        answers = MutableStateFlow(
            listOf(
                Answer(this.getString(R.string.true_answer_placeholder)),
                Answer(this.getString(R.string.false_answer_placeholder))
            )
        ),
        answerType = AnswerType.BOOLEAN,
        isAnonymous = binding.anonymousQuestionOption.isChecked,
        timeOut = binding.timeoutInput.text.toString().toIntOrNull() ?: 0
    )

}