package com.orbys.vote.ui.view.fragments.add

import com.orbys.vote.R
import com.orbys.vote.domain.models.Answer
import com.orbys.vote.domain.models.AnswerType
import com.orbys.vote.domain.models.Question
import com.orbys.vote.ui.viewmodels.QuestionViewModel
import kotlinx.coroutines.flow.MutableStateFlow

/**
 * Fragmento que extiende de [AddQuestionFragment]
 * Muestra el formulario para generar preguntas de tipo "Verdadero o Falso"
 */
class AddBooleanQuestion(viewModel: QuestionViewModel) : AddQuestionFragment(viewModel) {

    override val answerType = AnswerType.BOOLEAN

    /** Función que genera un objeto [Question] con respuestas de tipo "Verdadero o Falso" */
    override fun createQuestionFromInput() = Question(
        question = binding.questionQuestion.text.toString(),
        answers = MutableStateFlow(listOf(Answer(this.getString(R.string.true_answer_placeholder)), Answer(this.getString(R.string.false_answer_placeholder)))),
        answerType = answerType,
        isAnonymous = binding.configurationsLayout.getIsAnonymous(),
        timer = binding.configurationsLayout.getTime()
    )

}