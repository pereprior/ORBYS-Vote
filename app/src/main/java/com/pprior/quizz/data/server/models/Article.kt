package com.pprior.quizz.data.server.models

import java.util.concurrent.atomic.AtomicInteger

class Article
private constructor(val id: Int, var title: String, var body: String) {

    val articles = mutableListOf(Article.newEntry(
        "The drive to develop!",
        "...it's what keeps me going."
    ))

    companion object {
        private val idCounter = AtomicInteger()

        fun newEntry(title: String, body: String) = Article(idCounter.getAndIncrement(), title, body)
    }
}