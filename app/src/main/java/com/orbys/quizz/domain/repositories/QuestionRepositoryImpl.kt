package com.orbys.quizz.domain.repositories

import com.orbys.quizz.core.extensions.getAnswers
import com.orbys.quizz.core.extensions.getCount
import com.orbys.quizz.domain.models.Answer
import com.orbys.quizz.domain.models.Question
import kotlinx.coroutines.flow.MutableStateFlow

/**
 * Clase que gestiona el flujo de datos relacionados con las preguntas y respuestas.
 *
 * @property question Un flujo mutable privado la información de la pregunta.
 * @property timerState Un flujo mutable para gestionar el tiempo limite de la pregunta.
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
    private val timerState = MutableStateFlow(false)

    // Metodos para gestionar el temporizador
    override fun getTimerState(): MutableStateFlow<Boolean> = timerState
    override fun timeOut() { timerState.tryEmit(true) }
    override fun resetTimer() { timerState.tryEmit(false) }

    // Metodos para gestionar la pregunta lanzada
    override fun getQuestion(): Question = question.value
    override fun addQuestion(question: Question) { this.question.value = question }

    // Métodos para gestionar las respuestas de una pregunta
    override fun addAnswer(answerText: String) {
        val newAnswersList = question.value.getAnswers() + Answer(answerText)
        question.value.answers.tryEmit(newAnswersList)
    }
    override fun incAnswer(answerText: String) {
        for (answer in question.value.getAnswers()) {
            if (answer.answer == answerText) {
                answer.count.tryEmit(answer.getCount() + 1)
            }
        }
    }

}