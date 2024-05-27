package com.orbys.vote.core.extensions

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.text.InputFilter
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.WindowManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.orbys.vote.R
import com.orbys.vote.domain.models.Answer
import com.orbys.vote.domain.models.Question
import com.orbys.vote.ui.components.qr.ImageDialog

// Devuelve el valor del flow con la lista de respuestas de una pregunta
fun Question.getAnswers() = answers.value

// Devuelve el nombre del tipo de las respuestas de una pregunta
fun Question.getAnswerType() = answerType.name

// Devuelve la lista de respuestas como una lista de strings
fun Question.getAnswersAsString() = this.answers.value.map { it.answer }

// Devuelve el valor del flow con la respuesta seleccionada de una pregunta
fun Answer.getCount() = count.value

// Pasar de minutos a milisegundos
fun Int.minutesToMillis(): Long = this * 60 * 1000L

/**
 * Limita el número de líneas y de caracteres de un EditText.
 *
 * @param maxLines El número máximo de líneas.
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
 * Al pulsar en la imagen, se mostrará un dialogo con esta misma ampliada.
 */
fun ImageView.setExpandOnClick() {
    // Muestra un dialogo con el qr ampliado
    this.setOnClickListener { 
        val dialog = ImageDialog(context, drawable)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            dialog.window?.setType(WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY)
        else
            dialog.window?.setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT)
        dialog.show()
    }
}

/**
 * Muestra un mensaje informativo.
 *
 * @param message El mensaje que muestra.
 * @param duration El tiempo que dura el mensaje.
 */
@SuppressLint("InflateParams")
fun Context.showToastWithCustomView(
    message: String, duration: Int = Toast.LENGTH_SHORT, textSize: Float = 8f
) {
    Log.d("Toast", "VAMOS A EMPEZAR A INFLAR EL TOAST")
    val inflater = this.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    val customToastView = inflater.inflate(R.layout.toast_custom, null)

    Log.d("Toast", "SE VA A PONER LA PLANTILLA DEL TOAST")
    val toastText = customToastView.findViewById<TextView>(R.id.custom_toast_text)
    toastText.text = message
    toastText.textSize = textSize

    Log.d("Toast", "SE VA A CREAR EL TOAST")
    val toast = Toast(this)
    toast.duration = duration
    toast.setGravity(Gravity.BOTTOM, 0, 200)
    toast.view = customToastView
    Log.d("Toast", "SE VA A MOSTRAR EL TOAST")
    toast.show()
    Log.d("Toast", "SE HA MOSTRADO EL TOAST")
}