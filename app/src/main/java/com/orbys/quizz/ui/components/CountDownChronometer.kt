package com.orbys.quizz.ui.components

import android.content.Context
import android.os.CountDownTimer
import android.os.SystemClock
import android.util.AttributeSet
import android.widget.Chronometer
import com.orbys.quizz.domain.repositories.QuestionRepositoryImpl

class CountDownChronometer : Chronometer {

    private val repository = QuestionRepositoryImpl.getInstance()

    private var timeInMillis: Long = 0
    private var countDownTimer: CountDownTimer? = null

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    fun setTimeInMillis(timeInMillis: Long) {
        this.timeInMillis = timeInMillis
    }

    fun startCountDown() {
        base = SystemClock.elapsedRealtime() + timeInMillis
        repository.resetTimer()

        countDownTimer = object : CountDownTimer(timeInMillis, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                base = SystemClock.elapsedRealtime() + millisUntilFinished
            }

            override fun onFinish() {
                stop()
                text = ": TIME OUT"
                repository.timeOut()
            }
        }.start()
    }

}