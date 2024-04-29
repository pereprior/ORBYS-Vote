package com.orbys.quizz.core.extensions

import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.TextView
import android.widget.Toast
import com.orbys.quizz.R

fun Context.showToastWithCustomView(
    message: String,
    duration: Int = Toast.LENGTH_SHORT
) {
    val inflater = this.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    val customToastView = inflater.inflate(R.layout.toast_custom, null)

    val toastText = customToastView.findViewById<TextView>(R.id.custom_toast_text)
    toastText.text = message
    toastText.textSize = 8f

    val toast = Toast(this)
    toast.duration = duration
    toast.setGravity(Gravity.BOTTOM, 0, 200)
    toast.view = customToastView
    toast.show()
}
