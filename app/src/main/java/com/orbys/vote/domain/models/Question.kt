package com.orbys.vote.domain.models

import kotlinx.coroutines.flow.MutableStateFlow

/**
 * Clase que representa la información de una pregunta.
 *
 * @param question Campo de texto que contiene la pregunta.
 * @param answerType Tipo de respuestas que tiene la pregunta.
 * @param answers Flujo mutable que contiene la lista de las respuestas de la pregunta.
 * @param maxNumericAnswer Número límite al que podremos contestar en una pregunta de tipo numérico.
 * @param icon Identificador del recurso de imagen que representa el icono del tipo de respuestas de la pregunta.
 * @param isAnonymous Indica si la pregunta es anónima.
 * @param isMultipleAnswers Indica si la pregunta permite que el usuario pueda contestar varias veces.
 * @param isMultipleChoices Indica si la pregunta admite más de una elección por respuesta.
 * @param timer Tiempo límite para contestar la pregunta (por defecto es nulo).
 */
data class Question(
    var question: String,
    val answerType: AnswerType = AnswerType.NONE,
    var answers: MutableStateFlow<List<Answer>> = MutableStateFlow(emptyList()),
    val maxNumericAnswer: Int = 1,
    val icon: Int = answerType.iconResId,
    val isAnonymous: Boolean = true,
    val isMultipleAnswers: Boolean = false,
    val isMultipleChoices: Boolean = false,
    val timer: Int? = null,
)