package com.pprior.quizz.ui.viewmodels

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
    private var answer = MutableLiveData(Answer())

    fun getQuestionsList(): List<Question> = questionsList.value ?: emptyList()
    fun exists(question: Question): Boolean = questionsList.value?.any { it.title == question.title } ?: false
    fun addQuestion(question: Question) { questionsList.value?.add(question) }

    fun getAnswer(): Answer = answer.value ?: Answer()
    fun setYesAnswer() {
        answer.value!!.yesCount += 1
    }

    fun setNoAnswer() { answer.value?.noCount = answer.value?.noCount?.plus(1) ?: 1 }

}