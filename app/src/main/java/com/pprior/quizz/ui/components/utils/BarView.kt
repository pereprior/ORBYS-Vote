package com.pprior.quizz.ui.components.utils

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.View
import com.pprior.quizz.domain.models.Bar

class BarView(
    context: Context,
    attributes: AttributeSet
) : View(context, attributes) {

    private val bars: MutableList<Bar> = mutableListOf()
    private val paint = Paint()

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        drawHorizontalBars(canvas)
    }

    private fun drawHorizontalBars(canvas: Canvas) {
        val barMargin = 50f
        val barHeight = (height - barMargin * (bars.size - 1)) / bars.size

        bars.forEachIndexed { index, bar ->
            val top = index * (barHeight + barMargin)
            val bottom = top + barHeight
            val right = width.toFloat() * (bar.height / 10)

            paint.color = bar.color
            canvas.drawRect(0f, top, right, bottom, paint)

            drawText(canvas, "${bar.answer}: ${bar.height.toInt()}", top, bottom)
        }
    }

    private fun drawText(
        canvas: Canvas,
        text: String,
        barTop: Float,
        barBottom: Float
    ) {
        paint.color = Color.BLACK
        paint.textSize = 30f
        paint.typeface = Typeface.DEFAULT_BOLD

        val x = 5f
        val y = (barTop + barBottom) / 2

        val yOffset = (paint.descent() + paint.ascent()) / 2

        canvas.drawText(text, x, y - yOffset, paint)
    }

    fun addBar(bar: Bar) {
        bars.add(bar)
        invalidate()
    }

    fun clearBars() {
        bars.clear()
        invalidate()
    }

}