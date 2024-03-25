package com.pprior.quizz.ui.components.dialogs

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import androidx.compose.runtime.livedata.observeAsState
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.pprior.quizz.data.constants.ENDPOINT
import com.pprior.quizz.data.constants.SERVER_PORT
import com.pprior.quizz.data.constants.host
import com.pprior.quizz.databinding.DialogLaunchQuestionBinding
import com.pprior.quizz.domain.viewModels.QuestionViewModel
import com.pprior.quizz.ui.components.QRCodeGenerator
import kotlinx.coroutines.launch

class LaunchQuestionDialog(
    question: String,
    context: Context,
    private val viewModel: QuestionViewModel,
    private val lifecycleOwner: LifecycleOwner
): QuestionDialog<DialogLaunchQuestionBinding>(context) {

    private val qrCodeGenerator = QRCodeGenerator()

    init {
        //viewModel.clearAnswer()
        bindQuestion(question)
    }

    private fun bindQuestion(question: String) {
        with(binding) {
            qrCode.setImageBitmap(
                qrCodeGenerator.encodeAsBitmap(
                    url = "${host}:$SERVER_PORT$ENDPOINT",
                    width = 200,
                    height = 200
                )
            )

            this.question.text = question

            lifecycleOwner.lifecycleScope.launch {
                viewModel.answer.observe(lifecycleOwner) { answer ->
                    Log.d("LaunchQuestionDialog", "Answer: $answer")
                    yesAnswersCount.text = answer.yesCount.toString()
                    noAnswersCount.text = answer.noCount.toString()
                }
            }

            closeButton.setOnClickListener { dismiss() }
        }
    }

    override fun getViewBinding(inflater: LayoutInflater) = DialogLaunchQuestionBinding.inflate(inflater)
}