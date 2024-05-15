package com.orbys.vote.domain.models

import android.graphics.Color

// Clase que representa una barra del grafico.
data class Bar(
    val answer: String,
    var height: Int = 0,
    var color: Int = Color.WHITE
)