package com.orbys.vote.ui.components.graphics

import android.content.Context
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import com.orbys.vote.R

/**
 * Clase que extiende de [View] y se utiliza para dibujar un gráfico de barras en la vista.
 *
 * @property bars Lista que contiene la información de cada barra a representar en el gráfico.
 * @property paint Plantilla que se utiliza para dibujar las barras.
 * @property barWidth Variable abstracta para indicar el ancho fijo de cada barra del gráfico.
 * @property barMargin Variable abstracta para indicar el margen fijo entre cada barra del gráfico.
 */
abstract class GraphicView(
    context: Context, attributes: AttributeSet
): View(context, attributes) {

    val bars = mutableListOf<Bar>()
    val paint = Paint().apply { textSize = context.resources.getDimensionPixelSize(R.dimen.font_size).toFloat() }

    abstract val barWidth: Int
    abstract val barMargin: Int

    /** Añade una nueva barra a la lista */
    fun addBar(bar: Bar) {
        bars.add(bar)
        invalidate()
    }

    /** Elimina todas las barras de la lista */
    fun clearBars() {
        bars.clear()
        invalidate()
    }

    protected fun setPaintColor(color: Int) {
        val resColor = context.getColor(R.color.gray3)
        paint.color = resColor
    }

    /** 
     * Clase que representa una barra del gráfico 
     * 
     * @property value Texto que contiene la respuesta a la que representa la barra.
     * @property length Contador que representa la longitud que debe tener la barra.
     * @property color Color de la barra (por defecto blanco).
     */
    data class Bar(
        val value: String,
        var length: Int = 0,
        var color: Int = Color.WHITE
    )

}