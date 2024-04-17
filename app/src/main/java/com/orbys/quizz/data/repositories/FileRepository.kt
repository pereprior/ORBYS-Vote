package com.orbys.quizz.data.repositories

import android.content.Context
import android.os.Environment
import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter

class FileRepository(
    private val context: Context
) : IFileRepository {

    private var file: File = File("")

    fun getFile(): File {
        return file
    }

    override fun createFile(fileName: String) {
        val filePath = context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).toString() + "/" + "$fileName.csv"
        file = File(filePath)

        if (!file.exists()) {
            file.createNewFile()
            writeFile("Date", "Time", "IP Address", "Username", "Question", "Answer")
        }
    }

    override fun writeFile(
        date: String,
        time: String,
        ip: String,
        username: String,
        question: String,
        answer: String
    ) {
        if (file.exists()) {
            val fileWriter = FileWriter(file, true)
            val bufferedWriter = BufferedWriter(fileWriter)

            val csvData = "$date;$time;$ip;$username;$question;$answer\n"

            bufferedWriter.write(csvData)
            bufferedWriter.close()
        }
    }

    override fun readFile(): String {
        if (file.exists()) {
            return file.readText()
        }

        return ""
    }

    override fun deleteFile() {
        if (file.exists()) {
            val fileWriter = FileWriter(file, false)
            val bufferedWriter = BufferedWriter(fileWriter)

            bufferedWriter.write("")
            bufferedWriter.close()
        }
    }

}