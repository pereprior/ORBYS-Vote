package com.orbys.quizz.domain.repositories

import com.orbys.quizz.domain.models.Question

interface IQuestionRepository {
    // Obtiene la pregunta actual
    fun getQuestion(): Question

    // Añade una pregunta a la lista
    fun addQuestion(question: Question)

    // Indica que el tiempo limite ha terminado
    fun setTimeOut(isTimeOut: Boolean = true)
}