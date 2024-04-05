package com.pprior.quizz.data.server.repositories

import android.util.Log
import com.pprior.quizz.data.flow.FlowRepository
import com.pprior.quizz.domain.models.Question
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.java.KoinJavaComponent.inject

/**
 * Implementación del repositorio de preguntas.
 *
 * Esta clase se encarga de gestionar las operaciones relacionadas con las respuestas.
 */
class QuestionRepositoryImp: IQuestionRepository {

    private val repository: FlowRepository by inject(FlowRepository::class.java)
    private val scope = CoroutineScope(Dispatchers.IO)

    override suspend fun setPostInAnswerCount(question: Question, answer: String?) {
        scope.launch {
            repository.incAnswer(question, answer ?: "")
        }
    }
    override fun findQuestion(question: String) = repository.findQuestionByText(question)
    override fun addUserToRespondedList(userIp: String) = repository.addRespondedUser(userIp)

}