package com.pprior.quizz.data.server.models

data class ResponseBase<T>(
    val status: Int,
    val data: T? = null,
    val message: String? = "Success"
)