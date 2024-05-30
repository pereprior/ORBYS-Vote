package com.orbys.vote.ui.view.fragments.add

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import com.orbys.vote.R
import com.orbys.vote.core.extensions.showToastWithCustomView
import com.orbys.vote.domain.models.AnswerType
import com.orbys.vote.domain.models.Question
import com.orbys.vote.ui.components.managers.TextFieldsManager
import com.orbys.vote.ui.viewmodels.QuestionViewModel

/**
 * Fragmento que extiende de [AddQuestionFragment]
 * Muestra el formulario para generar preguntas de tipo "Otros"
 *
 * @property answersManager Gestor de los campos para introducir las respuestas
 */
class AddOtherQuestion(viewModel: QuestionViewModel): AddQuestionFragment(viewModel) {

    override val answerType = AnswerType.OTHER
    private lateinit var answersManager: TextFieldsManager

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            addContainer.layoutParams.height = LinearLayout.LayoutParams.WRAP_CONTENT

            // Configurar el formulario para añadir nuevas respuestas
            addAnswersLayout.visibility = View.VISIBLE
            answersManager = TextFieldsManager(
                context = requireContext(),
                layout = answersLayout,
                minAnswers = MIN_ANSWERS,
                maxAnswers = MAX_ANSWERS
            )
            // Añadir dos respuestas por defecto
            repeat(MIN_ANSWERS) { answersManager.addAnswerField() }

            // Configurar el botón para añadir nuevas respuestas
            addAnswerButton.visibility = View.VISIBLE
            answersManager.setAddButtonListener(addAnswerButton)

            // Configurar las configuraciones adicionales
            configurationsLayout.setAdditionalConfigurations(filterUsersConfig = true, multiAnswerConfig = true, timerConfig = true)
        }

    }

    /** Función que genera un objeto [Question] con respuestas de tipo "Otros" */
    override fun createQuestionFromInput() = Question(
        question = binding.questionQuestion.text.toString(),
        answers = answersManager.getAnswers(),
        answerType = answerType,
        isAnonymous = binding.configurationsLayout.getIsAnonymous(),
        timer = binding.configurationsLayout.getTime(),
        isMultipleChoices = binding.configurationsLayout.getIsMultipleChoices(),
        isMultipleAnswers = binding.configurationsLayout.getIsMultipleAnswers()
    )

    /** Función que se encarga de lanzar la pregunta generada para que los clientes del servidor la puedan contestar */
    override fun launchQuestion(context: Context) {
        with(context) {
            // Controlar que los campos de las preguntas no estén vacíos
            if (answersManager.anyAnswerIsEmpty()) {
                showToastWithCustomView(getString(R.string.empty_answers_error))
                return
            }

            // Controlar que no haya dos preguntas iguales
            val answerTexts = answersManager.getAnswersText()
            if (answerTexts.size != answerTexts.toSet().size) {
                showToastWithCustomView(getString(R.string.same_question_error))
                return
            }

            // Controlar que no haya caracteres no permitidos en las respuestas
            if (answersManager.anyAnswerContainsInvalidCharacter()) {
                showToastWithCustomView(getString(R.string.char_unavailable_message))
                return
            }

            super.launchQuestion(this)
        }
    }

    override fun setConfigVisibilityTo(icon: Int, additionalConfigurationsVisibility: Int) {
        super.setConfigVisibilityTo(icon, additionalConfigurationsVisibility)
        // Añadimos el control de visibilidad del formulario de respuestas
        binding.addAnswersLayout.visibility = if (additionalConfigurationsVisibility == View.VISIBLE) View.GONE else View.VISIBLE
    }

    companion object {
        private const val MIN_ANSWERS = 2
        private const val MAX_ANSWERS = 5
    }

}