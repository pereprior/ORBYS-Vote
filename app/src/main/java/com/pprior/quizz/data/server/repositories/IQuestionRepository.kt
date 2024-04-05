package com.pprior.quizz.data.server.repositories

import com.pprior.quizz.domain.models.Question

interface IQuestionRepository {
    // Incrementa el contador de respuestas
    suspend fun setPostInAnswerCount(question: Question, answer: String?)

    fun findQuestion(question: String): Question

    // Añade un usuario a la lista de usuarios que han respondido
    fun addUserToRespondedList(userIp: String)
}