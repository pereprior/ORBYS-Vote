package com.orbys.vote.domain.repositories

import com.orbys.vote.domain.models.Question

/** Inversión de dependencias para el repositorio que gestiona las operaciones con las preguntas y respuestas */
interface IQuestionRepository {
    fun getQuestion(): Question
    fun getTimerState(): Boolean
    fun generateQuestion(question: Question)
    fun setTimerAs(isTimeOut: Boolean = true)
}