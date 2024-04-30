package com.orbys.quizz.ui.view.widgets

import android.content.Context
import android.content.Intent
import android.graphics.PixelFormat
import android.os.Build
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import com.orbys.quizz.R
import com.orbys.quizz.core.extensions.setChronometerCount
import com.orbys.quizz.core.extensions.setDownloadButtonClickable
import com.orbys.quizz.core.extensions.setGraphicAnswersCount
import com.orbys.quizz.core.extensions.setUsersCount
import com.orbys.quizz.core.extensions.showToastWithCustomView
import com.orbys.quizz.data.utils.ServerUtils
import com.orbys.quizz.data.utils.ServerUtils.Companion.QUESTION_ENDPOINT
import com.orbys.quizz.databinding.ServiceLaunchQuestionBinding
import com.orbys.quizz.domain.repositories.QuestionRepositoryImpl
import com.orbys.quizz.domain.repositories.UsersRepositoryImpl
import com.orbys.quizz.ui.components.QRCodeGenerator
import com.orbys.quizz.ui.view.MainActivity

/**
 * Clase que representa una vista para lanzar una pregunta a ser contestada.
 *
 * @property binding Objeto de enlace para acceder a los elementos de la interfaz de usuario.
 * @property windowManager Gestor de ventanas para controlar la vista.
 * @property layoutParams Parámetros de diseño de la ventana.
 * @property questionRepository Repositorio para gestionar las operaciones relacionadas con las preguntas.
 * @property usersRepository Repositorio para gestionar las operaciones relacionadas con los usuarios.
 * @property x Coordenada x de la vista.
 * @property y Coordenada y de la vista.
 * @property onChangeX Cambio en la coordenada x durante un evento de movimiento.
 * @property onChangeY Cambio en la coordenada y durante un evento de movimiento.
 */
class LaunchQuestionView(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
): ConstraintLayout(context, attrs, defStyleAttr), View.OnTouchListener {

    private var binding: ServiceLaunchQuestionBinding
    private var windowManager: WindowManager
    private val layoutParams = WindowManager.LayoutParams(
        context.resources.getDimensionPixelSize(R.dimen.widget_width),
        //WindowManager.LayoutParams.WRAP_CONTENT,
        WindowManager.LayoutParams.WRAP_CONTENT,
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
        } else {
            WindowManager.LayoutParams.TYPE_PHONE
        },
        WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
        PixelFormat.TRANSLUCENT
    )

    private val questionRepository = QuestionRepositoryImpl.getInstance()
    private val usersRepository = UsersRepositoryImpl.getInstance()
    private val serverUtils = ServerUtils()

    private var x: Int = 0
    private var y: Int = 0
    private var onChangeX: Float = 0f
    private var onChangeY: Float = 0f

    init {
        binding = ServiceLaunchQuestionBinding.inflate(LayoutInflater.from(context))
        windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager

        addView(binding.root)

        printQrCode()
        bindOnQuestion()

        setOnTouchListener(this)

        layoutParams.x = x
        layoutParams.y = y

        windowManager.addView(this, layoutParams)
        questionRepository.resetTimer()
    }

    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        when (event?.action) {

            MotionEvent.ACTION_DOWN -> {
                x = layoutParams.x
                y = layoutParams.y
                onChangeX = event.rawX
                onChangeY = event.rawY
            }

            //Change the position of the widget
            MotionEvent.ACTION_MOVE -> {
                layoutParams.x = (x + event.rawX - onChangeX).toInt()
                layoutParams.y = (y + event.rawY - onChangeY).toInt()
                windowManager.updateViewLayout(this, layoutParams)
            }

        }

        return true
    }

    private fun printQrCode() {
        val  url = serverUtils.getServerUrl("$QUESTION_ENDPOINT/${questionRepository.question.value.id}")

        val qrCode: ImageView = findViewById(R.id.qrCode)
        val qrUrl: TextView = findViewById(R.id.qrUrl)

        qrCode.setImageBitmap(QRCodeGenerator(context).encodeAsBitmap(url))
        qrUrl.text = url
    }

    private fun bindOnQuestion() {
        val question = questionRepository.question.value

        with(binding) {
            // Establece los elementos relacionados con la pregunta
            questionTypeIcon.setImageResource(question.icon)
            this.question.text = question.question

            // Establece las acciones de los botones
            closeButton.setOnClickListener {
                windowManager.removeView(this@LaunchQuestionView)
                usersRepository.clearRespondedUsers()

                val intent = Intent(context, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                context.startActivity(intent)
            }

            // Gestión del cronómetro
            if(question.timeOut!! > 0) {
                setChronometerCount(question.timeOut, context, windowManager, this@LaunchQuestionView,)
            }
            timeOutButton.setOnClickListener {
                setDownloadButtonClickable(context, windowManager, this@LaunchQuestionView, usersRepository)
                questionRepository.timeOut()
                chronometer.cancelTimer()
                context.showToastWithCustomView(context.getString(R.string.time_up_message), Toast.LENGTH_SHORT)
            }

            setUsersCount(context, usersRepository.respondedUsers)
            setGraphicAnswersCount(question)
        }

    }

}