package com.orbys.vote.core.extensions

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.text.InputFilter
import android.view.Gravity
import android.view.LayoutInflater
import android.view.WindowManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.orbys.vote.R
import com.orbys.vote.domain.models.Answer
import com.orbys.vote.domain.models.Question
import com.orbys.vote.ui.components.qr.ImageDialog
import io.ktor.http.ContentType
import io.ktor.server.application.ApplicationCall
import io.ktor.server.application.call
import io.ktor.server.response.respondBytes
import io.ktor.server.response.respondRedirect
import io.ktor.server.response.respondText
import io.ktor.util.pipeline.PipelineContext

/** Devuelve el valor del flow con la lista de respuestas de una pregunta */
fun Question.getAnswers() = answers.value

/** Devuelve el nombre del tipo de las respuestas de una pregunta */
fun Question.getAnswerType() = answerType.name

/** Devuelve la lista de respuestas como una lista de strings */
fun Question.getAnswersAsString() = this.answers.value.map { it.answer }

/** Devuelve el valor del flow con la respuesta seleccionada de una pregunta */
fun Answer.getCount() = count.value

/** Convertir los minutos a milisegundos */
fun Int.minutesToMillis(): Long = this * 60 * 1000L

/**
 * Limita el número de líneas y de caracteres de un EditText.
 *
 * @param maxLines El número máximo de líneas.
 * @param maxCharsForLine El número máximo de caracteres por línea (por defecto es 42).
 */
fun EditText.limitLines(maxLines: Int, maxCharsForLine: Int = 42) {
    this.filters = arrayOf(
        // Limita el numero de caracteres por linea
        InputFilter.LengthFilter(maxLines*maxCharsForLine),
        InputFilter { source, start, end, dest, dstart, _ ->
            for (index in start until end) {
                // Si ya hay tres lineas y se presiona enter, no se añade el salto de linea
                if (source[index] == '\n' && dest.subSequence(0, dstart).count { it == '\n' } >= (maxLines - 1))
                    return@InputFilter ""
            }
            null
        }
    )
}

/**
 * Al pulsar en la imagen, se mostrará un dialogo con la imagen ampliada.
 *
 * @param size El tamaño de la imagen ampliada (por defecto es 1024).
 */
fun ImageView.setExpandOnClick(size: Int = 1024) {
    // Muestra un dialogo con el qr ampliado
    this.setOnClickListener { 
        val dialog = ImageDialog(context, drawable, size)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            dialog.window?.setType(WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY)
        else
            dialog.window?.setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT)
        dialog.show()
    }
}

/**
 * Muestra un mensaje informativo.
 * No se muestra si el dispositivo no tiene permisos de notificación activos o los tiene bloqueados
 *
 * @param message El mensaje que muestra.
 * @param duration El tiempo que dura el mensaje (por defecto el lapso es corto).
 * @param textSize El tamaño del texto del mensaje (por defecto es 8).
 */
@SuppressLint("InflateParams")
fun Context.showToastWithCustomView(
    message: String, duration: Int = Toast.LENGTH_SHORT, textSize: Float = 8f
) {
    val inflater = this.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    val customToastView = inflater.inflate(R.layout.toast_custom, null)

    val toastText = customToastView.findViewById<TextView>(R.id.custom_toast_text)
    toastText.text = message
    toastText.textSize = textSize

    val toast = Toast(this)
    toast.duration = duration
    toast.setGravity(Gravity.BOTTOM, 0, 200)
    toast.view = customToastView
    toast.show()
}

/**
 * Carga un archivo de texto de la carpeta de assets.
 *
 * @param filePath La ruta del archivo.
 * @param contentType El tipo de contenido del archivo (por defecto es CSS).
 */
suspend fun PipelineContext<Unit, ApplicationCall>.loadFile(
    filePath: String, contentType: ContentType = ContentType.Text.CSS
) {
    try {
        val fileContent = this::class.java.classLoader!!.getResource("assets/$filePath")!!.readText()
        call.respondText(fileContent, contentType)
    } catch (e: Exception) {
        call.respondRedirect("/error/1")
    }
}

/**
 * Carga una imagen de la carpeta de assets.
 *
 * @param imagePath La ruta del archivo.
 * @param contentType El tipo de contenido del archivo (por defecto es SVG).
 */
suspend fun PipelineContext<Unit, ApplicationCall>.loadImage(
    imagePath: String, contentType: ContentType = ContentType.Image.SVG
) {
    try {
        // Almacenamos la imagen en la cache del navegador durante 24 horas.
        call.response.headers.append("Cache-Control", "max-age=86400")
        val imageContent = this::class.java.classLoader!!.getResource("assets/images/$imagePath").readBytes()
        call.respondBytes(imageContent, contentType)
    } catch (e: Exception) {
        call.respondRedirect("/error/1")
    }
}

/**
 * Esta función reemplaza el fragmento actual por otro nuevo al pulsar un botón.
 *
 * @param button El botón que al pulsar se reemplaza el fragmento.
 * @param newFragment El nuevo fragmento que se muestra.
 * @param oldFragmentId El id del fragmento que se reemplaza (por defecto es el contenedor de la actividad principal).
 */
fun Fragment.replaceFragmentOnClick(
    button: ImageView, newFragment: Fragment, oldFragmentId: Int = R.id.fragment_container
) {
    button.setOnClickListener {
        parentFragmentManager.beginTransaction().apply {
            replace(oldFragmentId, newFragment)
            addToBackStack(null)
            commit()
        }
    }
}