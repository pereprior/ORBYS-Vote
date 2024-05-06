package com.orbys.quizz.ui.view.fragments.add

import com.orbys.quizz.R
import com.orbys.quizz.domain.models.Answer
import com.orbys.quizz.domain.models.AnswerType
import com.orbys.quizz.domain.models.Question
import kotlinx.coroutines.flow.MutableStateFlow

/**
 * Clase que representa una actividad para añadir preguntas de tipo "Estrellas".
 */
class AddStarsQuestion: AddFragment() {

    override val titleResId: Int = R.string.stars_question_type_title
    override val iconResId: Int = R.drawable.ic_star

    override fun createQuestionFromInput() = Question(
        question = binding.questionQuestion.text.toString(),
        icon = iconResId,
        answers = MutableStateFlow(
            listOf(
                Answer("1"),
                Answer("2"),
                Answer("3"),
                Answer("4"),
                Answer("5")
            )
        ),
        answerType = AnswerType.STARS,
        isAnonymous = binding.anonymousQuestionOption.isChecked,
        timeOut = binding.timeoutInput.text.toString().toIntOrNull() ?: 0
    )

}