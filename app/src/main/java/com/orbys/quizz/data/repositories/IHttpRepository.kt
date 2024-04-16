package com.orbys.quizz.data.repositories

import com.orbys.quizz.domain.models.Question
import com.orbys.quizz.domain.models.User
import kotlinx.coroutines.flow.MutableStateFlow

interface IHttpRepository {
    fun getQuestion(): Question
    fun setUserResponded(ip: String)
    fun timeState(): MutableStateFlow<Boolean>
    fun setPostInAnswerCount(answer: String?)
    fun userNotExists(userIp: String): Boolean
    fun addUser(user: User)
    fun userResponded(ip: String): Boolean
}