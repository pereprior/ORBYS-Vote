package com.orbys.quizz.domain.repositories

import com.orbys.quizz.domain.models.Answer
import com.orbys.quizz.domain.models.Question
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

/**
 * Clase que gestiona el flujo de datos relacionados con las preguntas y respuestas.
 *
 * @property _question Un flujo mutable privado la información de la pregunta.
 * @property question Un flujo inmutable y publico para llamar a la pregunta.
 * @property timeOut Un flujo mutable para gestionar el tiempo limite de la pregunta.
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

    private var _question = MutableStateFlow(Question(""))
    val question: StateFlow<Question> = _question
    val timeOut = MutableStateFlow(false)

    // Metodos para gestionar el temporizador
    override fun timeOut() { timeOut.tryEmit(true) }
    override fun resetTimer() { timeOut.tryEmit(false) }

    // Metodos para gestionar la pregunta lanzada
    override fun addQuestion(question: Question) { _question.value = question }

    // Métodos para gestionar las respuestas de una pregunta
    override fun incAnswer(answerText: String) {
        for (answer in _question.value.answers.value) {
            if (answer.answer?.toString()?.equals(answerText) == true) {
                answer.count.tryEmit(answer.count.value + 1)
            }
        }

    }
    override fun addAnswer(answerText: String) {
        val newAnswersList = _question.value.answers.value + Answer(answerText)
        _question.value.answers.tryEmit(newAnswersList)
    }

}