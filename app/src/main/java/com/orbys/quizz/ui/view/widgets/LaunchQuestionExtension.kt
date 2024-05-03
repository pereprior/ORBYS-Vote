package com.orbys.quizz.ui.view.widgets

import android.content.Context
import android.content.Intent
import android.view.View
import android.view.WindowManager
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.orbys.quizz.R
import com.orbys.quizz.core.extensions.getCount
import com.orbys.quizz.core.extensions.minutesToSeconds
import com.orbys.quizz.core.extensions.secondsToMillis
import com.orbys.quizz.databinding.ServiceLaunchQuestionBinding
import com.orbys.quizz.domain.models.Bar
import com.orbys.quizz.domain.models.Question
import com.orbys.quizz.domain.models.User
import com.orbys.quizz.domain.repositories.UsersRepositoryImpl
import com.orbys.quizz.ui.view.MainActivity
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@OptIn(DelicateCoroutinesApi::class)
fun ServiceLaunchQuestionBinding.setChronometerCount(
    time: Int,
    context: Context,
    windowManager: WindowManager,
    view: View
) {
    // Muestra el cronómetro si la pregunta tiene un tiempo de espera
    chronometerTitle.visibility = ConstraintLayout.VISIBLE
    chronometer.visibility = ConstraintLayout.VISIBLE

    val timeInSeconds = time.minutesToSeconds()
    val timeInMillis = timeInSeconds.secondsToMillis()

    chronometer.setTimeInMillis(timeInMillis)
    chronometer.startCountDown()

    // Lanzamos una corrutina para recoger los cambios en el estado del cronómetro
    GlobalScope.launch {
        chronometer.isFinished.collect { isFinished ->
            if (isFinished) {
                setDownloadButtonClickable(
                    context,
                    windowManager,
                    view
                )
            }
        }
    }

}

@OptIn(DelicateCoroutinesApi::class)
fun ServiceLaunchQuestionBinding.setUsersCount(
    context: Context,
    respondedUsers: StateFlow<List<User>>
) {
    // Lanza una corrutina para recoger los cambios en el número de usuarios que han respondido
    GlobalScope.launch {
        respondedUsers.collect { usersList ->
            withContext(Dispatchers.Main) {
                respondedUsersCount.text =
                    "${context.getString(R.string.users_count_title)}${usersList.size}"
            }
        }
    }
}

@OptIn(DelicateCoroutinesApi::class)
fun ServiceLaunchQuestionBinding.setGraphicAnswersCount(question: Question) {
    // Lanza una corrutina para recoger los recuentos de respuestas y establecerlos en el grafico de barras.
    GlobalScope.launch {
        question.answers.collect { answers ->
            barView.clearBars()

            answers.forEach { answer ->
                val bar = Bar(answer.answer.toString(), height = answer.getCount())
                barView.addBar(bar)

                GlobalScope.launch {
                    // Recoge los cambios en count para cada respuesta
                    answer.count.collect { count ->
                        bar.height = count
                        barView.invalidate()
                    }
                }

            }
        }
    }
}

fun ServiceLaunchQuestionBinding.setDownloadButtonClickable(
    context: Context,
    windowManager: WindowManager,
    view: View,
    usersRepository: UsersRepositoryImpl = UsersRepositoryImpl.getInstance()
) {
    downloadButton.drawable.setTint(
        ContextCompat.getColor(
            context,
            R.color.blue_selected
        )
    )

    downloadButton.setOnClickListener {
        windowManager.removeView(view)
        usersRepository.clearRespondedUsers()

        val intent = Intent(context, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        intent.putExtra("SHOW_DOWNLOAD_FRAGMENT", true)
        context.startActivity(intent)
    }
}