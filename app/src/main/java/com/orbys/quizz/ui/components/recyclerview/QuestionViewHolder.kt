package com.orbys.quizz.ui.components.recyclerview

import android.content.Context
import android.content.Intent
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.orbys.quizz.R
import com.orbys.quizz.databinding.FragmentListItemBinding
import com.orbys.quizz.domain.models.Question
import com.orbys.quizz.ui.activities.dialogs.EditQuestionActivity
import com.orbys.quizz.ui.activities.dialogs.LaunchQuestionActivity
import com.orbys.quizz.ui.components.alert.ConfirmDialog
import com.orbys.quizz.ui.viewModels.QuestionViewModel

/**
 * Extiende de RecyclerView.ViewHolder y se utiliza para representar una tarjeta de pregunta.
 *
 * @property viewModel El ViewModel que se utiliza para interactuar con los datos de la aplicación.
 * @property binding El objeto de binding que se utiliza para acceder a los elementos de la interfaz de usuario.
 */
class QuestionViewHolder(
    private val viewModel: QuestionViewModel,
    private val binding: FragmentListItemBinding
): RecyclerView.ViewHolder(binding.root) {

    fun bind(question: Question) = with(binding) {
        // Vincula una pregunta a la vista
        questionTypeIcon.setImageResource(question.icon)
        titleQuestion.text = question.question

        // Muestra el diálogo para eliminar la pregunta
        deleteButton.setOnClickListener {
            deleteQuestion(it.context, question)
        }

        // Muestra el dialogo para poder contestar esa pregunta
        launchButton.setOnClickListener {
            showLaunchQuestionDialog(question, it)
        }

        // Muestra el diálogo para editar la pregunta
        editButton.setOnClickListener {
            showEditQuestionDialog(question, it)
        }
    }

    private fun deleteQuestion(
        context: Context,
        question: Question
    ) {
        ConfirmDialog(context, message = context.getString(R.string.delete_question_message)) {
            viewModel.deleteQuestion(question)
        }
    }

    private fun showLaunchQuestionDialog(question: Question, view: View) {
        val intent = Intent(view.context, LaunchQuestionActivity::class.java)
        intent.putExtra("question", question.question)
        view.context.startActivity(intent)
    }

    private fun showEditQuestionDialog(question: Question, view: View) {
        val intent = Intent(view.context, EditQuestionActivity::class.java)
        intent.putExtra("question", question.question)
        view.context.startActivity(intent)
    }

}