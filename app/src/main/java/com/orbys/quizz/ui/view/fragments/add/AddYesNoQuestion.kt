package com.orbys.quizz.ui.view.fragments.add

import com.orbys.quizz.R
import com.orbys.quizz.domain.models.Answer
import com.orbys.quizz.domain.models.AnswerType
import com.orbys.quizz.domain.models.Question
import kotlinx.coroutines.flow.MutableStateFlow

/**
 * Clase que representa una actividad para añadir preguntas de tipo "Si/No".
 */
class AddYesNoQuestion: AddFragment() {

    override val titleResId: Int = R.string.yesno_question_type_title
    override val iconResId: Int = R.drawable.ic_yesno

    override fun createQuestionFromInput() = Question(
        question = binding.questionQuestion.text.toString(),
        icon = iconResId,
        answers = MutableStateFlow(
            listOf(
                Answer(this.getString(R.string.yes_answer_placeholder)),
                Answer(this.getString(R.string.no_answers_placeholder))
            )
        ),
        answerType = AnswerType.BOOLEAN,
        isAnonymous = binding.anonymousQuestionOption.isChecked,
        timeOut = binding.timeoutInput.text.toString().toIntOrNull() ?: 0
    )

}