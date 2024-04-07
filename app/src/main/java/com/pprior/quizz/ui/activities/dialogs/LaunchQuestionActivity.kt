package com.pprior.quizz.ui.activities.dialogs

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.pprior.quizz.data.constants.ENDPOINT
import com.pprior.quizz.data.constants.SERVER_PORT
import com.pprior.quizz.data.constants.URL_ENTRY
import com.pprior.quizz.data.constants.host
import com.pprior.quizz.data.services.HttpService
import com.pprior.quizz.databinding.ActivityLaunchQuestionBinding
import com.pprior.quizz.domain.models.Bar
import com.pprior.quizz.domain.models.Question
import com.pprior.quizz.ui.components.utils.QRCodeGenerator
import com.pprior.quizz.ui.viewModels.QuestionViewModel
import com.pprior.quizz.ui.viewModels.UserViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.net.URLEncoder

/**
 * Clase que representa una actividad para lanzar una pregunta a ser contestada.
 *
 * @property questionViewModel ViewModel para gestionar las operaciones relacionadas con las preguntas.
 * @property userViewModel ViewModel para gestionar las operaciones relacionadas con los usuarios.
 * @property binding Objeto de enlace para acceder a los elementos de la interfaz de usuario.
 * @property question La pregunta que se va a lanzar.
 */
@AndroidEntryPoint
class LaunchQuestionActivity: AppCompatActivity() {

    private val questionViewModel by viewModels<QuestionViewModel>()
    private val userViewModel by viewModels<UserViewModel>()

    private lateinit var binding: ActivityLaunchQuestionBinding
    private lateinit var question: Question

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLaunchQuestionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtiene la pregunta seleccionada.
        question = questionViewModel.findQuestion(intent.getStringExtra("question") ?: "")
        questionViewModel.clearAnswer(question)

        // Inicia el servicio HTTP.
        startService(Intent(this, HttpService::class.java))

        lifecycleScope.launch {
            bindQuestion(question)
        }
    }

    private suspend fun bindQuestion(question: Question) {
        with(binding) {
            questionTypeIcon.setImageResource(question.icon)

            // Establece la imagen del código QR.
            qrCode.setImageBitmap(generateQRCode())

            // Establece el texto de la pregunta.
            this.question.text = question.question

            // Establece el evento de clic en el botón de cerrar.
            closeButton.setOnClickListener { finish() }

            // Lanza una corrutina para recoger los recuentos de respuestas y establecerlos en la interfaz de usuario.
            question.answers.forEach { answer ->
                val bar = Bar(answer.answer.toString(), height = answer.count.value.toFloat())
                barView.addBar(bar)

                lifecycleScope.launch {
                    // Recoge los cambios en count para cada respuesta
                    answer.count.collect { newCount ->
                        bar.height = newCount.toFloat()
                        barView.invalidate()
                    }
                }
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

    override fun onDestroy() {
        // Detiene el servicio HTTP.
        stopService(Intent(this, HttpService::class.java))

        userViewModel.clearRespondedUsers()
        super.onDestroy()
    }

}