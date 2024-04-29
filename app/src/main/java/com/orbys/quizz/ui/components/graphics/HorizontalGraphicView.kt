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

    companion object {
        private const val TEXT_POSITION = 5f
    }

    private val barWidth = context.resources.getDimensionPixelSize(R.dimen.bar_width)
    private val barMargin = context.resources.getDimensionPixelSize(R.dimen.bar_margin)

    override fun onDraw(canvas: Canvas) {
        // Obtenemos el total de todas las respuestas
        val totalCount = bars.sumOf { it.height }

        bars.forEachIndexed { index, bar ->
            // Calculamos las coordenadas en las que se dibuja la barra
            val top = index * (barWidth + barMargin).toFloat()
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
            val x = TEXT_POSITION
            val y = (top + bottom) / 2 - (paint.descent() + paint.ascent()) / 2
            // Pintamos el texto en la barra segun este vacia o llena
            paint.color = Color.WHITE
            // Pintamos el texto en la barra
            canvas.drawText("${bar.answer}: ${bar.height}", x, y, paint)
        }

        // Actualizar el tamaño de la vista conforme a la cantidad de barras
        layoutParams.height = if (bars.size == 0) 1 else (bars.size * (barWidth + barMargin))
        requestLayout()
    }

}