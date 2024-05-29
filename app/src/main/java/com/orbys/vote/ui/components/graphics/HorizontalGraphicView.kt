package com.orbys.vote.ui.components.graphics

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import com.orbys.vote.R

/**
 * Clase que extiende de [GraphicView] y se utiliza para dibujar un gráfico de barra en horizontal.
 */
class HorizontalGraphicView(
    context: Context, attributes: AttributeSet
): GraphicView(context, attributes) {
    
    override val barWidth = context.resources.getDimensionPixelSize(R.dimen.bar_size)
    override val barMargin = context.resources.getDimensionPixelSize(R.dimen.small_margin)

    override fun onDraw(canvas: Canvas) {
        // Obtenemos el total de todas las respuestas
        val totalCount = bars.sumOf { it.length }

        bars.forEachIndexed { index, bar ->
            // Calculamos las coordenadas en las que se dibuja la barra
            val top = index * (barWidth + barMargin).toFloat()
            val bottom = top + barWidth

            // Obtenemos el porcentaje de la longitud de la barra en base al total de respuestas
            val countPercent: Float = if(totalCount > 0) (bar.length.toFloat() / totalCount) else 0f
            // Establecemos la longitud total en base al ancho de la vista
            val right = width.toFloat() * countPercent

            // Pintamos la barra que sirve como fondo
            setPaintColor(R.color.gray3)
            canvas.drawRoundRect(0f, top, width.toFloat(), bottom, ROUND_RADIUS, ROUND_RADIUS, paint)
            // Pintamos la barra que representa la respuesta sobre la anterior
            setPaintColor(R.color.blue_selected_60)
            canvas.drawRoundRect(0f, top, right, bottom, ROUND_RADIUS, ROUND_RADIUS, paint)

            // Calculamos las coordenadas en las que se dibuja el texto de la pregunta
            var x = TEXT_POSITION
            val y = (top + bottom) / 2 - (paint.descent() + paint.ascent()) / 2
            // Pintamos la respuesta que representa en el centro de la barra
            setPaintColor(R.color.white)
            canvas.drawText(bar.value, x, y, paint)
            // Pintamos el contador de la respuesta en el otro extremo de la barra
            x = width - TEXT_POSITION
            canvas.drawText(bar.length.toString(), x, y, paint)
        }

        // Actualizar la vista conforme a la cantidad de barras
        layoutParams.height = bars.size * (barWidth + barMargin)
        requestLayout()
    }

    companion object {
        private const val TEXT_POSITION = 40f
        private const val ROUND_RADIUS = 10f
    }

}