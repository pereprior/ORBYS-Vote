package com.pprior.quizz.ui.components.dialogs

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.Window
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.pprior.quizz.constants.ENDPOINT
import com.pprior.quizz.constants.SERVER_PORT
import com.pprior.quizz.constants.host
import com.pprior.quizz.databinding.DialogLaunchQuestionBinding
import com.pprior.quizz.data.flow.FlowRepository
import com.pprior.quizz.data.server.HttpService
import com.pprior.quizz.ui.components.QRCodeGenerator
import kotlinx.coroutines.launch

private const val URL_ENTRY = "http://"

/**
 * Clase que representa un diálogo para lanzar una pregunta a ser contestada.
 *
 * @property question La pregunta a lanzar.
 * @property context El contexto del fragmento al que pertenece este dialogo.
 * @property flowRepository El repositorio para gestionar el flujo de datos.
 * @property lifecycleOwner El ciclo de vida del fragmento al que pertenece este diálogo.
 */
class LaunchQuestionDialog(
    question: String,
    context: Context,
    private val flowRepository: FlowRepository,
    private val lifecycleOwner: LifecycleOwner
): Dialog(context) {

    private var _binding: DialogLaunchQuestionBinding? = null
    val binding get() = _binding!!

    private val qrCodeGenerator = QRCodeGenerator()

    init {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        _binding = DialogLaunchQuestionBinding.inflate(LayoutInflater.from(context))
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
            qrCode.setImageBitmap(
                qrCodeGenerator.encodeAsBitmap(url = "$URL_ENTRY$host:$SERVER_PORT$ENDPOINT")
            )

            // Establece el texto de la pregunta.
            this.question.text = question

            // Lanza una corrutina para recoger los recuentos de respuestas y establecerlos en la interfaz de usuario.
            lifecycleOwner.lifecycleScope.launch {
                flowRepository.answer.collect { answer ->
                    countYes.text = "Si: ${answer.yesCount}"
                    countNo.text = "No: ${answer.noCount}"
                }
            }

            closeButton.setOnClickListener { dismiss() }
        }
    }

}