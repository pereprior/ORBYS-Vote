package com.orbys.quizz.core.extensions

import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.TextView
import android.widget.Toast
import com.orbys.quizz.R

/**
 * Muestra un mensaje informativo.
 *
 * @param message El mensaje que muestra.
 * @param duration El tiempo que dura el mensaje.
 */
fun Context.showToastWithCustomView(
    message: String,
    duration: Int = Toast.LENGTH_SHORT,
    textSize: Float = 8f
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
