package com.orbys.quizz.ui.view.fragments.add

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.orbys.quizz.R
import com.orbys.quizz.core.extensions.showToastWithCustomView
import com.orbys.quizz.domain.models.AnswerType
import com.orbys.quizz.domain.models.Question
import com.orbys.quizz.ui.components.managers.TextAnswersManager

/**
 * Clase que representa una actividad para añadir preguntas de tipo "Otros".
 */
class AddOtherQuestion: AddFragment() {

    override val answerType = AnswerType.OTHER

    private lateinit var fieldsManager: TextAnswersManager

    companion object {
        private const val MIN_ANSWERS = 2
        private const val MAX_ANSWERS = 5
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            // Configurar el formulario para añadir las respuestas
            fieldsManager = TextAnswersManager(
                context = requireContext(),
                layout = answersLayout,
                minAnswers = MIN_ANSWERS,
                maxAnswers = MAX_ANSWERS
            )

            setAdditionalConfigurations()
        }

        // Añadir dos respuestas por defecto
        repeat(MIN_ANSWERS) { fieldsManager.addAnswerField() }
        fieldsManager.addButtonForAddAnswers()
    }

    override fun saveQuestion(context: Context) {
        // Controlar que los campos de las preguntas no estén vacíos
        if (fieldsManager.anyAnswerIsEmpty()) {
            context.showToastWithCustomView(getString(R.string.empty_answers_error), Toast.LENGTH_LONG)
            return
        }

        // Controlar que no haya dos preguntas iguales
        val answerTexts = fieldsManager.getAnswersText()
        if (answerTexts.size != answerTexts.toSet().size) {
            context.showToastWithCustomView(getString(R.string.same_question_error), Toast.LENGTH_LONG)
            return
        }

        super.saveQuestion(context)
    }

    override fun createQuestionFromInput() = Question(
        question = binding.questionQuestion.text.toString(),
        answers = fieldsManager.getAnswers(),
        answerType = answerType,
        isAnonymous = binding.anonymousQuestionOption.isChecked,
        timeOut = binding.timeoutInput.text.toString().toIntOrNull() ?: 0,
        isMultipleChoices = binding.multiAnswerQuestionOption.isChecked,
        isMultipleAnswers = binding.nonFilterUsersQuestionOption.isChecked
    )

}