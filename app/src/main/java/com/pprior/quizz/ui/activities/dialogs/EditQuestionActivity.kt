package com.pprior.quizz.ui.activities.dialogs

import android.os.Bundle

/**
 * Clase que representa una actividad para editar una pregunta existente.
 *
 * @property question La pregunta que se va a editar.
 */
class EditQuestionActivity: AddQuestionActivity() {

    private lateinit var question: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        question = intent.getStringExtra("question") ?: ""
        binding.questionQuestion.setText(question)
    }

    override fun saveQuestion() {
        val updatedQuestion = createQuestionFromInput()

        if (updatedQuestion.question.isEmpty()) {
            return
        }

        viewModel.updateQuestion(question, updatedQuestion)
        finish()
    }

}