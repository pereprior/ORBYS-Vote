package com.orbys.quizz.ui.view.fragments.add

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.orbys.quizz.R
import com.orbys.quizz.core.extensions.showToastWithCustomView
import com.orbys.quizz.domain.models.AnswerType
import com.orbys.quizz.domain.models.Question
import com.orbys.quizz.ui.components.managers.AnswerManager
import com.orbys.quizz.ui.components.managers.NumericAnswersManager

/**
 * Clase que representa una actividad para añadir preguntas de tipo "Numerico".
 */
class AddNumericQuestion: AddFragment() {

    override val answerType = AnswerType.NUMERIC

    private lateinit var answersManager: AnswerManager

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            // Configurar el formulario para añadir el número máximo para la respuesta
            answersManager = NumericAnswersManager(
                context = requireContext(),
                layout = answersLayout,
                hintText = getString(R.string.numeric_answer_hint)
            )

            setAdditionalConfigurations(multiAnswerConfig = false)
        }

        answersManager.addAnswerField()
    }

    override fun createQuestionFromInput() = Question(
        question = binding.questionQuestion.text.toString(),
        answerType = answerType,
        maxNumericAnswer = answersManager.getAnswersText().firstOrNull()?.toIntOrNull() ?: 0,
        isAnonymous = binding.anonymousQuestionOption.isChecked,
        timeOut = binding.timeoutInput.text.toString().toIntOrNull() ?: 0,
        isMultipleAnswers = binding.nonFilterUsersQuestionOption.isChecked
    )

    override fun saveQuestion(context: Context) {
        if (answersManager.anyAnswerIsEmpty()) {
            context.showToastWithCustomView(getString(R.string.empty_answers_error), Toast.LENGTH_LONG)
            return
        }

        super.saveQuestion(context)
    }

}