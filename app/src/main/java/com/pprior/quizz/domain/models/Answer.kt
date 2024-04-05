package com.pprior.quizz.domain.models

import kotlinx.coroutines.flow.MutableStateFlow

// Clase que representa el contador de respuestas de una pregunta.
data class Answer(
    val answer: Any? = null,
    var count: MutableStateFlow<Int> = MutableStateFlow(0)
)