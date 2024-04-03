package com.pprior.quizz.data.flow

import com.pprior.quizz.domain.models.Answer
import com.pprior.quizz.domain.models.AnswerType
import com.pprior.quizz.domain.models.Question
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

/**
 * Clase que gestiona el flujo de datos relacionados con las preguntas y respuestas.
 *
 * @property _questionsList Un flujo de estado mutable privado que contiene una lista de preguntas.
 * @property _answer Un flujo de estado mutable privado que contiene una respuesta.
 * @property questionsList Un flujo de estado que expone la lista de preguntas.
 * @property answer Un flujo que expone la respuesta.
 */
class FlowRepository {

    private var _questionsList = MutableStateFlow<MutableList<Question>>(mutableListOf())
    private var _respondedUsers = MutableSharedFlow<MutableList<String>>(replay = 1)
    private val _questionUpdated = MutableSharedFlow<Unit>(replay = 1)

    val questionsList: StateFlow<MutableList<Question>> = _questionsList
    val questionUpdated: SharedFlow<Unit> = _questionUpdated

    // Métodos para gestionar la lista de preguntas
    fun exists(question: Question): Boolean = _questionsList.value.any { it.question == question.question }
    fun findQuestionByText(text: String): Question = _questionsList.value.find { it.question == text } ?: Question("")
    fun addQuestion(question: Question) {
        _questionsList.value.add(question)
        _questionUpdated.tryEmit(Unit)
    }
    fun deleteQuestion(question: Question) {
        _questionsList.value.remove(question)
        _questionUpdated.tryEmit(Unit)
    }
    fun updateQuestion(oldQuestion: String, newQuestion: Question) {
        val index = _questionsList.value.indexOfFirst { it.question == oldQuestion }

        // Si la pregunta existe, actualizamos la pregunta
        if (index != -1) {
            _questionsList.value[index].question = newQuestion.question
            _questionUpdated.tryEmit(Unit)
        }
    }

    // Métodos para gestionar el contador de respuestas
    fun clearAnswer(question: Question) {
        if (exists(question)) {
            val existingQuestion = _questionsList.value.first { it.question == question.question }
            existingQuestion.answers.forEach { it.count = 0 }
        }
    }
    fun incAnswer() { updateAnswerCount { it.count++ } }

    private fun updateAnswerCount(update: (Answer) -> Unit) {
        /*val currentAnswer = _answer.replayCache.firstOrNull() ?: Answer()
        update(currentAnswer)
        _answer.tryEmit(currentAnswer)*/
    }

    // Métodos para gestionar la lista de usuarios que han respondido
    fun clearRespondedUsers() { _respondedUsers.tryEmit(mutableListOf()) }
    fun addRespondedUser(user: String) {
        val users = _respondedUsers.replayCache.firstOrNull() ?: mutableListOf()
        users.add(user)
        _respondedUsers.tryEmit(users)
    }

}