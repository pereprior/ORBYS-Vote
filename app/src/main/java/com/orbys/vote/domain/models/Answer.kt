package com.orbys.vote.domain.models

import kotlinx.coroutines.flow.MutableStateFlow

/**
 * Clase que representa una respuesta de una pregunta.
 *
 * @param answer Campo de texto que contiene la respuesta.
 * @param count Flujo mutable que mantiene el recuento de votos que tiene la respuesta.
 */
data class Answer(
    val answer: String,
    var count: MutableStateFlow<Int> = MutableStateFlow(0)
)