package com.pprior.quizz.data.server.repositories

interface IQuestionRepository {
    // Incrementa el contador de respuestas
    fun setPostInAnswerCount(answer: String?)
    // Añade un usuario a la lista de usuarios que han respondido
    fun addUserToRespondedList(userIp: String)
}