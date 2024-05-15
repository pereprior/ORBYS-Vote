package com.orbys.vote.domain.models

// Clase que representa un usuario que contesta una pregunta
data class User(
    val ip: String,
    val username: String = "Anonymous",
    var responded: Boolean = false
)