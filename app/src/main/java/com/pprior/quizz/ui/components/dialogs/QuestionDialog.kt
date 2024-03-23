package com.pprior.quizz.ui.components.dialogs

import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.view.Window
import androidx.viewbinding.ViewBinding

/**
 * Clase abstracta que representa un dialogo de gestión de las preguntas.
 *
 * @param T La vista del dialogo que herede de esta clase.
 * @property context El contexto del dialogo.
 */
abstract class QuestionDialog<T: ViewBinding>(
    context: Context
) : Dialog(context) {
    private var _binding: T? = null
    val binding get() = _binding!!

    init {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        _binding = getViewBinding(LayoutInflater.from(context))
        setContentView(binding.root)
    }

    /**
     * Método abstracto que devuelve la vista del dialogo.
     *
     * @param inflater El inflater del dialogo.
     * @return La vista del dialogo.
     */
    abstract fun getViewBinding(inflater: LayoutInflater): T
}