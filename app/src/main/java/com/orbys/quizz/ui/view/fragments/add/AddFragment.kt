package com.orbys.quizz.ui.view.fragments.add

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.orbys.quizz.R
import com.orbys.quizz.databinding.FragmentAddQuestionBinding
import com.orbys.quizz.domain.models.Question
import com.orbys.quizz.ui.services.FloatingViewService
import com.orbys.quizz.ui.view.fragments.TypesQuestionFragment
import com.orbys.quizz.ui.viewmodels.QuestionViewModel
import dagger.hilt.android.AndroidEntryPoint

/**
 * Clase abstracta que representa un Fragmento para añadir nueva pregunta a lanzar
 *
 * @property viewModel ViewModel para gestionar las operaciones relacionadas con las preguntas.
 * @property binding Objeto de enlace para acceder a los elementos de la interfaz de usuario.
 */
@AndroidEntryPoint
abstract class AddFragment: Fragment() {

    private lateinit var viewModel: QuestionViewModel
    protected lateinit var binding: FragmentAddQuestionBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(this)[QuestionViewModel::class.java]
        binding = FragmentAddQuestionBinding.inflate(inflater, container, false)

        with(binding) {
            val button: ImageButton = activity?.findViewById(R.id.back_button) ?: return root
            setBackButtonVisible(button)

            // Asignar los listeners a los botones
            saveButton.setOnClickListener { saveQuestion(it.context) }

            configurationsIcon.setOnClickListener {
                if (configurationsLayout.visibility == View.VISIBLE) setConfigVisible(R.drawable.ic_config_hide, View.GONE)
                else setConfigVisible(R.drawable.ic_config_show, View.VISIBLE)
            }

            timeoutQuestionOption.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) setTimerVisibility(View.VISIBLE) else setTimerVisibility(View.GONE)
            }

            return root
        }
    }

    abstract fun createQuestionFromInput(): Question

    protected open fun saveQuestion(context: Context) {
        val question = createQuestionFromInput()

        // Comprobar si la pregunta o el título están vacíos
        if (question.question.isEmpty()) {
            errorMessage(R.string.empty_answers_error)
            return
        }

        viewModel.addQuestion(question)

        // Lanzar la actividad para añadir respuestas
        context.startService(Intent(context, FloatingViewService::class.java))

        activity?.finish()
        clear()
    }

    protected fun errorMessage(message: Int) {
        binding.errorMessage.text = getString(message)
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    // Cambiar la visibilidad del formulario del cronometro
    private fun setTimerVisibility(visible: Int) {
        binding.timeoutInput.visibility = visible
        binding.minutesHelpText.visibility = visible
    }

    // Cambiar la visibilidad de la confirguración adicional
    private fun setConfigVisible(icon: Int, visible: Int) {
        binding.configurationsIcon.setImageResource(icon)
        binding.configurationsLayout.visibility = visible
    }

    // Cambiar la visibilidad del boton de volver al fragmento anterior
    private fun setBackButtonVisible(button: ImageButton) {
        button.visibility = View.VISIBLE
        button.setOnClickListener {
            parentFragmentManager.beginTransaction().apply {
                replace(R.id.fragment_container, TypesQuestionFragment())
                addToBackStack(null)
                commit()
            }
        }
    }

    private fun clear() {
        with(binding) {
            // Limpiar los campos de texto y el mensaje de error
            questionQuestion.text.clear()
            errorMessage.text = ""
        }
    }

}