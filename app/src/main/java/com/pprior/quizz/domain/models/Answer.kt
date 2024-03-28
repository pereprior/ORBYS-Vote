package com.pprior.quizz.domain.models

// Clase que representa el contador de respuestas de una pregunta.
data class Answer(
    var yesCount: Int = 0,
    var noCount : Int = 0
)