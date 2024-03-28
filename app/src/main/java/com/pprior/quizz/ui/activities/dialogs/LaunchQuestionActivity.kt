package com.pprior.quizz.ui.activities.dialogs

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.pprior.quizz.core.ENDPOINT
import com.pprior.quizz.core.SERVER_PORT
import com.pprior.quizz.core.URL_ENTRY
import com.pprior.quizz.core.host
import com.pprior.quizz.data.flow.FlowRepository
import com.pprior.quizz.data.server.HttpService
import com.pprior.quizz.databinding.ActivityLaunchQuestionBinding
import com.pprior.quizz.domain.models.Bar
import com.pprior.quizz.ui.components.utils.QRCodeGenerator
import kotlinx.coroutines.launch
import org.koin.java.KoinJavaComponent.inject

/**
 * Clase que representa una actividad para lanzar una pregunta a ser contestada.
 *
 * @property flowRepository El repositorio para gestionar el flujo de datos.
 * @property lifecycleOwner El ciclo de vida del fragmento al que pertenece esta actividad.
 */
class LaunchQuestionActivity: AppCompatActivity() {

    private lateinit var binding: ActivityLaunchQuestionBinding
    private val repository: FlowRepository by inject(FlowRepository::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLaunchQuestionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Inicia el servicio HTTP.
        startService(Intent(this, HttpService::class.java))

        // Limpia la respuesta en el repositorio y vincula la pregunta a la interfaz de usuario.
        repository.clearAnswer()
        this.lifecycleScope.launch {
            bindQuestion(intent.getStringExtra("question") ?: "")
        }
    }

    override fun onDestroy() {
        // Detiene el servicio HTTP.
        stopService(Intent(this, HttpService::class.java))

        repository.clearRespondedUsers()
        super.onDestroy()
    }

    private suspend fun bindQuestion(question: String) {
        with(binding) {
            // Establece la imagen del código QR.
            qrCode.setImageBitmap(generateQRCode())

            // Establece el texto de la pregunta.
            this.question.text = question

            // Establece el evento de clic en el botón de cerrar.
            closeButton.setOnClickListener { finish() }

            // Lanza una corrutina para recoger los recuentos de respuestas y establecerlos en la interfaz de usuario.
            repository.answer.collect { answer ->
                barView.clearBars()

                val yesBar = Bar("Si", height = answer.yesCount.toFloat(), color = Color.RED)
                val noBar = Bar("No", height = answer.noCount.toFloat(), color = Color.BLUE)

                barView.addBar(yesBar)
                barView.addBar(noBar)
            }

        }
    }

    private fun generateQRCode(): Bitmap? {
        val qrCodeGenerator = QRCodeGenerator()

        return qrCodeGenerator.encodeAsBitmap(
            url = "$URL_ENTRY$host:$SERVER_PORT$ENDPOINT"
        )
    }

}