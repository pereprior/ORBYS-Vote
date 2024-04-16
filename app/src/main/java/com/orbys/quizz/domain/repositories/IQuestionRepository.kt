package com.orbys.quizz.domain.repositories

import com.orbys.quizz.domain.models.Question

interface IQuestionRepository {
    // Añade una pregunta a la lista
    fun addQuestion(question: Question)

    // Incrementa el contador de una respuesta de una pregunta
    fun incAnswer(answer: String)

    // Reinicia el contador de respuestas de una pregunta
    fun clearAnswer(question: Question)

    // Indica que el tiempo limite ha terminado
    fun timeOut()

    // Reinicia el temporizador
    fun resetTimer()
}