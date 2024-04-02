package com.pprior.quizz.domain.models

// Clase que representa el contador de respuestas de una pregunta.
data class Answer(
    val answer: Any? = null,
    var count: Int = 0
)