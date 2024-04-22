package com.orbys.quizz.data.repositories

interface IFileRepository {
    fun createFile(fileName: String, question: String, answers: List<String>)
    fun writeFile(
        date: String,
        time: String,
        ip: String,
        username: String,
        answer: String
    )
    fun deleteFile()
}