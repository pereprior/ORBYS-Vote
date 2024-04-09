package com.pprior.quizz.ui.components.graphics

import android.content.Context
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import com.pprior.quizz.domain.models.Bar

// Constantes para el tamaño de la barra
private const val TEXT_SIZE = 30f

/**
 * BarView es una clase que extiende de View y se utiliza para dibujar barras de graficos en un Canvas.
 *
 * @param context El contexto en el que se utiliza la vista.
 * @param attributes Los atributos del XML que se han establecido en la vista.
 * @property bars Lista de barras que se van a dibujar en el grafico.
 * @property paint Plantilla que se utiliza para dibujar las barras.
 */
abstract class BarView(
    context: Context,
    attributes: AttributeSet
) : View(context, attributes) {

    protected val bars = mutableListOf<Bar>()
    protected val paint = Paint().apply {
        textSize = TEXT_SIZE
        color = Color.BLACK
    }

    // Añade una nueva barra al grafico
    fun addBar(bar: Bar) {
        bars.add(bar)
        invalidate()
    }

}