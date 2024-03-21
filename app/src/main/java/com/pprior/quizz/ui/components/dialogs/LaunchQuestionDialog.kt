package com.pprior.quizz.ui.components.dialogs

import android.content.Context
import android.view.LayoutInflater
import com.pprior.quizz.databinding.DialogLaunchQuestionBinding
import com.pprior.quizz.ui.components.qr.QRCodeGenerator

class LaunchQuestionDialog(
    question: String,
    context: Context
): QuestionDialog<DialogLaunchQuestionBinding>(context) {

    private val qrCodeGenerator = QRCodeGenerator()

    init {
        val qrCodeBitmap = qrCodeGenerator.encodeAsBitmap(
            url = "http://google.com",
            width = 200,
            height = 200
        )

        binding.qrCode.setImageBitmap(qrCodeBitmap)
        binding.question.text = question

        binding.closeButton.setOnClickListener {
            dismiss()
        }
    }

    override fun getViewBinding(inflater: LayoutInflater): DialogLaunchQuestionBinding {
        return DialogLaunchQuestionBinding.inflate(inflater)
    }


}