package com.pprior.quizz.data.flow

import android.util.Log
import com.pprior.quizz.data.models.Answer
import com.pprior.quizz.data.models.Question
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow

/**
 * Clase que gestiona el flujo de datos relacionados con las preguntas y respuestas.
 *
 * @property _questionsList Un flujo de estado mutable privado que contiene una lista de preguntas.
 * @property _answer Un flujo de estado mutable privado que contiene una respuesta.
 * @property questionsList Un flujo de estado que expone la lista de preguntas.
 * @property answer Un flujo que expone la respuesta.
 */
class FlowRepository {

    private val _answer = MutableSharedFlow<Answer>(replay = 1)
    private var _questionsList = MutableStateFlow<MutableList<Question>>(mutableListOf())

    val answer: Flow<Answer> = _answer.asSharedFlow()
    val questionsList: StateFlow<MutableList<Question>> = _questionsList

    init {
        clearAnswer()
    }

    fun exists(question: Question): Boolean = _questionsList.value.any { it.title == question.title }
    fun addQuestion(question: Question) { _questionsList.value.add(question) }
    fun updateQuestion(oldQuestion: Question, newQuestion: Question) {
        val index = _questionsList.value.indexOf(oldQuestion)

        _questionsList.value.removeAt(index)
        _questionsList.value.add(newQuestion)
    }

    fun clearAnswer() { _answer.tryEmit(Answer()) }
    fun incYesAnswer() {
        val currentAnswer = _answer.replayCache.firstOrNull() ?: Answer()
        currentAnswer.yesCount++
        _answer.tryEmit(currentAnswer)
    }
    fun incNoAnswer() {
        val currentAnswer = _answer.replayCache.firstOrNull() ?: Answer()
        currentAnswer.noCount++
        _answer.tryEmit(currentAnswer)
    }
}