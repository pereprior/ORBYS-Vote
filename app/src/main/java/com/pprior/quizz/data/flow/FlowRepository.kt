package com.pprior.quizz.data.flow

import com.pprior.quizz.data.server.models.Answer
import com.pprior.quizz.data.server.models.Question
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
    private var _respondedUsers = MutableSharedFlow<MutableList<String>>(replay = 1)

    val answer: Flow<Answer> = _answer.asSharedFlow()
    val questionsList: StateFlow<MutableList<Question>> = _questionsList

    init {
        clearAnswer()
    }

    // Métodos para gestionar la lista de preguntas
    fun exists(question: Question): Boolean = _questionsList.value.any { it.title == question.title }
    fun addQuestion(question: Question) { _questionsList.value.add(question) }
    fun updateQuestion(oldQuestion: Question, newQuestion: Question) {
        val index = _questionsList.value.indexOf(oldQuestion)

        _questionsList.value.removeAt(index)
        _questionsList.value.add(newQuestion)
    }

    // Métodos para gestionar el contador de respuestas
    fun clearAnswer() { _answer.tryEmit(Answer()) }
    fun incYesAnswer() { updateAnswerCount { it.yesCount++ } }
    fun incNoAnswer() { updateAnswerCount { it.noCount++ } }

    private fun updateAnswerCount(update: (Answer) -> Unit) {
        val currentAnswer = _answer.replayCache.firstOrNull() ?: Answer()
        update(currentAnswer)
        _answer.tryEmit(currentAnswer)
    }

    // Métodos para gestionar la lista de usuarios que han respondido
    fun clearRespondedUsers() { _respondedUsers.tryEmit(mutableListOf()) }
    fun addRespondedUser(user: String) {
        val users = _respondedUsers.replayCache.firstOrNull() ?: mutableListOf()
        users.add(user)
        _respondedUsers.tryEmit(users)
    }
    fun exists(userIp: String): Boolean {
        val users = _respondedUsers.replayCache.firstOrNull() ?: mutableListOf()
        return users.contains(userIp)
    }

}