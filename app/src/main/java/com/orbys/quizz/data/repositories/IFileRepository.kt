package com.orbys.quizz.data.repositories

interface IFileRepository {
    fun createFile(fileName: String)
    fun writeFile(
        date: String,
        time: String,
        ip: String,
        username: String,
        question: String,
        answer: String
    )
}