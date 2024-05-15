package com.orbys.vote.domain.models

import kotlinx.coroutines.flow.MutableStateFlow

// Clase que representa una pregunta.
data class Question(
    var question: String,
    val answerType: AnswerType = AnswerType.NONE,
    var answers: MutableStateFlow<List<Answer>> = MutableStateFlow(emptyList()),
    val maxNumericAnswer: Int = 1,
    val icon: Int = answerType.iconResId,
    val isAnonymous: Boolean = true,
    val isMultipleAnswers: Boolean = false,
    val isMultipleChoices: Boolean = false,
    val timeOut: Int? = null,
)