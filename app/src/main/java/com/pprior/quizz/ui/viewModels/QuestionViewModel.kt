package com.pprior.quizz.ui.viewModels

import androidx.lifecycle.ViewModel
import com.pprior.quizz.domain.repositories.QuestionRepositoryImpl
import com.pprior.quizz.domain.models.Question
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * ViewModel que proporciona funciones para interactuar con los casos de uso de la lista de preguntas.
 */
@HiltViewModel
class QuestionViewModel @Inject constructor(
    private val repositoryImpl: QuestionRepositoryImpl
): ViewModel() {
    fun getQuestions() = repositoryImpl.questionsList.value
    fun getUpdates() = repositoryImpl.questionUpdated
    fun existsQuestion(question: Question) = repositoryImpl.exists(question)
    fun findQuestion(question: String) = repositoryImpl.findQuestion(question)
    fun addQuestion(question: Question) = repositoryImpl.addQuestion(question)
    fun deleteQuestion(question: Question) = repositoryImpl.deleteQuestion(question)
    fun updateQuestion(oldQuestion: String, newQuestion: Question) = repositoryImpl.updateQuestion(oldQuestion, newQuestion)
    fun clearAnswer(question: Question) = repositoryImpl.clearAnswer(question)
}