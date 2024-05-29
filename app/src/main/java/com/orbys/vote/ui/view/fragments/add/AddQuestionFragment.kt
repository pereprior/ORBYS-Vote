package com.orbys.vote.ui.view.fragments.add

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.orbys.vote.R
import com.orbys.vote.core.extensions.limitLines
import com.orbys.vote.core.extensions.showToastWithCustomView
import com.orbys.vote.databinding.FragmentAddQuestionBinding
import com.orbys.vote.domain.models.AnswerType
import com.orbys.vote.domain.models.Question
import com.orbys.vote.ui.services.FloatingViewService
import com.orbys.vote.ui.view.fragments.TypesQuestionFragment
import com.orbys.vote.ui.viewmodels.QuestionViewModel
import dagger.hilt.android.AndroidEntryPoint

/**
 * Clase abstracta que contiene la lógica común de un fragmento para generar una nueva pregunta
 *
 * @property viewModel Contiene los datos de las preguntas las funciones para poder gestionarlas
 */
@AndroidEntryPoint
abstract class AddQuestionFragment(private val viewModel: QuestionViewModel): Fragment() {

    protected lateinit var binding: FragmentAddQuestionBinding

    // Variable abstracta que contiene el tipo de pregunta a generar
    protected abstract val answerType: AnswerType

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddQuestionBinding.inflate(inflater, container, false)

        val backButton: ImageView = activity?.findViewById(R.id.app_logo)!!
        backButton.setOnClickListener {
            parentFragmentManager.beginTransaction().apply {
                replace(R.id.fragment_container, TypesQuestionFragment(viewModel))
                addToBackStack(null)
                commit()
            }
        }

        with(binding) {
            // Configurar el título y el icono de la pregunta
            addContainer.layoutParams.height = resources.getDimensionPixelSize(R.dimen.small_fragment_layout_height)

            // Configurar el tipo de pregunta
            cardIcon.setImageResource(answerType.iconResId)
            cardTitle.text = getString(answerType.titleResId)

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
                if (isChecked) timeoutInputLayout.visibility = View.VISIBLE else timeoutInputLayout.visibility = View.GONE
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

        viewModel.generateQuestion(question)

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
        multiAnswerTitle.visibility = if (multiAnswerConfig) View.VISIBLE else View.GONE
        multiAnswerGroup.visibility = if (multiAnswerConfig) View.VISIBLE else View.GONE
        timeoutQuestionOption.visibility = if (timerConfig) View.VISIBLE else View.GONE
    }

    // Cambiar la visibilidad de la confirguración adicional
    protected open fun setConfigVisibilityTo(icon: Int, visible: Int) {
        with(binding) {
            iconConfigVisibility.setImageResource(icon)
            configurationsLayout.visibility = visible
            questionTitle.visibility = if (visible == View.VISIBLE) View.GONE else View.VISIBLE
            questionQuestion.visibility = if (visible == View.VISIBLE) View.GONE else View.VISIBLE
        }
    }

}