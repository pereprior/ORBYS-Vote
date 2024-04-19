package com.orbys.quizz.ui.viewmodels

import androidx.lifecycle.ViewModel
import com.orbys.quizz.domain.models.Question
import com.orbys.quizz.domain.usecases.AddQuestionUseCase
import com.orbys.quizz.domain.usecases.ResetTimerUseCase
import com.orbys.quizz.domain.usecases.TimeOutUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * ViewModel que proporciona funciones para interactuar con los casos de uso de la lista de preguntas.
 */
@HiltViewModel
class QuestionViewModel @Inject constructor(
    private val addQuestionUseCase: AddQuestionUseCase,
    private val resetTimerUseCase: ResetTimerUseCase,
    private val timeOutUseCase: TimeOutUseCase
): ViewModel() {
    fun addQuestion(question: Question) { addQuestionUseCase(question) }
    fun resetTimer() { resetTimerUseCase() }
    fun timeOut() { timeOutUseCase() }
}