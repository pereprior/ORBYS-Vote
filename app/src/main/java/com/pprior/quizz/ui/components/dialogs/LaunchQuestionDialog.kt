package com.pprior.quizz.ui.components.dialogs

import android.content.Context
import android.view.LayoutInflater
import com.pprior.quizz.data.constants.ENDPOINT
import com.pprior.quizz.data.constants.SERVER_PORT
import com.pprior.quizz.data.constants.host
import com.pprior.quizz.databinding.DialogLaunchQuestionBinding
import com.pprior.quizz.ui.components.QRCodeGenerator

/**
 * Clase que representa un dialogo de una pregunta seleccionada para contestar.
 *
 * @param question La pregunta seleccionada.
 * @param context El contexto del dialogo.
 */
class LaunchQuestionDialog(
    question: String,
    context: Context
): QuestionDialog<DialogLaunchQuestionBinding>(context) {

    // Generador de códigos QR.
    private val qrCodeGenerator = QRCodeGenerator()

    init {
        with(binding) {
            // Genera el código QR con la URL de la pregunta.
            qrCode.setImageBitmap(
                qrCodeGenerator.encodeAsBitmap(
                    url = "${host}:$SERVER_PORT$ENDPOINT",
                    width = 200,
                    height = 200
                )
            )

            this.question.text = question
            closeButton.setOnClickListener { dismiss() }
        }
    }

    override fun getViewBinding(inflater: LayoutInflater) = DialogLaunchQuestionBinding.inflate(inflater)
}