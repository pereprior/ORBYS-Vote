package com.pprior.quizz.domain.models

import com.pprior.quizz.R

// Clase que representa una pregunta.
data class Question(
    var question: String,
    val answerType: AnswerType = AnswerType.NONE,
    val answers: List<Answer> = emptyList(),
    val icon: Int = R.drawable.baseline_help_24
) {
    // Id autonumerico de la pregunta.
    val id: Int = nextId++

    companion object {
        private var nextId = 1
    }
}