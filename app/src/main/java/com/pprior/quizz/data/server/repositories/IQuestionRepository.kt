package com.pprior.quizz.data.server.repositories

interface IQuestionRepository {
    fun setPostInAnswerCount(answer: String?)
}