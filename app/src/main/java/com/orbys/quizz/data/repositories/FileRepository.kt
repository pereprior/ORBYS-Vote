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

        // Instancia del repositorio
        fun getInstance(context: Context): FileRepository {
            return INSTANCE ?: synchronized(this) {
                FileRepository(context).also { INSTANCE = it }
            }
        }
    }

    private var file = File("")
    override fun getFile() = file

    override fun createFile(
        fileName: String,
        question: String,
        answers: List<String>
    ) {
        // Crear el archivo para almacenar los datos del quizz
        val filePath = "${context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)}/$fileName.csv"
        file = File(filePath).apply {

            if (!exists()) {
                createNewFile()
                appendToFile { bufferedWriter ->
                    val answersToCsv = answers.joinToString(";")

                    // Escribir la cabecera del archivo
                    bufferedWriter.write("${context.getString(R.string.csv_legend_question_title)}$question\n")
                    bufferedWriter.write("${context.getString(R.string.csv_legend_answers_title)}$answersToCsv\n\n")
                    bufferedWriter.write("${context.getString(R.string.csv_legend)}\n")
                }
            }
        }
    }

    override fun writeFile(
        date: String,
        time: String,
        ip: String,
        username: String,
        answer: String
    ) {
        // Si el fichero existe, añadir la respuesta al archivo
        if (file.exists()) {
            appendToFile { bufferedWriter ->
                val csvData = "$date;$time;$ip;$username;$answer\n"
                bufferedWriter.write(csvData)
            }
        }
    }

    override fun deleteFile() { if (file.exists()) file.delete() }

    private fun appendToFile(writeAction: (BufferedWriter) -> Unit) {
        // Añade una nueva linea al archivo
        FileWriter(file, true).buffered().use { bufferedWriter ->
            writeAction(bufferedWriter)
        }
    }

}