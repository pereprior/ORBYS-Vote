package com.orbys.vote.ui.components.graphics

import android.content.Context
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import com.orbys.vote.R
import com.orbys.vote.domain.models.Bar

/**
 * BarView es una clase que extiende de View y se utiliza para dibujar barras de graficos en un Canvas.
 *
 * @param context El contexto en el que se utiliza la vista.
 * @param attributes Los atributos del XML que se han establecido en la vista.
 * @property bars Lista de barras que se van a dibujar en el grafico.
 * @property paint Plantilla que se utiliza para dibujar las barras.
 */
abstract class GraphicView(
    context: Context,
    attributes: AttributeSet
) : View(context, attributes) {

    protected val barWidth = context.resources.getDimensionPixelSize(R.dimen.bar_size)
    protected val barMargin = context.resources.getDimensionPixelSize(R.dimen.small_margin)

    val bars = mutableListOf<Bar>()
    protected val paint = Paint().apply {
        textSize = context.resources.getDimensionPixelSize(R.dimen.font_size).toFloat()
    }

    // Añade una nueva barra al grafico
    fun addBar(bar: Bar) {
        bars.add(bar)
        invalidate()
    }

    // Limpia todas las barras del grafico
    fun clearBars() {
        bars.clear()
        invalidate()
    }

}