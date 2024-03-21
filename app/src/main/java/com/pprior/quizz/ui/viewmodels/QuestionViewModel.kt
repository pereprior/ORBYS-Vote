package com.pprior.quizz.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.pprior.quizz.domain.models.Question

class QuestionViewModel: ViewModel() {

    private var _questionsList = MutableLiveData<MutableList<Question>>(mutableListOf())
    val questionsList: LiveData<MutableList<Question>> = _questionsList

    fun getQuestionsList(): List<Question> {
        return _questionsList.value ?: emptyList()
    }

    fun addQuestion(question: Question) {
        _questionsList.value?.add(question)
    }

    fun removeQuestion(question: Question) {
        _questionsList.value?.remove(question)
    }

    fun exists(question: Question): Boolean {
        return _questionsList.value?.any { it.title == question.title } ?: false
    }

}