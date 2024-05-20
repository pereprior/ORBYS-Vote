package com.orbys.vote.ui.components.graphics

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.util.AttributeSet
import androidx.core.content.ContextCompat
import com.orbys.vote.R

/**
 * Vista de grafico horizontal.
 */
class HorizontalGraphicView(
    context: Context,
    attributes: AttributeSet
): GraphicView(context, attributes) {

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

            // Pintamos las barra del grafico
            val gray3 = ContextCompat.getColor(context, R.color.gray3)
            paint.color = gray3
            canvas.drawRoundRect(0f, top, width.toFloat(), bottom, ROUND_RADIUS, ROUND_RADIUS, paint)
            val blueSelected = ContextCompat.getColor(context, R.color.blue_selected_60)
            paint.color = blueSelected
            canvas.drawRoundRect(0f, top, right, bottom, ROUND_RADIUS, ROUND_RADIUS, paint)

            // Calculamos la posicion del texto en el centro de la barra
            var x = TEXT_POSITION
            val y = (top + bottom) / 2 - (paint.descent() + paint.ascent()) / 2
            // Pintamos el texto en la barra segun este vacia o llena
            paint.color = Color.WHITE
            // Pintamos el texto en la barra
            canvas.drawText(bar.answer, x, y, paint)
            x = width - TEXT_POSITION
            canvas.drawText(bar.height.toString(), x, y, paint)
        }

        // Actualizar el tamaño de la vista conforme a la cantidad de barras
        layoutParams.height = bars.size * (barWidth + barMargin)
        requestLayout()
    }

    companion object {
        private const val TEXT_POSITION = 40f
        private const val ROUND_RADIUS = 10f
    }

}