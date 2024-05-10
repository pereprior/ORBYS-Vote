package com.orbys.quizz.ui.view.fragments.add

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import com.orbys.quizz.R
import com.orbys.quizz.domain.models.AnswerType
import com.orbys.quizz.domain.models.Question
import com.orbys.quizz.ui.components.managers.TextAnswersManager

/**
 * Clase que representa una actividad para añadir preguntas de tipo "Otros".
 */
class AddOtherQuestion: AddFragment() {

    override val titleResId: Int = R.string.other_question_type_title
    override val iconResId: Int = R.drawable.ic_others

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
                maxAnswers = MAX_ANSWERS,
                fieldLength = LinearLayout.LayoutParams.MATCH_PARENT
            )

            setAdditionalConfigurations()
        }

        // Añadir dos respuestas por defecto
        fieldsManager.addButtonForAddAnswers()
        repeat(MIN_ANSWERS) { fieldsManager.addAnswerField() }
    }

    override fun saveQuestion(context: Context) {
        // Controlar que los campos de las preguntas no estén vacíos
        if (fieldsManager.anyAnswerIsEmpty()) {
            errorMessage(R.string.empty_answers_error, false)
            return
        }

        // Controlar que no haya dos preguntas iguales
        val answerTexts = fieldsManager.getAnswersText()
        if (answerTexts.size != answerTexts.toSet().size) {
            errorMessage(R.string.same_question_error, false)
            return
        }

        super.saveQuestion(context)
    }

    override fun createQuestionFromInput() = Question(
        question = binding.questionQuestion.text.toString(),
        icon = R.drawable.ic_others,
        answers = fieldsManager.getAnswers(),
        answerType = AnswerType.OTHER,
        isAnonymous = binding.anonymousQuestionOption.isChecked,
        timeOut = binding.timeoutInput.text.toString().toIntOrNull() ?: 0,
        isMultipleChoices = binding.multiAnswerQuestionOption.isChecked,
        isMultipleAnswers = binding.nonFilterUsersQuestionOption.isChecked
    )

}