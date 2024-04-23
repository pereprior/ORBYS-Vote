package com.orbys.quizz.data.repositories

import java.io.File

interface IFileRepository {
    fun getFile(): File
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