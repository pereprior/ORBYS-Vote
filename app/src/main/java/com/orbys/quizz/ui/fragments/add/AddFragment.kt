package com.orbys.quizz.ui.fragments.add

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.orbys.quizz.R
import com.orbys.quizz.domain.models.Question
import com.orbys.quizz.databinding.ActivityAddQuestionBinding
import com.orbys.quizz.ui.fragments.TypesQuestionFragment
import com.orbys.quizz.ui.services.FloatingViewService
import com.orbys.quizz.ui.viewModels.QuestionViewModel
import dagger.hilt.android.AndroidEntryPoint

/**
 * Clase que representa una actividad para añadir preguntas a la lista.
 *
 * @property viewModel ViewModel para gestionar las operaciones relacionadas con las preguntas.
 * @property binding Objeto de enlace para acceder a los elementos de la interfaz de usuario.
 */
@AndroidEntryPoint
abstract class AddFragment: Fragment() {

    private lateinit var viewModel: QuestionViewModel
    protected lateinit var binding: ActivityAddQuestionBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Recogemos la instancia del ViewModel
        viewModel = ViewModelProvider(this)[QuestionViewModel::class.java]
        binding = ActivityAddQuestionBinding.inflate(inflater, container, false)

        with(binding) {
            // Asignar los listeners a los botones
            saveButton.setOnClickListener { saveQuestion(it.context) }
            closeButton.setOnClickListener {
                parentFragmentManager.beginTransaction().apply {
                    replace(R.id.fragment_container, TypesQuestionFragment())
                    addToBackStack(null)
                    commit()
                }
            }
        }

        return binding.root
    }

    abstract fun createQuestionFromInput(): Question

    protected fun saveQuestion(context: Context) {
        val question = createQuestionFromInput()

        // Comprobar si la pregunta o el título están vacíos
        if (question.question.isEmpty()) {
            return
        }

        // Si ya existe la pregunta, mostrar un mensaje de error
        if (viewModel.existsQuestion(question)) {
            binding.errorMessage.text = getString(R.string.error_questions_exists)
        } else {
            // Guardamos la pregunta en la lista y cerramos la actividad
            viewModel.addQuestion(question)

            // Lanzar la actividad para añadir respuestas
            val intent = Intent(context, FloatingViewService::class.java)
            intent.putExtra("question", question.question)
            context.startService(intent)

            activity?.finish()
            clear()
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