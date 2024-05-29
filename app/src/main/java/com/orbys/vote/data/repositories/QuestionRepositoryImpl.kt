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
 * @property question Un flujo mutable que contiene la información de la pregunta actual.
 * @property isTimerOut Un flujo mutable que contiene el estado del temporizador.
 */
class QuestionRepositoryImpl private constructor(): IQuestionRepository {

    private var question = MutableStateFlow(Question(""))
    private val isTimerOut = MutableStateFlow(false)

    /** Devuelve el valor de la pregunta actual */
    override fun getQuestion(): Question = question.value

    /**
     * Devuelve el valor actual del temporizador
     *
     * @return true si el temporizador ha finalizado, false en caso contrario.
     */
    override fun getTimerState(): Boolean = isTimerOut.value

    /** Genera una nueva pregunta */
    override fun generateQuestion(question: Question) { this.question.tryEmit(question) }

    /** Actualiza el estado del temporizador */
    override fun setTimerAs(isTimeOut: Boolean) { isTimerOut.tryEmit(isTimeOut) }

    /**
     * Busca si una respuesta existe en la lista de respuestas de la pregunta actual
     *
     * @param answer La respuesta a buscar
     * @return true si la respuesta existe, false en caso contrario.
     */
    fun answerExists(answer: String?) = question.value.getAnswers().any { it.answer == answer }

    /**
     *  Comprobamos la lista de respuestas de la pregunta actual y si la respuesta existe, incrementamos su contador.
     *
     *  @param answer La respuesta a incrementar
     *  @param incValue El valor en el que se incrementará el contador (por defecto 1)
     */
    fun incAnswerCount(answer: String?, incValue: Int = 1) {
        answer?.split(";")?.forEach {
            incAnswer(it, incValue)
        }
    }

    /** Añade una nueva respuesta a la lista de respuestas de la pregunta actual */
    fun addAnswer(answerText: String?) {
        if (answerText == null) return

        val existingAnswer = question.value.getAnswers().firstOrNull { it.answer == answerText }
        if (existingAnswer != null) return

        // La respuesta no existe, la añadimos a la lista
        val newAnswersList = question.value.getAnswers() + Answer(answerText)
        question.value.answers.tryEmit(newAnswersList)
    }

    /** Incrementa el contador de una respuesta */
    private fun incAnswer(answerText: String, incValue: Int) {
        question.value.getAnswers().firstOrNull { it.answer == answerText }?.let {
            it.count.tryEmit(it.getCount() + incValue)
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: QuestionRepositoryImpl? = null

        /** Instancia única de la clase QuestionRepositoryImpl */
        fun getInstance(): QuestionRepositoryImpl {
            return INSTANCE ?: synchronized(this) {
                QuestionRepositoryImpl().also { INSTANCE = it }
            }
        }
    }

}