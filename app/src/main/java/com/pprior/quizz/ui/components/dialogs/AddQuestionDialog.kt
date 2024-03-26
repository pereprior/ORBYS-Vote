package com.pprior.quizz.ui.components.dialogs

import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.view.Window
import com.pprior.quizz.R
import com.pprior.quizz.data.flow.FlowRepository
import com.pprior.quizz.data.server.models.Question
import com.pprior.quizz.databinding.DialogAddQuestionBinding

/**
 * Clase que representa un dialogo para añadir preguntas a la lista.
 *
 * @param flowRepository El repositorio que gestiona las preguntas.
 * @param context El contexto del dialogo.
 */
open class AddQuestionDialog(
    private val flowRepository: FlowRepository,
    context: Context
) : Dialog(context) {

    private var _binding: DialogAddQuestionBinding? = null
    protected val binding get() = _binding!!
    init {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        _binding = DialogAddQuestionBinding.inflate(LayoutInflater.from(context))
        setContentView(binding.root)

        with(binding) {
            // Asignar los listeners a los botones
            saveButton.setOnClickListener { saveQuestion() }
            closeButton.setOnClickListener { dismiss() }
        }
    }

    open fun saveQuestion() {
        val question = Question(
            binding.questionTitle.text.toString(),
            binding.questionQuestion.text.toString()
        )

        // Comprobar si la pregunta o el título están vacíos
        if (question.title.isEmpty() || question.question.isEmpty()) {
            return
        }

        // Si ya existe la pregunta, mostrar un mensaje de error
        if (flowRepository.exists(question)) {
            binding.errorMessage.text = context.getString(R.string.questions_exists)
        } else {
            // Guardamos la pregunta en la lista y cerramos el dialogo
            flowRepository.addQuestion(question)
            dismiss()
            clear()
        }
    }

    private fun clear() {
        with(binding) {
            // Limpiar los campos de texto y el mensaje de error
            questionTitle.text.clear()
            questionQuestion.text.clear()
            errorMessage.text = ""
        }
    }
}