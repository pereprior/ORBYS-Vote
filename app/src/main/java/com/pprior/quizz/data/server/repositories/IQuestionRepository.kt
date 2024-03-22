package com.pprior.quizz.data.server.repositories

import com.pprior.quizz.domain.models.Question

interface IQuestionRepository {
    fun getQuestionByTitle(title: String): Question
}