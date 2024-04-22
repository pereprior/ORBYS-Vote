package com.orbys.quizz.data.repositories

import android.content.Context
import android.os.Environment
import com.orbys.quizz.R
import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter

/**
 * Repositorio para manejar las operaciones de archivos.
 *
 * @property context Contexto de la aplicación.
 */
class FileRepository private constructor(
    private val context: Context,
) : IFileRepository {
    companion object {
        @Volatile
        private var INSTANCE: FileRepository? = null

        fun getInstance(context: Context): FileRepository {
            return INSTANCE ?: synchronized(this) {
                FileRepository(context).also { INSTANCE = it }
            }
        }
    }

    private var file = File("")

    fun getFile(): File {
        return file
    }

    override fun createFile(
        fileName: String,
        question: String,
        answers: List<String>
    ) {
        val filePath = context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).toString() + "/" + "$fileName.csv"
        file = File(filePath)

        if (!file.exists()) {
            file.createNewFile()
            writeLegend(question, answers)
        }
    }

    override fun writeFile(
        date: String,
        time: String,
        ip: String,
        username: String,
        answer: String
    ) {
        if (file.exists()) {
            val fileWriter = FileWriter(file, true)
            val bufferedWriter = BufferedWriter(fileWriter)

            val csvData = "$date;$time;$ip;$username;$answer\n"

            bufferedWriter.write(csvData)
            bufferedWriter.close()
        }
    }

    override fun deleteFile() { if (file.exists()) file.delete() }

    private fun writeLegend(question: String, answers: List<String>) {
        if (file.exists()) {
            val fileWriter = FileWriter(file, true)
            val bufferedWriter = BufferedWriter(fileWriter)
            val answersToCsv = answers.joinToString(";")

            bufferedWriter.write("${context.getString(R.string.csv_legend_question_title)}$question\n")
            bufferedWriter.write("${context.getString(R.string.csv_legend_answers_title)}$answersToCsv\n\n")
            bufferedWriter.write("${context.getString(R.string.csv_legend)}\n")

            bufferedWriter.close()
        }
    }

}