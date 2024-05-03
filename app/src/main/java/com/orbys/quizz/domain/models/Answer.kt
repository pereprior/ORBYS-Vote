package com.orbys.quizz.domain.models

import kotlinx.coroutines.flow.MutableStateFlow

// Clase que representa el contador de respuestas de una pregunta.
data class Answer(
    val answer: String,
    var count: MutableStateFlow<Int> = MutableStateFlow(0)
)