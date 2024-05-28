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
 * Repositorio que gestiona las operaciones relacionadas al archivo de almacenamiento de datos CSV.
 *
 * @property context Contexto de la aplicación.
 */
class FileRepositoryImpl private constructor(
    private val context: Context,
) : IFileRepository {

    /** Variable que representa el fichero contenedor de los resultados de la votación */
    var resultFile = File("assets")
        private set

    /** Función para eliminar el fichero del dispositivo */
    override fun deleteFile() { if (resultFile.exists()) resultFile.delete() }

    /**
     * Crea un archivo CSV en el dispositivo para almacenar los resultados de la votación.
     *
     * @param fileName Nombre del nuevo archivo.
     * @param question Pregunta a la que corresponde el nuevo fichero.
     */
    fun createFile(question: Question, fileName: String = CSV_FILE_NAME) {
        val filePath = context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).toString() + "/" + "$fileName.csv"
        resultFile = File(filePath)

        // Si el archivo no existe, creamos uno nuevo
        if (!resultFile.exists()) {
            resultFile.createNewFile()
            // Escribimos la leyenda de la pregunta lanzada en las primeras lineas del archivo
            writeLegend(question)
        }
    }

    /**
     * Escribe una nueva línea en el archivo CSV correspondiente a una respuesta de un cliente
     *
     * @param ip Dirección IP del cliente que ha respondido.
     * @param username Nombre de usuario  del cliente que ha respondido.
     * @param answer Respuesta seleccionada por el cliente.
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

        if (resultFile.exists()) {
            val fileWriter = FileWriter(resultFile, true)
            val bufferedWriter = BufferedWriter(fileWriter)

            val csvData = "$date;$time;$ip;$username;$answer\n"

            bufferedWriter.write(csvData)
            bufferedWriter.close()
        }
    }

    /**
     * Reemplaza el contenido de una linea dada dentro del fichero csv
     *
     * @param lineNumber Número de la línea a modificar.
     * @param newContent Nuevo contenido de la línea.
     */
    override suspend fun modifyLineInFile(lineNumber: Int, newContent: String) {
        withContext(Dispatchers.IO) {
            if (resultFile.exists()) {
                val lines = resultFile.readLines().toMutableList()

                if (lineNumber < 0 || lineNumber >= lines.size) return@withContext

                lines[lineNumber] = newContent
                resultFile.writeText(lines.joinToString(separator = "\n"))
            }
        }
    }

    /**
     * Escribe la leyenda de la pregunta en el archivo CSV.
     *
     * @param question Pregunta a la que corresponde el archivo.
     */
    private fun writeLegend(question: Question) {
        if (resultFile.exists()) {
            val fileWriter = FileWriter(resultFile, true)
            val bufferedWriter = BufferedWriter(fileWriter)
            val answersToCsv = question.getAnswersAsString().joinToString(";")
            val modifiedQuestion = question.question.replace("\n", " ")

            bufferedWriter.write("${context.getString(R.string.csv_legend_question_type)}${question.getAnswerType()}\n")
            bufferedWriter.write("${context.getString(R.string.csv_legend_question_title)}$modifiedQuestion\n")
            bufferedWriter.write("${context.getString(R.string.csv_legend_answers_title)}$answersToCsv\n\n")
            bufferedWriter.write("${context.getString(R.string.csv_legend)}\n")

            bufferedWriter.close()
        }
    }

    companion object {
        @SuppressLint("StaticFieldLeak")
        @Volatile
        private var INSTANCE: FileRepositoryImpl? = null
        const val CSV_FILE_NAME = "data"

        /** Instancia única de la clase FileRepositoryImpl */
        fun getInstance(context: Context): FileRepositoryImpl {
            return INSTANCE ?: synchronized(this) {
                FileRepositoryImpl(context).also { INSTANCE = it }
            }
        }
    }

}