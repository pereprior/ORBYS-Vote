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
import com.orbys.vote.core.extensions.replaceFragmentOnClick
import com.orbys.vote.core.extensions.showToastWithCustomView
import com.orbys.vote.databinding.FragmentAddQuestionBinding
import com.orbys.vote.databinding.FragmentAdditionalSettingsBinding
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
        replaceFragmentOnClick(backButton, TypesQuestionFragment(viewModel))

        with(binding) {
            // Mostrar en la vista el tipo de pregunta que queremos generar
            cardIcon.setImageResource(answerType.iconResId)
            cardTitle.text = getString(answerType.titleResId)

            // Limitar el número de líneas de la pregunta
            questionQuestion.limitLines(3)

            // Configurar el botón para lanzar la pregunta
            launchButton.setOnClickListener { launchQuestion(it.context) }

            // Configuraciones adicionales de la pregunta
            bindAdditionalConfigurations()

            return root
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Configuración adicionales por defecto de las preguntas
        binding.configurationsLayout.setAdditionalConfigurations(timerConfig = true)
    }

    /**
     * Función abstracta que crea una instancia de [Question] a partir de los parametros introducidos en la interfaz.
     * La instancia cambia dependiendo del tipo de pregunta que se quiera generar
     */
    protected abstract fun createQuestionFromInput(): Question

    /** Función que se encarga de lanzar la pregunta generada para que los clientes del servidor la puedan contestar */
    protected open fun launchQuestion(context: Context) {
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

    /** Función que se encarga de gestionar las acciones de configuración adicional de la pregunta */
    private fun FragmentAddQuestionBinding.bindAdditionalConfigurations() {
        // Establecemos un tamaño fijo al fragmento para que no cambie al mostrar la configuración adicional
        addContainer.layoutParams.height = resources.getDimensionPixelSize(R.dimen.small_fragment_layout_height)

        // Mostrar la configuración adicional
        showConfigurationsLayout.setOnClickListener {
            if (configurationsLayout.childLayout.visibility == View.VISIBLE)
                setConfigVisibilityTo(R.drawable.ic_config_hide, View.GONE)
            else
                setConfigVisibilityTo(R.drawable.ic_config_show, View.VISIBLE)
        }

        // Mostrar el formulario del tiempo de espera
        with(configurationsLayout) {
            timeoutQuestionOption.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) timeoutInputLayout.visibility = View.VISIBLE
                else timeoutInputLayout.visibility = View.GONE
            }
        }
    }

    /**
     * Cambia la vista del fragmento entre el formulario de la pregunta y la configuración adicional
     *
     * @param icon Icono que indica si se muestra o no la configuración adicional
     * @param additionalConfigurationsVisibility Visibilidad de la configuración adicional
     */
    protected open fun setConfigVisibilityTo(icon: Int, additionalConfigurationsVisibility: Int) {
        with(binding) {
            iconConfigVisibility.setImageResource(icon)
            configurationsLayout.childLayout.visibility = additionalConfigurationsVisibility

            questionTitle.visibility = if (additionalConfigurationsVisibility == View.VISIBLE) View.GONE else View.VISIBLE
            questionQuestion.visibility = if (additionalConfigurationsVisibility == View.VISIBLE) View.GONE else View.VISIBLE
        }
    }

    /**
     * Gestiona las diferentes opciones disponibles en la configuración adicional de la pregunta
     *
     * @param filterUsersConfig Mostrar o no la opción de filtrar usuarios
     * @param multiAnswerConfig Mostrar o no la opción de respuestas múltiples
     * @param timerConfig Mostrar o no la opción de tiempo de espera
     */
    protected fun FragmentAdditionalSettingsBinding.setAdditionalConfigurations(
        filterUsersConfig: Boolean = false, multiAnswerConfig: Boolean = false, timerConfig: Boolean = false
    ) {
        filterUsersTitle.visibility = if (filterUsersConfig) View.VISIBLE else View.GONE
        filterUsersGroup.visibility = if (filterUsersConfig) View.VISIBLE else View.GONE

        multiAnswerTitle.visibility = if (multiAnswerConfig) View.VISIBLE else View.GONE
        multiAnswerGroup.visibility = if (multiAnswerConfig) View.VISIBLE else View.GONE

        timeoutQuestionOption.visibility = if (timerConfig) View.VISIBLE else View.GONE
    }

    /** Función que obtiene el valor del grupo del nombre de los clientes que acceden al servidor */
    protected fun FragmentAdditionalSettingsBinding.getIsAnonymous() = anonymousQuestionOption.isChecked

    /** Función que obtiene el número de veces que puede responder un cliente a una misma respuesta */
    protected fun FragmentAdditionalSettingsBinding.getIsMultipleAnswers() = nonFilterUsersQuestionOption.isChecked

    /** Función que obtiene el número de respuestas que se pueden seleccionar en una misma pregunta */
    protected fun FragmentAdditionalSettingsBinding.getIsMultipleChoices() = multiAnswerQuestionOption.isChecked

    /** Función que obtiene el valor de si la pregunta tiene tiempo limite o no */
    protected fun FragmentAdditionalSettingsBinding.getTime() = timeoutInput.text.toString().toIntOrNull() ?: 0
}