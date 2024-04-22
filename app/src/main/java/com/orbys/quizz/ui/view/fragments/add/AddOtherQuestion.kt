package com.orbys.quizz.ui.view.fragments.add

import android.content.Context
import android.os.Bundle
import android.view.View
import com.orbys.quizz.R
import com.orbys.quizz.domain.models.AnswerType
import com.orbys.quizz.domain.models.Question
import com.orbys.quizz.ui.components.managers.AnswerFieldsManager
import com.orbys.quizz.ui.components.managers.AnswerFieldsManager.Companion.MIN_ANSWERS
import dagger.hilt.android.AndroidEntryPoint

/**
 * Clase que representa una actividad para añadir preguntas de tipo "Otros".
 *
 * @property viewModel ViewModel para gestionar las operaciones relacionadas con las preguntas.
 * @property binding Objeto de enlace para acceder a los elementos de la interfaz de usuario.
 */
@AndroidEntryPoint
class AddOtherQuestion: AddFragment() {

    private lateinit var fieldsManager: AnswerFieldsManager

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fieldsManager = AnswerFieldsManager(requireContext(), binding.answersLayout)
        binding.questionTypeIcon.setImageResource(R.drawable.ic_others)
        setDefaultAnswers()
    }

    override fun saveQuestion(context: Context) {
        // Controlar que los campos de las preguntas no estén vacíos
        if (fieldsManager.anyAnswerIsEmpty()) {
            errorMessage(R.string.empty_answers_error)
            return
        }

        // Controlar que no haya dos preguntas iguales
        val answerTexts = fieldsManager.getAnswersText()
        if (answerTexts.size != answerTexts.toSet().size) {
            errorMessage(R.string.same_question_error)
            return
        }

        super.saveQuestion(context)
    }

    override fun createQuestionFromInput(): Question {
        val questionText = binding.questionQuestion.text.toString()

        return Question(
            question = questionText,
            icon = R.drawable.ic_others,
            answers = fieldsManager.getAnswers(),
            answerType = AnswerType.OTHER,
            isAnonymous = binding.anonymousQuestionOption.isChecked,
            timeOut = binding.timeoutInput.text.toString().toIntOrNull() ?: 0,
            isMultipleChoices = binding.multiAnswerQuestionOption.isChecked,
            isMultipleAnswers = binding.nonFilterUsersQuestionOption.isChecked
        )
    }

    private fun setDefaultAnswers() {
        // Boton para añadir mas respuestas
        fieldsManager.setAddAnswersButtons()

        // Añadir dos respuestas por defecto
        repeat(MIN_ANSWERS) { fieldsManager.addAnswersField() }
    }

}