package com.pprior.quizz.ui.activities.dialogs

import android.os.Bundle

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

        repository.updateQuestion(question, updatedQuestion)
        finish()
    }

}