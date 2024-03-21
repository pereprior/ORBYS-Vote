package com.pprior.quizz.ui.components.dialogs

import android.content.Context
import android.view.LayoutInflater
import com.pprior.quizz.databinding.DialogLaunchQuestionBinding

class LaunchQuestionDialog(
    context: Context
): QuestionDialog<DialogLaunchQuestionBinding>(context) {

    init {
        binding.closeButton.setOnClickListener {
            dismiss()
        }
    }

    override fun getViewBinding(inflater: LayoutInflater): DialogLaunchQuestionBinding {
        return DialogLaunchQuestionBinding.inflate(inflater)
    }
}