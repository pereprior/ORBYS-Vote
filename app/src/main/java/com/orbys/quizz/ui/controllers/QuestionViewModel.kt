package com.orbys.quizz.ui.controllers

import androidx.lifecycle.ViewModel
import com.orbys.quizz.domain.models.Question
import com.orbys.quizz.domain.usecases.GetServerUrlUseCase
import com.orbys.quizz.domain.usecases.question.AddQuestionUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * ViewModel que proporciona funciones para interactuar con los casos de uso de la lista de preguntas.
 */
@HiltViewModel
class QuestionViewModel @Inject constructor(
    private val addQuestionUseCase: AddQuestionUseCase,
    private val getSeverUrlUseCase: GetServerUrlUseCase
): ViewModel() {
    fun addQuestion(question: Question) { addQuestionUseCase(question) }
    fun getServerUrl(endpoint: String) = getSeverUrlUseCase(endpoint)
}