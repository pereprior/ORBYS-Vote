package com.pprior.quizz.ui.activities

import android.os.Bundle
import com.pprior.quizz.data.flow.FlowRepository
import com.pprior.quizz.data.server.models.Question
import org.koin.java.KoinJavaComponent.inject

/**
 * Clase que representa una actividad para editar preguntas de la lista.
 *
 * @param flowRepository El repositorio que gestiona las preguntas.
 * @param question La pregunta a editar.
 */
/*class EditQuestionActivity: AddQuestionActivity() {

    private val repository: FlowRepository by inject(FlowRepository::class.java)
    private lateinit var question: Question

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        question = intent.getStringExtra("question") ?: ""

        with(binding) {
            // Rellenar los campos de texto con los datos de la pregunta
            questionTitle.setText(question.title)
            questionQuestion.setText(question.question)
        }
    }

    override fun saveQuestion() {
        val updatedQuestion = createQuestionFromInput()

        // Comprobar si la pregunta o el título están vacíos
        if (updatedQuestion.title.isEmpty() || updatedQuestion.question.isEmpty()) {
            return
        }

        // Actualizar la pregunta en la lista y cerrar la actividad
        repository.updateQuestion(question, updatedQuestion)
        finish()
    }

}*/