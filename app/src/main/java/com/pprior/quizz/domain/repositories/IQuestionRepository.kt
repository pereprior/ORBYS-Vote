package com.pprior.quizz.domain.repositories

import com.pprior.quizz.domain.models.Question

interface IQuestionRepository {
    // Devuelve true si la pregunta ya existe en la lista
    fun exists(question: Question): Boolean

    // Devuelve la pregunta de la lista que coincide con el texto recibido por parametro
    fun findQuestion(question: String): Question

    // Añade una pregunta a la lista
    fun addQuestion(question: Question)

    // Elimina una pregunta de la lista
    fun deleteQuestion(question: Question)

    // Incrementa el contador de una respuesta de una pregunta
    fun incAnswer(question: Question, answer: String)

    // Reinicia el contador de respuestas de una pregunta
    fun clearAnswer(question: Question)
}