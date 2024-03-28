package com.pprior.quizz.ui.activities.dialogs

import android.os.Bundle
import com.pprior.quizz.domain.models.Question

class EditQuestionActivity: AddQuestionActivity() {

    private lateinit var question: Question

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        createQuestionFromIntents()

        with(binding) {
            questionTitle.setText(question.title)
            questionQuestion.setText(question.question)
        }
    }

    override fun saveQuestion() {
        val updatedQuestion = createQuestionFromInput()

        if (updatedQuestion.title.isEmpty() || updatedQuestion.question.isEmpty()) {
            return
        }

        repository.updateQuestion(question, updatedQuestion)
        finish()
    }

    private fun createQuestionFromIntents() {
        val questionTitle = intent.getStringExtra("questionTitle") ?: ""
        val questionQuestion = intent.getStringExtra("questionQuestion") ?: ""
        question = Question(questionTitle, questionQuestion)
    }

}