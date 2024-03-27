package com.pprior.quizz.ui.activities

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.Window
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.pprior.quizz.R
import com.pprior.quizz.core.ENDPOINT
import com.pprior.quizz.core.SERVER_PORT
import com.pprior.quizz.core.URL_ENTRY
import com.pprior.quizz.core.host
import com.pprior.quizz.databinding.DialogLaunchQuestionBinding
import com.pprior.quizz.data.flow.FlowRepository
import com.pprior.quizz.data.server.HttpService
import com.pprior.quizz.ui.components.utils.QRCodeGenerator
import kotlinx.coroutines.launch

/**
 * Clase que representa un diálogo para lanzar una pregunta a ser contestada.
 *
 * @param question La pregunta a lanzar.
 * @param context El contexto del fragmento al que pertenece este dialogo.
 * @property flowRepository El repositorio para gestionar el flujo de datos.
 * @property lifecycleOwner El ciclo de vida del fragmento al que pertenece este diálogo.
 */
class LaunchQuestionDialog(
    question: String,
    context: Context,
    private val flowRepository: FlowRepository,
    private val lifecycleOwner: LifecycleOwner
): Dialog(context) {

    private var binding: DialogLaunchQuestionBinding

    init {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        binding = DialogLaunchQuestionBinding.inflate(LayoutInflater.from(context))
        setContentView(binding.root)

        // Inicia el servicio HTTP.
        this.context.startService(Intent(this.context, HttpService::class.java))

        // Limpia la respuesta en el repositorio y vincula la pregunta a la interfaz de usuario.
        flowRepository.clearAnswer()
        bindQuestion(question)
    }

    override fun dismiss() {
        // Detiene el servicio HTTP.
        this.context.stopService(Intent(this.context, HttpService::class.java))

        flowRepository.clearRespondedUsers()
        super.dismiss()
    }

    private fun bindQuestion(question: String) {
        with(binding) {
            // Establece la imagen del código QR.
            qrCode.setImageBitmap(generateQRCode())

            // Establece el texto de la pregunta.
            this.question.text = question

            // Lanza una corrutina para recoger los recuentos de respuestas y establecerlos en la interfaz de usuario.
            lifecycleOwner.lifecycleScope.launch {
                flowRepository.answer.collect { answer ->
                    val yesAnswersText = "${context.getString(R.string.yes_aswers)} ${answer.yesCount}"
                    val noAnswersText = "${context.getString(R.string.no_aswers)} ${answer.noCount}"

                    countYes.text = yesAnswersText
                    countNo.text = noAnswersText
                }
            }

            closeButton.setOnClickListener { dismiss() }
        }
    }

    private fun generateQRCode(): Bitmap? {
        val qrCodeGenerator = QRCodeGenerator()

        return qrCodeGenerator.encodeAsBitmap(
            url = "$URL_ENTRY$host:$SERVER_PORT$ENDPOINT"
        )
    }

}