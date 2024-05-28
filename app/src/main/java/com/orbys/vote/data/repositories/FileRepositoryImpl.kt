package com.orbys.vote.data.repositories

import android.annotation.SuppressLint
import android.content.Context
import android.os.Environment
import com.orbys.vote.R
import com.orbys.vote.core.extensions.getAnswerType
import com.orbys.vote.core.extensions.getAnswersAsString
import com.orbys.vote.domain.models.Question
import com.orbys.vote.domain.repositories.IFileRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

/**
 * Repositorio para manejar las operaciones de archivos.
 *
 * @property context Contexto de la aplicación.
 */
class FileRepositoryImpl private constructor(
    private val context: Context,
) : IFileRepository {
    companion object {
        @SuppressLint("StaticFieldLeak")
        @Volatile
        private var INSTANCE: FileRepositoryImpl? = null
        const val CSV_FILE_NAME = "data"

        // Instancia unica para el repositorio.
        fun getInstance(context: Context): FileRepositoryImpl {
            return INSTANCE ?: synchronized(this) {
                FileRepositoryImpl(context).also { INSTANCE = it }
            }
        }
    }

    private var file = File("")
    override fun getFile() = file
    override fun deleteFile() { if (file.exists()) file.delete() }

    /**
     * Crea un archivo CSV y escribe la leyenda de la pregunta.
     *
     * @param fileName Nombre del nuevo archivo.
     * @param question Pregunta a la que corresponde el nuevo fichero.
     */
    fun createFile(question: Question, fileName: String = CSV_FILE_NAME) {
        val filePath = context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).toString() + "/" + "$fileName.csv"
        file = File(filePath)

        // Si el archivo no existe, se crea y se escribe la leyenda de la pregunta.
        if (!file.exists()) {
            file.createNewFile()
            writeLegend(question, question.getAnswersAsString())
        }
    }

    /**
     * Escribe una línea en el archivo CSV correspondiente a una respuesta.
     *
     * @param ip Dirección IP del usuario que ha respondido.
     * @param username Nombre del usuario que ha respondido.
     * @param answer Respuesta seleccionada por el usuario.
     */
    fun writeLine(
        ip: String,
        username: String,
        answer: String
    ) {
        val dateFormatter = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
        val timeFormatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val date = dateFormatter.format(Calendar.getInstance().time)
        val time = timeFormatter.format(Calendar.getInstance().time)

        if (file.exists()) {
            val fileWriter = FileWriter(file, true)
            val bufferedWriter = BufferedWriter(fileWriter)

            val csvData = "$date;$time;$ip;$username;$answer\n"

            bufferedWriter.write(csvData)
            bufferedWriter.close()
        }
    }

    /**
     * Modifica una línea en el archivo CSV.
     *
     * @param lineNumber Número de la línea a modificar.
     * @param newContent Nuevo contenido de la línea.
     */
    override suspend fun modifyLineInFile(lineNumber: Int, newContent: String) {
        withContext(Dispatchers.IO) {
            if (file.exists()) {
                val lines = file.readLines().toMutableList()

                if (lineNumber < 0 || lineNumber >= lines.size) return@withContext

                lines[lineNumber] = newContent
                file.writeText(lines.joinToString(separator = "\n"))
            }
        }
    }

    /**
     * Escribe la leyenda de la pregunta en el archivo CSV.
     *
     * @param question Pregunta a la que corresponde el archivo.
     * @param answers Lista de respuestas de la pregunta.
     */
    private fun writeLegend(question: Question, answers: List<String>) {
        if (file.exists()) {
            val fileWriter = FileWriter(file, true)
            val bufferedWriter = BufferedWriter(fileWriter)
            val answersToCsv = answers.joinToString(";")
            val modifiedQuestion = question.question.replace("\n", " ")

            bufferedWriter.write("${context.getString(R.string.csv_legend_question_type)}${question.getAnswerType()}\n")
            bufferedWriter.write("${context.getString(R.string.csv_legend_question_title)}$modifiedQuestion\n")
            bufferedWriter.write("${context.getString(R.string.csv_legend_answers_title)}$answersToCsv\n\n")
            bufferedWriter.write("${context.getString(R.string.csv_legend)}\n")

            bufferedWriter.close()
        }
    }

}