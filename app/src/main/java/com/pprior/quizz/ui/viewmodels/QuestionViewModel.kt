package com.pprior.quizz.ui.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.pprior.quizz.domain.models.Question

/**
 * Clase ViewModel para gestionar la lista de preguntas.
 *
 * Contiene una lista de preguntas y proporciona metodos para gestionar dicha lista.
 */
class QuestionViewModel: ViewModel() {

    private var questionsList = MutableLiveData<MutableList<Question>>(mutableListOf())

    fun getQuestionsList(): List<Question> = questionsList.value ?: emptyList()
    fun exists(question: Question): Boolean = questionsList.value?.any { it.title == question.title } ?: false
    fun addQuestion(question: Question) { questionsList.value?.add(question) }
    fun removeQuestion(question: Question) { questionsList.value?.remove(question) }

}