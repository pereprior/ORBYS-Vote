package com.pprior.quizz.domain.models

import android.graphics.Color

// Colores de las barras del grafico
private val barColors = listOf(
    Color.parseColor("#8B0000"),
    Color.parseColor("#006400"),
    Color.parseColor("#00008B"),
    Color.parseColor("#B8860B"),
    Color.parseColor("#4B0082")
)

// Clase que representa una barra del grafico.
data class Bar(
    val answer: String,
    var height: Int = 0,
    var color: Int = barColors.random()
)