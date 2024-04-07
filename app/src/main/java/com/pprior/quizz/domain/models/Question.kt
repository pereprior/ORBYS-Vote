package com.pprior.quizz.domain.models

import com.pprior.quizz.R

// Clase que representa una pregunta.
data class Question(
    var question: String,
    val answerType: AnswerType = AnswerType.NONE,
    val answers: List<Answer> = emptyList(),
    val icon: Int = R.drawable.baseline_help_24
)