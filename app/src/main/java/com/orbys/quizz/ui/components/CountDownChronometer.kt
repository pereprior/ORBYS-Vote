package com.orbys.quizz.ui.components

import android.content.Context
import android.os.CountDownTimer
import android.os.SystemClock
import android.util.AttributeSet
import android.widget.Chronometer
import com.orbys.quizz.domain.repositories.QuestionRepositoryImpl
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

/**
 * Cronometro que proporciona funcionalidad adicional para contar hacia atrás.
 *
 * @param context El contexto en el que se utiliza el cronómetro.
 * @param attrs Los atributos del cronómetro.
 */
class CountDownChronometer(
    context: Context,
    attrs: AttributeSet? = null
) : Chronometer(context, attrs) {

    private companion object {
        const val TIME_OUT = ": TIME OUT"
        const val COUNT_DOWN_INTERVAL = 1000L
    }

    private val repository = QuestionRepositoryImpl.getInstance()
    private var timeInMillis: Long = 0

    private val _isFinished = MutableStateFlow(false)
    val isFinished: StateFlow<Boolean> get() = _isFinished

    fun setTimeInMillis(timeInMillis: Long) {
        this.timeInMillis = timeInMillis
    }

    fun startCountDown() {
        base = SystemClock.elapsedRealtime() + timeInMillis
        repository.resetTimer()
        startTimer()
    }

    private fun startTimer() = object : CountDownTimer(timeInMillis, COUNT_DOWN_INTERVAL) {
        override fun onTick(millisUntilFinished: Long) {
            base = SystemClock.elapsedRealtime() + millisUntilFinished
        }

        override fun onFinish() {
            stopTimer()
        }
    }.start()

    private fun stopTimer() {
        stop()
        text = TIME_OUT
        repository.timeOut()
        _isFinished.value = true
    }

}