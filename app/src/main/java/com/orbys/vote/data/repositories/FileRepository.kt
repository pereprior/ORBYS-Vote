package com.orbys.vote.data.repositories

import android.content.Context
import android.os.Environment
import com.orbys.vote.R
import com.orbys.vote.core.extensions.getAnswerType
import com.orbys.vote.domain.models.Question
import com.orbys.vote.domain.repositories.IFileRepository
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

        // Instancia unica para el repositorio.
        fun getInstance(context: Context): FileRepository {
            return INSTANCE ?: synchronized(this) {
                FileRepository(context).also { INSTANCE = it }
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
     * @param answers Lista de respuestas de la pregunta del fichero.
     */
    fun createFile(
        fileName: String,
        question: Question,
        answers: List<String>
    ) {
        val filePath = context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).toString() + "/" + "$fileName.csv"
        file = File(filePath)

        // Si el archivo no existe, se crea y se escribe la leyenda de la pregunta.
        if (!file.exists()) {
            file.createNewFile()
            writeLegend(question, answers)
        }
    }

    /**
     * Escribe una línea en el archivo CSV correspondiente a una respuesta.
     *
     * @param date Fecha de la respuesta.
     * @param time Hora de la respuesta.
     * @param ip Dirección IP del usuario que ha respondido.
     * @param username Nombre del usuario que ha respondido.
     * @param answer Respuesta seleccionada por el usuario.
     */
    fun writeLine(
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

            bufferedWriter.write("${context.getString(R.string.csv_legend_question_type)}${question.getAnswerType()}\n")
            bufferedWriter.write("${context.getString(R.string.csv_legend_question_title)}${question.question}\n")
            bufferedWriter.write("${context.getString(R.string.csv_legend_answers_title)}$answersToCsv\n\n")
            bufferedWriter.write("${context.getString(R.string.csv_legend)}\n")

            bufferedWriter.close()
        }
    }

}