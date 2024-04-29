package com.orbys.quizz.data.repositories

import com.orbys.quizz.domain.models.Question
import com.orbys.quizz.domain.models.User
import kotlinx.coroutines.flow.MutableStateFlow

interface IHttpRepository {
    fun getQuestionInfo(): Question
    fun setUserAsResponded(ip: String)
    fun timerState(): MutableStateFlow<Boolean>
    fun setPostInAnswerCount(answer: String?)
    fun userNotExists(userIp: String): Boolean
    fun addUserToRespondedList(user: User)
    fun userResponded(ip: String): Boolean
    fun getUsernameByIp(ip: String): String
    fun addAnswerToList(answer: String?)
}