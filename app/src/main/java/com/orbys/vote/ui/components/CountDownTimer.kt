package com.orbys.vote.ui.components

import android.content.Context
import android.os.CountDownTimer
import android.os.SystemClock
import android.util.AttributeSet
import android.widget.Chronometer
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

/**
 * Vista temporizador con el contador descendente
 *
 * @param context Contexto de la aplicacion
 * @param attrs Atributos de la vista
 */
class CountDownTimer(
    context: Context, attrs: AttributeSet? = null
) : Chronometer(context, attrs) {

    companion object {
        const val TIME_OUT = "TIME OUT"
        const val COUNT_DOWN_INTERVAL = 1000L
        const val TEXT_SIZE = 20f
    }

    private var timeInMillis: Long = 0
    private var timer: CountDownTimer? = null

    private val _isFinished = MutableStateFlow(false)
    val isFinished: StateFlow<Boolean> get() = _isFinished

    init {
        textSize = TEXT_SIZE
        format = "%s"
    }

    fun setTimeInMillis(timeInMillis: Long) { this.timeInMillis = timeInMillis }

    fun startCountDown() {
        base = SystemClock.elapsedRealtime() + timeInMillis
        startTimer()
    }

    private fun startTimer() {
        timer = object : CountDownTimer(timeInMillis, COUNT_DOWN_INTERVAL) {
            override fun onTick(millisUntilFinished: Long) {
                base = SystemClock.elapsedRealtime() + millisUntilFinished
            }

            override fun onFinish() {
                stopTimer()
            }
        }.start()
    }

    fun cancelTimer() {
        // Cancelar el tiempo limite
        timer?.cancel()
        text = TIME_OUT
        timer = null
    }

    private fun stopTimer() {
        // Detener el tiempo limite
        stop()
        text = TIME_OUT
        _isFinished.value = true
    }

}