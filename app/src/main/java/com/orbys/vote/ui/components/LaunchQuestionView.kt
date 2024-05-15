package com.orbys.vote.ui.components

import android.content.Context
import android.graphics.PixelFormat
import android.os.Build
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import androidx.constraintlayout.widget.ConstraintLayout
import com.orbys.vote.R
import com.orbys.vote.databinding.ServiceLaunchQuestionBinding

/**
 * Clase que representa una vista para lanzar una pregunta a ser contestada.
 *
 * @property binding Objeto de enlace para acceder a los elementos de la interfaz de usuario.
 * @property windowManager Gestor de ventanas para controlar la vista.
 * @property layoutParams Parámetros de diseño de la ventana.
 * @property x Coordenada x de la vista.
 * @property y Coordenada y de la vista.
 * @property onChangeX Cambio en la coordenada x durante un evento de movimiento.
 * @property onChangeY Cambio en la coordenada y durante un evento de movimiento.
 */
class LaunchQuestionView(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0,
): ConstraintLayout(context, attrs, defStyleAttr), View.OnTouchListener {
    var binding: ServiceLaunchQuestionBinding
        private set
    var windowManager: WindowManager
        private set

    private val layoutParams = WindowManager.LayoutParams(
        context.resources.getDimensionPixelSize(R.dimen.widget_width), WindowManager.LayoutParams.WRAP_CONTENT,
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
        } else {
            WindowManager.LayoutParams.TYPE_PHONE
        },
        WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE, PixelFormat.TRANSLUCENT
    )

    private var x: Int = 0
    private var y: Int = 0
    private var onChangeX: Float = 0f
    private var onChangeY: Float = 0f

    init {
        binding = ServiceLaunchQuestionBinding.inflate(LayoutInflater.from(context))
        windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        addView(binding.root)

        layoutParams.x = x
        layoutParams.y = y

        windowManager.addView(this, layoutParams)
    }

    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        when (event?.action) {

            MotionEvent.ACTION_DOWN -> {
                x = layoutParams.x
                y = layoutParams.y
                onChangeX = event.rawX
                onChangeY = event.rawY
            }

            //Change the position of the widget
            MotionEvent.ACTION_MOVE -> {
                layoutParams.x = (x + event.rawX - onChangeX).toInt()
                layoutParams.y = (y + event.rawY - onChangeY).toInt()
                windowManager.updateViewLayout(this, layoutParams)
            }

        }

        return true
    }

}