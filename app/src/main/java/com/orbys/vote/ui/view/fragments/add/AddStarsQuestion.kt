package com.orbys.vote.ui.view.fragments.add

import com.orbys.vote.domain.models.Answer
import com.orbys.vote.domain.models.AnswerType
import com.orbys.vote.domain.models.Question
import com.orbys.vote.ui.viewmodels.QuestionViewModel
import kotlinx.coroutines.flow.MutableStateFlow

/**
 * Fragmento que extiende de [AddQuestionFragment]
 * Muestra el formulario para generar preguntas de tipo "Estrellas"
 */
class AddStarsQuestion(viewModel: QuestionViewModel): AddQuestionFragment(viewModel) {

    override val answerType = AnswerType.STARS

    /** Función que genera un objeto [Question] con respuestas de tipo "Estrellas" */
    override fun createQuestionFromInput() = Question(
        question = binding.questionQuestion.text.toString(),
        answers = MutableStateFlow(
            listOf(Answer("1"), Answer("2"), Answer("3"), Answer("4"), Answer("5"))
        ),
        answerType = answerType,
        isAnonymous = binding.configurationsLayout.getIsAnonymous(),
        timer = binding.configurationsLayout.getTime()
    )

}