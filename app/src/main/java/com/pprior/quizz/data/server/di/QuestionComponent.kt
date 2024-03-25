package com.pprior.quizz.data.server.di

import com.pprior.quizz.data.models.Answer
import com.pprior.quizz.data.server.repositories.QuestionRepositoryImp
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class QuestionComponent: KoinComponent {

    private val questionRepository: QuestionRepositoryImp by inject()

    fun setAnswer(answer: String?) {
        return questionRepository.setPostInAnswerCount(answer)
    }

    fun getAnswer(): Answer {
        return questionRepository.getAnswer()
    }

}