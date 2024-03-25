package com.pprior.quizz.data.flow

import com.pprior.quizz.data.models.Answer
import com.pprior.quizz.data.models.Question
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow

class FlowRepository {

    private val _answer = MutableSharedFlow<Answer>(replay = 1)
    private var _questionsList = MutableStateFlow<MutableList<Question>>(mutableListOf())

    val answer: Flow<Answer> = _answer.asSharedFlow()
    val questionsList: StateFlow<MutableList<Question>> = _questionsList

    init {
        clearAnswer()
    }

    fun getQuestionsList(): List<Question> = _questionsList.value
    fun exists(question: Question): Boolean = _questionsList.value.any { it.title == question.title }
    fun addQuestion(question: Question) { _questionsList.value.add(question) }

    fun clearAnswer() { _answer.tryEmit(Answer()) }

    fun incYesAnswer() {
        val currentAnswer = _answer.replayCache.firstOrNull() ?: Answer()
        currentAnswer.yesCount++
        _answer.tryEmit(currentAnswer)
    }

    fun incNoAnswer() {
        val currentAnswer = _answer.replayCache.firstOrNull() ?: Answer()
        currentAnswer.noCount++
        _answer.tryEmit(currentAnswer)
    }
}