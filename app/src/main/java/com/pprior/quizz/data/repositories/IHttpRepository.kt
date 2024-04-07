package com.pprior.quizz.data.repositories

import com.pprior.quizz.domain.models.Question

interface IHttpRepository {
    fun setPostInAnswerCount(question: Question, answer: String?)
    fun findQuestion(question: String): Question
    fun userNotExists(userIp: String): Boolean
    fun addUserToRespondedList(userIp: String)
}