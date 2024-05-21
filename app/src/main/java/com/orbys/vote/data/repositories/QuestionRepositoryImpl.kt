package com.orbys.vote.data.repositories

import com.orbys.vote.core.extensions.getAnswers
import com.orbys.vote.core.extensions.getCount
import com.orbys.vote.domain.models.Answer
import com.orbys.vote.domain.models.Question
import com.orbys.vote.domain.repositories.IQuestionRepository
import kotlinx.coroutines.flow.MutableStateFlow

/**
 * Clase que gestiona el flujo de datos relacionados con las preguntas y respuestas.
 *
 * @property question Un flujo mutable privado la información de la pregunta.
 * @property isTimerOut Un flujo mutable para gestionar el tiempo limite de la pregunta.
 */
class QuestionRepositoryImpl private constructor(): IQuestionRepository {
    companion object {
        // Variable para almacenar la única instancia del repositorio
        @Volatile
        private var INSTANCE: QuestionRepositoryImpl? = null

        /**
         * El repositorio debe tener una unica instancia para todos los componentes de la aplicación
         * @return La única instancia de la clase QuestionRepositoryImpl.
         */
        fun getInstance(): QuestionRepositoryImpl {
            return INSTANCE ?: synchronized(this) {
                QuestionRepositoryImpl().also { INSTANCE = it }
            }
        }
    }

    private var question = MutableStateFlow(Question(""))
    private val isTimerOut = MutableStateFlow(false)

    override fun getQuestion(): Question = question.value
    override fun addQuestion(question: Question) { this.question.value = question }
    override fun setTimeOut(isTimeOut: Boolean) { isTimerOut.tryEmit(isTimeOut) }

    // Devuelve si el tiempo de la pregunta ha terminado
    fun getTimerState(): Boolean = isTimerOut.value

    // Devuelve si la respuesta existe en la lista de respuestas
    fun answerExists(answer: String?) = question.value.getAnswers().any { it.answer == answer }

    // Añade una respuesta a la lista de respuestas
    fun addAnswer(answerText: String?) {
        if (answerText == null) return

        val existingAnswer = question.value.getAnswers().firstOrNull { it.answer == answerText }
        if (existingAnswer != null) return

        // La respuesta no existe, la añadimos a la lista
        val newAnswersList = question.value.getAnswers() + Answer(answerText)
        question.value.answers.tryEmit(newAnswersList)
    }

    // Incrementa el contador de respuestas
    fun incAnswerCount(answer: String?) {
        answer?.split(";")?.forEach { incAnswer(it) } ?: incAnswer(answer ?: "")
    }
    private fun incAnswer(answerText: String) {
        question.value.getAnswers().firstOrNull { it.answer == answerText }?.let {
            it.count.tryEmit(it.getCount() + 1)
        }
    }

}