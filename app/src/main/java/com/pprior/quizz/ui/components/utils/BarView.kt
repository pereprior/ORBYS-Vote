package com.pprior.quizz.ui.components.utils

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import com.pprior.quizz.domain.models.Bar

// Constantes para el tamaño de la barra
private const val TEXT_SIZE = 30f
private const val BAR_MARGIN = 50f

/**
 * BarView es una clase que extiende de View y se utiliza para dibujar barras de graficos en un Canvas.
 *
 * @param context El contexto en el que se utiliza la vista.
 * @param attributes Los atributos del XML que se han establecido en la vista.
 * @property bars Lista de barras que se van a dibujar en el grafico.
 * @property paint Plantilla que se utiliza para dibujar las barras.
 */
class BarView(
    context: Context,
    attributes: AttributeSet
) : View(context, attributes) {

    private val bars = mutableListOf<Bar>()
    private val paint = Paint().apply {
        textSize = TEXT_SIZE
        color = Color.BLACK
    }

    override fun onDraw(canvas: Canvas) {
        // Margen entre barras
        val barMargin = BAR_MARGIN
        // Calculamos el ancho de cada barra
        val barWidth = (height - barMargin * (bars.size - 1)) / bars.size
        // Obtenemos el valor maximo del contador entre todas las barras del grafico
        val maxCount = bars.maxOf { it.height }

        bars.forEachIndexed { index, bar ->
            // Calculamos las coordenadas en las que se dibuja la barra
            val top = index * (barWidth + barMargin)
            val bottom = top + barWidth

            // Calculamos la longitud de la barra en funcion del contador y el ancho de la vista
            val right = if (maxCount > 0) width.toFloat() * (bar.height / maxCount) else 0f

            // Pintamos la barra del grafico
            paint.color = bar.color
            canvas.drawRect(0f, top, right, bottom, paint)

            // Calculamos la posicion del texto en el centro de la barra
            val x = 5f
            val y = (top + bottom) / 2 - (paint.descent() + paint.ascent()) / 2
            // Pintamos el texto en la barra segun este vacia o llena
            paint.color = if (right == 0f) Color.BLACK else Color.WHITE
            // Pintamos el texto en la barra
            canvas.drawText("${bar.answer}: ${bar.height.toInt()}", x, y, paint)
        }

    }

    // Añade una nueva barra al grafico
    fun addBar(bar: Bar) {
        bars.add(bar)
        invalidate()
    }

}