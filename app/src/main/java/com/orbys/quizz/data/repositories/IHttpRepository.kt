package com.orbys.quizz.data.repositories

import com.orbys.quizz.domain.models.User

interface IHttpRepository {
    fun setPostInAnswerCount(answer: String?)
    //fun findQuestion(questionID: String): Question
    fun userNotExists(userIp: String): Boolean
    fun addUserToRespondedList(user: User)
    fun userResponded(ip: String): Boolean
}