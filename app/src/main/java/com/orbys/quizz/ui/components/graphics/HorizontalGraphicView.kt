package com.orbys.quizz.ui.components.graphics

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.util.AttributeSet
import androidx.core.content.ContextCompat
import com.orbys.quizz.R

/**
 * Vista de grafico horizontal.
 */
class HorizontalGraphicView(
    context: Context,
    attributes: AttributeSet
): BarView(context, attributes) {

    override fun onDraw(canvas: Canvas) {
        // Margen entre barras
        val barMargin = 40f / bars.size
        // Calculamos el ancho de cada barra
        val barWidth = (height - barMargin * (bars.size - 1)) / bars.size
        // Obtenemos el total de todas las respuestas
        val totalCount = bars.sumOf { it.height }

        bars.forEachIndexed { index, bar ->
            // Calculamos las coordenadas en las que se dibuja la barra
            val top = index * (barWidth + barMargin)
            val bottom = top + barWidth

            // Porcentaje del contador de esta barra en funcion del total
            val countPercent: Float = if(totalCount > 0) (bar.height.toFloat() / totalCount) else 0f
            // Calculamos la longitud de la barra en funcion del contador y el ancho de la vista
            val right = width.toFloat() * countPercent

            // Pintamos la barra del grafico
            val blueSelected = ContextCompat.getColor(context, R.color.blue_selected)
            paint.color = blueSelected
            canvas.drawRect(0f, top, right, bottom, paint)

            // Calculamos la posicion del texto en el centro de la barra
            val x = 5f
            val y = (top + bottom) / 2 - (paint.descent() + paint.ascent()) / 2
            // Pintamos el texto en la barra segun este vacia o llena
            paint.color = if (right == 0f) Color.WHITE else Color.BLACK
            // Pintamos el texto en la barra
            canvas.drawText("${bar.answer}: ${bar.height}", x, y, paint)
        }

    }

}