package com.orbys.vote.ui.view.fragments.add

import com.orbys.vote.R
import com.orbys.vote.domain.models.Answer
import com.orbys.vote.domain.models.AnswerType
import com.orbys.vote.domain.models.Question
import kotlinx.coroutines.flow.MutableStateFlow

/**
 * Clase que representa una actividad para añadir preguntas de tipo "Si/No".
 */
class AddYesNoQuestion: AddFragment() {

    override val answerType = AnswerType.YES_NO

    override fun createQuestionFromInput() = Question(
        question = binding.questionQuestion.text.toString(),
        answers = MutableStateFlow(
            listOf(
                Answer(this.getString(R.string.yes_answer_placeholder)),
                Answer(this.getString(R.string.no_answers_placeholder))
            )
        ),
        answerType = answerType,
        isAnonymous = binding.anonymousQuestionOption.isChecked,
        timeOut = binding.timeoutInput.text.toString().toIntOrNull() ?: 0
    )

}