package com.pprior.quizz.domain.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.pprior.quizz.data.models.Answer
import com.pprior.quizz.data.models.Question

/**
 * Clase ViewModel para gestionar la lista de preguntas.
 *
 * Contiene una lista de preguntas y proporciona metodos para gestionar dicha lista.
 */
class QuestionViewModel: ViewModel() {

    private var questionsList = MutableLiveData<MutableList<Question>>(mutableListOf())
    private var _answer = MutableLiveData(Answer())
    val answer: LiveData<Answer> = _answer

    fun getQuestionsList(): List<Question> = questionsList.value ?: emptyList()
    fun exists(question: Question): Boolean = questionsList.value?.any { it.title == question.title } ?: false
    fun addQuestion(question: Question) { questionsList.value?.add(question) }

    fun getAnswer(): Answer = _answer.value ?: Answer()
    fun clearAnswer() { _answer.value = Answer() }
    fun incYesAnswer() { _answer.value?.yesCount = _answer.value?.yesCount?.plus(1) ?: 1 }
    fun incNoAnswer() { _answer.value?.noCount = _answer.value?.noCount?.plus(1) ?: 1 }

}