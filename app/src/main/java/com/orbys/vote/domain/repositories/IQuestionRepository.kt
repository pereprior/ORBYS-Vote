package com.orbys.vote.domain.repositories

import com.orbys.vote.domain.models.Question

interface IQuestionRepository {
    fun getQuestion(): Question
    fun getTimerState(): Boolean
    fun generateQuestion(question: Question)
    fun setTimeOutAs(isTimeOut: Boolean = true)
}