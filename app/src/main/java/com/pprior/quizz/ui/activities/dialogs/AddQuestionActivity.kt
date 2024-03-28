package com.pprior.quizz.ui.activities.dialogs

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.pprior.quizz.R
import com.pprior.quizz.data.flow.FlowRepository
import com.pprior.quizz.data.server.models.Question
import com.pprior.quizz.databinding.ActivityAddQuestionBinding
import org.koin.java.KoinJavaComponent.inject

/**
 * Clase que representa una actividad para añadir preguntas a la lista.
 *
 * @param flowRepository El repositorio que gestiona las preguntas.
 */
open class AddQuestionActivity: AppCompatActivity() {

    protected val repository: FlowRepository by inject(FlowRepository::class.java)
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
        if (question.title.isEmpty() || question.question.isEmpty()) {
            return
        }

        // Si ya existe la pregunta, mostrar un mensaje de error
        if (repository.exists(question)) {
            binding.errorMessage.text = getString(R.string.questions_exists)
        } else {
            // Guardamos la pregunta en la lista y cerramos la actividad
            repository.addQuestion(question)
            finish()
            clear()
        }
    }

    protected fun createQuestionFromInput() = Question(
        binding.questionTitle.text.toString(),
        binding.questionQuestion.text.toString()
    )

    private fun clear() {
        with(binding) {
            // Limpiar los campos de texto y el mensaje de error
            questionTitle.text.clear()
            questionQuestion.text.clear()
            errorMessage.text = ""
        }
    }

}