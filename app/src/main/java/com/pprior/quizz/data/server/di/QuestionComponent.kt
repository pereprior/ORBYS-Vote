package com.pprior.quizz.data.server.di

import com.pprior.quizz.data.server.repositories.QuestionRepositoryImp
import com.pprior.quizz.domain.models.Question
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class QuestionComponent: KoinComponent {

    private val questionRepository: QuestionRepositoryImp by inject()
    fun getQuestion(title: String): Question {
        return questionRepository.getQuestionByTitle(title)
    }

}