package com.orbys.vote.ui.view.fragments.add

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.orbys.vote.R
import com.orbys.vote.core.extensions.showToastWithCustomView
import com.orbys.vote.domain.models.AnswerType
import com.orbys.vote.domain.models.Question
import com.orbys.vote.ui.components.managers.AnswerManager
import com.orbys.vote.ui.components.managers.NumericAnswersManager
import com.orbys.vote.ui.viewmodels.QuestionViewModel

/**
 * Clase que representa una actividad para añadir preguntas de tipo "Numerico".
 */
class AddNumericQuestion(viewModel: QuestionViewModel): AddFragment(viewModel) {

    override val answerType = AnswerType.NUMERIC

    private lateinit var answersManager: AnswerManager

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            addContainer.layoutParams.height = resources.getDimensionPixelSize(R.dimen.medium_fragment_layout_height)

            // Configurar el formulario para añadir el número máximo para la respuesta
            answersManager = NumericAnswersManager(
                context = requireContext(),
                layout = answersLayout,
                hintText = getString(R.string.numeric_answer_hint)
            )

            addAnswersLayout.visibility = View.VISIBLE
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

    override fun setConfigVisibilityTo(icon: Int, visible: Int) {
        super.setConfigVisibilityTo(icon, visible)
        binding.addAnswersLayout.visibility = if (visible == View.VISIBLE) View.GONE else View.VISIBLE
    }

}