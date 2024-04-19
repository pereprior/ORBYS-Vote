package com.orbys.quizz.domain.models

data class User(
    val ip: String,
    val username: String = "Anonymous",
    var responded: Boolean = false
)