package com.pprior.quizz.ui.components.dialogs

import android.content.Context
import android.view.LayoutInflater
import com.pprior.quizz.data.models.Question
import com.pprior.quizz.databinding.DialogAddQuestionBinding
import com.pprior.quizz.ui.viewmodels.QuestionViewModel

class AddQuestionDialog(
    private val viewModel: QuestionViewModel,
    context: Context
) : QuestionDialog<DialogAddQuestionBinding>(context) {

    init {
        binding.saveButton.setOnClickListener {
            saveQuestion()
            dismiss()
            clear()
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
            binding.questionTitle.text.toString()
        )

        viewModel.addQuestion(question)
    }

    private fun clear() {
        binding.questionTitle.text.clear()
        binding.questionQuestion.text.clear()
    }
}