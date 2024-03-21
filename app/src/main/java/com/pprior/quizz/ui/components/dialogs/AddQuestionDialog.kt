package com.pprior.quizz.ui.components.dialogs

import android.content.Context
import android.view.LayoutInflater
import com.pprior.quizz.R
import com.pprior.quizz.domain.models.Question
import com.pprior.quizz.databinding.DialogAddQuestionBinding
import com.pprior.quizz.ui.viewmodels.QuestionViewModel

class AddQuestionDialog(
    private val viewModel: QuestionViewModel,
    context: Context
) : QuestionDialog<DialogAddQuestionBinding>(context) {

    init {
        binding.saveButton.setOnClickListener {
            saveQuestion()
        }

        binding.closeButton.setOnClickListener {
            dismiss()
        }
    }

    override fun getViewBinding(inflater: LayoutInflater): DialogAddQuestionBinding {
        return DialogAddQuestionBinding.inflate(inflater)
    }

    private fun saveQuestion() {
        val question = Question(
            binding.questionTitle.text.toString(),
            binding.questionQuestion.text.toString()
        )

        // Comprobar si la pregunta o el título están vacíos
        if (question.title.isEmpty() || question.question.isEmpty()) {
            return
        }

        // Si ya existe la pregunta, mostrar un mensaje de error
        if (viewModel.exists(question)) {
            binding.errorMessage.text = context.getString(R.string.questions_exists)
        } else {
            viewModel.addQuestion(question)
            dismiss()
            clear()
        }
    }

    private fun clear() {
        binding.questionTitle.text.clear()
        binding.questionQuestion.text.clear()
        binding.errorMessage.text = ""
    }
}