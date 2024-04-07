package com.pprior.quizz.ui.activities.dialogs

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.pprior.quizz.R
import com.pprior.quizz.domain.models.Question
import com.pprior.quizz.databinding.ActivityAddQuestionBinding
import com.pprior.quizz.ui.viewModels.QuestionViewModel
import dagger.hilt.android.AndroidEntryPoint

/**
 * Clase que representa una actividad para añadir preguntas a la lista.
 *
 * @property viewModel ViewModel para gestionar las operaciones relacionadas con las preguntas.
 * @property binding Objeto de enlace para acceder a los elementos de la interfaz de usuario.
 */
@AndroidEntryPoint
open class AddQuestionActivity: AppCompatActivity() {

    protected val viewModel by viewModels<QuestionViewModel>()
    protected lateinit var binding: ActivityAddQuestionBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAddQuestionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        with(binding) {
            // Asignar los listeners a los botones
            saveButton.setOnClickListener { saveQuestion() }
            closeButton.setOnClickListener { finish() }
        }
    }

    protected open fun saveQuestion() {
        val question = createQuestionFromInput()

        // Comprobar si la pregunta o el título están vacíos
        if (question.question.isEmpty()) {
            return
        }

        // Si ya existe la pregunta, mostrar un mensaje de error
        if (viewModel.existsQuestion(question)) {
            binding.errorMessage.text = getString(R.string.questions_exists)
        } else {
            // Guardamos la pregunta en la lista y cerramos la actividad
            viewModel.addQuestion(question)
            finish()
            clear()
        }
    }

    protected open fun createQuestionFromInput() = Question(
        binding.questionQuestion.text.toString()
    )

    private fun clear() {
        with(binding) {
            // Limpiar los campos de texto y el mensaje de error
            questionQuestion.text.clear()
            errorMessage.text = ""
        }
    }

}