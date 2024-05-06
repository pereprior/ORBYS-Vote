package com.orbys.quizz.domain.repositories

import com.orbys.quizz.domain.models.Question
import kotlinx.coroutines.flow.MutableStateFlow

interface IQuestionRepository {
    // Obtiene la pregunta actual
    fun getQuestion(): Question

    // Añade una pregunta a la lista
    fun addQuestion(question: Question)

    // Incrementa el contador de una respuesta de una pregunta
    fun incAnswer(answerText: String)

    // Añade una nueva respuesta a la pregunta
    fun addAnswer(answerText: String)

    // Obtiene el estado del temporizador
    fun getTimerState(): MutableStateFlow<Boolean>

    // Indica que el tiempo limite ha terminado
    fun setTimeOut(isTimeOut: Boolean = true)
}