package com.pprior.quizz.ui.components.dialogs

import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.view.Window
import androidx.viewbinding.ViewBinding

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

    abstract fun getViewBinding(inflater: LayoutInflater): T
}