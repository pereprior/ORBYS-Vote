package com.pprior.quizz.ui.activities.dialogs

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.pprior.quizz.core.ENDPOINT
import com.pprior.quizz.core.SERVER_PORT
import com.pprior.quizz.core.URL_ENTRY
import com.pprior.quizz.core.host
import com.pprior.quizz.data.flow.FlowRepository
import com.pprior.quizz.data.server.HttpService
import com.pprior.quizz.databinding.ActivityLaunchQuestionBinding
import com.pprior.quizz.domain.models.Bar
import com.pprior.quizz.domain.models.Question
import com.pprior.quizz.ui.components.utils.QRCodeGenerator
import org.koin.java.KoinJavaComponent.inject
import java.net.URLEncoder

/**
 * Clase que representa una actividad para lanzar una pregunta a ser contestada.
 *
 * @property flowRepository El repositorio para gestionar el flujo de datos.
 * @property lifecycleOwner El ciclo de vida del fragmento al que pertenece esta actividad.
 */
class LaunchQuestionActivity: AppCompatActivity() {

    private lateinit var binding: ActivityLaunchQuestionBinding
    private val repository: FlowRepository by inject(FlowRepository::class.java)
    private lateinit var question: Question

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLaunchQuestionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtiene la pregunta seleccionada.
        question = repository.findQuestionByText(intent.getStringExtra("question") ?: "")

        // Inicia el servicio HTTP.
        startService(Intent(this, HttpService::class.java))

        // Limpia la respuesta en el repositorio y vincula la pregunta a la interfaz de usuario.
        repository.clearAnswer(question)

        bindQuestion(question)
    }

    override fun onDestroy() {
        // Detiene el servicio HTTP.
        stopService(Intent(this, HttpService::class.java))

        repository.clearRespondedUsers()
        super.onDestroy()
    }

    private fun bindQuestion(question: Question) {
        with(binding) {
            questionTypeIcon.setImageResource(question.icon)

            // Establece la imagen del código QR.
            qrCode.setImageBitmap(generateQRCode())

            // Establece el texto de la pregunta.
            this.question.text = question.question

            // Establece el evento de clic en el botón de cerrar.
            closeButton.setOnClickListener { finish() }

            // Lanza una corrutina para recoger los recuentos de respuestas y establecerlos en la interfaz de usuario.
            barView.clearBars()
            question.answers.forEach { answer ->
                Log.d("LaunchQuestionActivity", "Answer: ${answer.answer} - ${answer.count}")
                val bar = Bar(answer.answer.toString(), height = answer.count.toFloat(), color = Color.RED)
                barView.addBar(bar)
            }

        }
    }

    private fun generateQRCode(): Bitmap? {
        val qrCodeGenerator = QRCodeGenerator()
        val encodedQuestion = URLEncoder.encode(question.question, "UTF-8")

        return qrCodeGenerator.encodeAsBitmap(
            url = "$URL_ENTRY$host:$SERVER_PORT$ENDPOINT/$encodedQuestion"
        )
    }

}