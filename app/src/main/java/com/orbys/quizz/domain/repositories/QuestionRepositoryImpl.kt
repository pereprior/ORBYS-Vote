package com.orbys.quizz.domain.repositories

import com.orbys.quizz.domain.models.Question
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

/**
 * Clase que gestiona el flujo de datos relacionados con las preguntas y respuestas.
 *
 * @property _question Un flujo mutable privado que contiene una lista de preguntas.
 * @property _questionUpdated Un flujo mutable privado que emite una respuesta cada vez que se actualiza una pregunta.
 * @property question Un flujo inmutable y publico para llamar a la lista de preguntas.
 * @property questionUpdated Un flujo inmutable y publico para llamar a la respuesta emitida cada vez que se actualiza una pregunta.
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

    // Flujo de preguntas
    private var _question = MutableStateFlow(Question(""))
    private val _questionUpdated = MutableSharedFlow<Unit>(replay = 1)

    val question: StateFlow<Question> = _question
    val questionUpdated: SharedFlow<Unit> = _questionUpdated

    // Metodos para buscar preguntas dentro de la lista
    /*override fun exists(question: Question) = _questionsList.value.any { it.question == question.question }
    override fun findQuestion(question: String) = _questionsList.value.find { it.question == question } ?: Question("")
    override fun findQuestion(questionID: Int) = _questionsList.value.find { it.id == questionID } ?: Question("")
*/
    // Metodos para gestionar las preguntas dentro de la lista
    override fun addQuestion(question: Question) {
        _question.value = question
        _questionUpdated.tryEmit(Unit)
    }
    /*override fun deleteQuestion(question: Question) {
        _question.value.remove(question)
        _questionUpdated.tryEmit(Unit)
    }
    fun updateQuestion(oldQuestion: String, newQuestion: Question) {
        val index = _question.value.indexOfFirst { it.question == oldQuestion }

        // Si la pregunta existe, actualizamos la pregunta
        if (index != -1) {
            _question.value[index].question = newQuestion.question
            _questionUpdated.tryEmit(Unit)
        }
    }*/

    // Métodos para gestionar el contador de respuestas de una pregunta
    override fun clearAnswer(question: Question) {
        _question.value.answers.forEach { it.count.tryEmit(0) }
    }
    override fun incAnswer(answer: String) {
        for (ans in _question.value.answers) {
            if (ans.answer?.toString()?.equals(answer) == true) {
                ans.count.tryEmit(ans.count.value + 1)
            }
        }

    }

}