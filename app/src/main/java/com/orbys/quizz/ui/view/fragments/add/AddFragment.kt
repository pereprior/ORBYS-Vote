package com.orbys.quizz.ui.view.fragments.add

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.orbys.quizz.R
import com.orbys.quizz.core.extensions.limitLines
import com.orbys.quizz.core.extensions.showToastWithCustomView
import com.orbys.quizz.databinding.FragmentAddQuestionBinding
import com.orbys.quizz.domain.models.Question
import com.orbys.quizz.ui.services.FloatingViewService
import com.orbys.quizz.ui.view.fragments.cards.QuestionTypesCard
import com.orbys.quizz.ui.viewmodels.QuestionViewModel
import dagger.hilt.android.AndroidEntryPoint

/**
 * Clase abstracta que representa un Fragmento para añadir nueva pregunta a lanzar
 */
@AndroidEntryPoint
abstract class AddFragment: Fragment() {

    private lateinit var viewModel: QuestionViewModel
    protected lateinit var binding: FragmentAddQuestionBinding
    
    protected abstract val titleResId: Int
    protected abstract val iconResId: Int
    protected abstract val cardType: QuestionTypesCard

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(this)[QuestionViewModel::class.java]
        binding = FragmentAddQuestionBinding.inflate(inflater, container, false)

        with(binding) {
            // Configurar el título y el icono de la pregunta
            parentFragmentManager.beginTransaction()
                .add(cardTypeContainer.id, cardType)
                .commit()

            // Limitar el número de lineas de la pregunta
            questionQuestion.limitLines(3)

            // Configurar el boton para lanzar la pregunta
            launchButton.setOnClickListener { saveQuestion(it.context) }

            // Mostrar la configuración adicional
            showConfigurationsLayout.setOnClickListener {
                if (configurationsLayout.visibility == View.VISIBLE)
                    setConfigVisibilityTo(R.drawable.ic_config_hide, View.GONE)
                else
                    setConfigVisibilityTo(R.drawable.ic_config_show, View.VISIBLE)
            }

            // Mostrar el formulario del tiempo de espera
            timeoutQuestionOption.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) setTimerVisibilityTo(View.VISIBLE) else setTimerVisibilityTo(View.GONE)
            }

            return root
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Configuración adicionales por defecto de las preguntas
        binding.setAdditionalConfigurations(
            filterUsersConfig = false, multiAnswerConfig = false
        )
    }

    protected abstract fun createQuestionFromInput(): Question

    protected open fun saveQuestion(context: Context) {
        val question = createQuestionFromInput()

        // Comprobar si la pregunta o el título están vacíos
        if (question.question.isEmpty()) {
            context.showToastWithCustomView(getString(R.string.empty_answers_error), Toast.LENGTH_LONG)
            return
        }

        viewModel.addQuestion(question)

        // Lanzar la actividad para añadir respuestas
        context.startService(Intent(context, FloatingViewService::class.java))

        activity?.finish()
        binding.questionQuestion.text.clear()
    }

    // Configurar las opciones adicionales de la pregunta
    protected fun FragmentAddQuestionBinding.setAdditionalConfigurations(
        filterUsersConfig: Boolean = true,
        multiAnswerConfig: Boolean = true,
        timerConfig: Boolean = true
    ) {
        filterUsersTitle.visibility = if (filterUsersConfig) View.VISIBLE else View.GONE
        filterUsersGroup.visibility = if (filterUsersConfig) View.VISIBLE else View.GONE
        filterUsersDivider.visibility = if (filterUsersConfig) View.VISIBLE else View.GONE
        multiAnswerTitle.visibility = if (multiAnswerConfig) View.VISIBLE else View.GONE
        multiAnswerGroup.visibility = if (multiAnswerConfig) View.VISIBLE else View.GONE
        multiAnswerDivider.visibility = if (multiAnswerConfig) View.VISIBLE else View.GONE
        timeoutQuestionOption.visibility = if (timerConfig) View.VISIBLE else View.GONE
    }

    // Cambiar la visibilidad del formulario del temporizador
    private fun FragmentAddQuestionBinding.setTimerVisibilityTo(visible: Int) {
        timeoutInput.visibility = visible
        minutesHelpText.visibility = visible
    }

    // Cambiar la visibilidad de la confirguración adicional
    private fun FragmentAddQuestionBinding.setConfigVisibilityTo(icon: Int, visible: Int) {
        iconConfigVisibility.setImageResource(icon)
        configurationsLayout.visibility = visible
    }

}