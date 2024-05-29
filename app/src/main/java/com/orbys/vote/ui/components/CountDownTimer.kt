package com.orbys.vote.ui.components

import android.content.Context
import android.os.CountDownTimer
import android.os.SystemClock
import android.util.AttributeSet
import android.widget.Chronometer
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

/**
 * Clase que extiende [Chronometer].
 * Se encarga de mostrar y gestionar un temporizador con contador descendente.
 *
 * @property timer Temporizador.
 * @property timeInMillis Tiempo restante del temporizador en milisegundos.
 * @property _isFinished Flujo mutable que contiene el estado de finalización del temporizador (true si ha finalizado).
 */
class CountDownTimer(
    context: Context, attrs: AttributeSet? = null
) : Chronometer(context, attrs) {

    private var timer: CountDownTimer? = null
    private var timeInMillis: Long = 0

    private val _isFinished = MutableStateFlow(false)
    val isFinished: StateFlow<Boolean> get() = _isFinished

    /** Establece un tiempo limite para el temporizador */
    fun setTimeInMillis(timeInMillis: Long) { this.timeInMillis = timeInMillis }

    /** Inicia el temporizador */
    fun startCountDown() {
        timer = object : CountDownTimer(timeInMillis, COUNT_DOWN_INTERVAL) {
            override fun onTick(millisUntilFinished: Long) {
                base = SystemClock.elapsedRealtime() + millisUntilFinished
                textSize = TEXT_SIZE
            }

            override fun onFinish() {
                stop()
                text = TIME_OUT
                _isFinished.value = true
            }
        }.start()
    }

    /** Detiene el temporizador aunque no se haya terminado el tiempo */
    fun cancelTimer() {
        timer?.cancel()
        timer = null
    }

    companion object {
        const val TIME_OUT = "TIME OUT"
        const val COUNT_DOWN_INTERVAL = 1000L
        const val TEXT_SIZE = 20f
    }

}