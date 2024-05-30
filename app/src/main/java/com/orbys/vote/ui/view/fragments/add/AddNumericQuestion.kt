package com.orbys.vote.ui.view.fragments.add

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.orbys.vote.R
import com.orbys.vote.core.extensions.showToastWithCustomView
import com.orbys.vote.domain.models.AnswerType
import com.orbys.vote.domain.models.Question
import com.orbys.vote.ui.components.managers.AnswerFieldsManager
import com.orbys.vote.ui.components.managers.NumericFieldsManager
import com.orbys.vote.ui.viewmodels.QuestionViewModel

/**
 * Fragmento que extiende de [AddQuestionFragment]
 * Muestra el formulario para generar preguntas de tipo "Numérico"
 *
 * @property answersManager Gestor de los campos para introducir las respuestas
 */
class AddNumericQuestion(viewModel: QuestionViewModel): AddQuestionFragment(viewModel) {

    override val answerType = AnswerType.NUMERIC

    private lateinit var answersManager: AnswerFieldsManager

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            // Ajustar el tamaño del contenedor del formulario
            addContainer.layoutParams.height = resources.getDimensionPixelSize(R.dimen.medium_fragment_layout_height)

            // Configurar el formulario para añadir el número máximo para la respuesta
            addAnswersLayout.visibility = View.VISIBLE
            answersManager = NumericFieldsManager(requireContext(), answersLayout, getString(R.string.numeric_answer_hint))
            answersManager.addAnswerField()

            // Configurar las configuraciones adicionales
            configurationsLayout.setAdditionalConfigurations(filterUsersConfig = true, timerConfig = true)
        }

    }

    /** Función que genera un objeto [Question] con respuestas de tipo "Numérico" */
    override fun createQuestionFromInput() = Question(
        question = binding.questionQuestion.text.toString(),
        answerType = answerType,
        maxNumericAnswer = answersManager.getAnswersText().firstOrNull()?.toIntOrNull() ?: 0,
        isAnonymous = binding.configurationsLayout.getIsAnonymous(),
        timer = binding.configurationsLayout.getTime(),
        isMultipleAnswers = binding.configurationsLayout.getIsMultipleAnswers()
    )

    /** Función que se encarga de lanzar la pregunta generada para que los clientes del servidor la puedan contestar */
    override fun launchQuestion(context: Context) {
        // Controlar que los campos de las preguntas no estén vacíos
        if (answersManager.anyAnswerIsEmpty()) {
            context.showToastWithCustomView(getString(R.string.empty_answers_error), Toast.LENGTH_LONG)
            return
        }

        super.launchQuestion(context)
    }

    override fun setConfigVisibilityTo(icon: Int, additionalConfigurationsVisibility: Int) {
        super.setConfigVisibilityTo(icon, additionalConfigurationsVisibility)
        // Añadimos el control de visibilidad del formulario de respuestas
        binding.addAnswersLayout.visibility = if (additionalConfigurationsVisibility == View.VISIBLE) View.GONE else View.VISIBLE
    }

}