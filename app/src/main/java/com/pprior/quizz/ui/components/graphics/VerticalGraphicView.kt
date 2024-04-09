package com.pprior.quizz.ui.components.graphics

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.util.AttributeSet

/**
 * Vista de grafico vertical.
 */
class VerticalGraphicView (
    context: Context,
    attributes: AttributeSet
): BarView(context, attributes) {

    override fun onDraw(canvas: Canvas) {
        // Margen entre barras
        val barMargin = 20f
        // Calculamos el ancho de cada barra
        val barWidth = (width - barMargin * (bars.size - 1)) / bars.size
        // Obtenemos el valor maximo del contador entre todas las barras del grafico
        val maxCount = bars.maxOf { it.height }

        bars.forEachIndexed { index, bar ->
            // Calculamos las coordenadas en las que se dibuja la barra
            val left = index * (barWidth + barMargin)
            val right = left + barWidth

            // Calculamos la longitud de la barra en funcion del contador y el alto de la vista
            val bottom = if (maxCount > 0) height.toFloat() * (bar.height / maxCount) else 0f

            // Pintamos la barra del grafico
            paint.color = bar.color
            canvas.drawRect(left, height.toFloat(), right, height - bottom, paint)

            // Calculamos la posicion del texto en el centro de la barra
            val x = (left + right) / 2 - (paint.descent() + paint.ascent()) / 2
            val y = height - 5f
            // Pintamos el texto en la barra segun este vacia o llena
            paint.color = if (bottom == 0f) Color.BLACK else Color.WHITE
            // Pintamos el texto en la barra
            canvas.drawText("${bar.answer}: ${bar.height.toInt()}", x, y, paint)
        }
    }

}