package com.orbys.quizz.ui.view.fragments.add

import com.orbys.quizz.domain.models.Answer
import com.orbys.quizz.domain.models.AnswerType
import com.orbys.quizz.domain.models.Question
import kotlinx.coroutines.flow.MutableStateFlow

/**
 * Clase que representa una actividad para añadir preguntas de tipo "Estrellas".
 */
class AddStarsQuestion: AddFragment() {

    override val answerType = AnswerType.STARS

    override fun createQuestionFromInput() = Question(
        question = binding.questionQuestion.text.toString(),
        answers = MutableStateFlow(
            listOf(
                Answer("1"),
                Answer("2"),
                Answer("3"),
                Answer("4"),
                Answer("5")
            )
        ),
        answerType = answerType,
        isAnonymous = binding.anonymousQuestionOption.isChecked,
        timeOut = binding.timeoutInput.text.toString().toIntOrNull() ?: 0
    )

}