package com.orbys.quizz.ui.fragments.add

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.orbys.quizz.R
import com.orbys.quizz.domain.models.Question
import com.orbys.quizz.databinding.ActivityAddQuestionBinding
import com.orbys.quizz.ui.MainActivity
import com.orbys.quizz.ui.services.LaunchService
import com.orbys.quizz.ui.viewModels.QuestionViewModel
import dagger.hilt.android.AndroidEntryPoint

/**
 * Clase que representa una actividad para añadir preguntas a la lista.
 *
 * @property viewModel ViewModel para gestionar las operaciones relacionadas con las preguntas.
 * @property binding Objeto de enlace para acceder a los elementos de la interfaz de usuario.
 */
@AndroidEntryPoint
abstract class AddActivity: AppCompatActivity() {

    private val viewModel by viewModels<QuestionViewModel>()
    protected lateinit var binding: ActivityAddQuestionBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAddQuestionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        with(binding) {
            // Asignar los listeners a los botones
            saveButton.setOnClickListener { saveQuestion() }
            closeButton.setOnClickListener {
                startActivity(Intent(it.context, MainActivity::class.java))
                finish()
            }
        }
    }

    abstract fun createQuestionFromInput(): Question

    private fun saveQuestion() {
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
            val intent = Intent(this, LaunchService::class.java)
            intent.putExtra("question", question.question)
            startService(intent)

            finish()
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