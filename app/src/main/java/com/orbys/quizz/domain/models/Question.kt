package com.orbys.quizz.domain.models

import com.orbys.quizz.R

// Clase que representa una pregunta.
data class Question(
    var question: String,
    val answerType: AnswerType = AnswerType.NONE,
    val answers: List<Answer> = emptyList(),
    val icon: Int = R.drawable.baseline_help_24,
    val isAnonymous: Boolean = true,
    val isMultipleAnswers: Boolean = false,
    val isMultipleChoices: Boolean = false,
    val timeOut: Int? = null,
) {
    // Id autonumerico de la pregunta.
    val id: Int = nextId++

    companion object {
        private var nextId = 0
    }
}