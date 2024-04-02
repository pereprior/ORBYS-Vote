package com.pprior.quizz.ui.activities.dialogs

import android.os.Bundle
import com.pprior.quizz.domain.models.Question

class EditQuestionActivity: AddQuestionActivity() {

    private lateinit var question: Question

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        createQuestionFromIntents()

        binding.questionQuestion.setText(question.question)
    }

    override fun saveQuestion() {
        val updatedQuestion = createQuestionFromInput()

        if (updatedQuestion.question.isEmpty()) {
            return
        }

        repository.updateQuestion(question, updatedQuestion)
        finish()
    }

    private fun createQuestionFromIntents() {
        val questionQuestion = intent.getStringExtra("questionQuestion") ?: ""
        question = Question(questionQuestion)
    }

}