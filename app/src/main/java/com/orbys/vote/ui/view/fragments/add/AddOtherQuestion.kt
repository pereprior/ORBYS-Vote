package com.orbys.vote.ui.view.fragments.add

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import com.orbys.vote.R
import com.orbys.vote.core.extensions.showToastWithCustomView
import com.orbys.vote.domain.models.AnswerType
import com.orbys.vote.domain.models.Question
import com.orbys.vote.ui.components.managers.TextAnswersManager

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
            addContainer.layoutParams.height = LinearLayout.LayoutParams.WRAP_CONTENT

            // Configurar el formulario para añadir las respuestas
            fieldsManager = TextAnswersManager(
                context = requireContext(),
                layout = answersLayout,
                minAnswers = MIN_ANSWERS,
                maxAnswers = MAX_ANSWERS
            )

            addAnswersLayout.visibility = View.VISIBLE
            addAnswerButton.visibility = View.VISIBLE
            fieldsManager.setAddButtonListener(addAnswerButton)

            setAdditionalConfigurations()
        }

        // Añadir dos respuestas por defecto
        repeat(MIN_ANSWERS) { fieldsManager.addAnswerField() }
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

    override fun setConfigVisibilityTo(icon: Int, visible: Int) {
        super.setConfigVisibilityTo(icon, visible)
        binding.addAnswersLayout.visibility = if (visible == View.VISIBLE) View.GONE else View.VISIBLE
    }

}