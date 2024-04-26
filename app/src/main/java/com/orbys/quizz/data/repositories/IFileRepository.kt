package com.orbys.quizz.data.repositories

import com.orbys.quizz.domain.models.Question
import java.io.File

interface IFileRepository {
    fun getFile(): File
    fun createFile(fileName: String, question: Question, answers: List<String>)
    fun writeLine(
        date: String,
        time: String,
        ip: String,
        username: String,
        answer: String
    )
    fun deleteFile()
}