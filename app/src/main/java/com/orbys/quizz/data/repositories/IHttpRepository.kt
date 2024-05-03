package com.orbys.quizz.data.repositories

import com.orbys.quizz.domain.models.Question
import com.orbys.quizz.domain.models.User

interface IHttpRepository {
    fun getQuestionInfo(): Question
    fun setUserAsResponded(ip: String)
    fun isTimeOut(): Boolean
    fun incAnswerCount(answer: String?)
    fun addUserToList(user: User)
    fun addAnswerToList(answer: String?)
    fun answerExists(answer: String?): Boolean
    fun getAnswersAsString(): List<String>
    fun userNotExists(userIp: String): Boolean
    fun usernameExists(username: String): Boolean
    fun userResponded(ip: String): Boolean
    fun getUsernameByIp(ip: String): String
}