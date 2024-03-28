package com.pprior.quizz.data.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pprior.quizz.R
import com.pprior.quizz.data.flow.FlowRepository
import com.pprior.quizz.data.server.models.Question
import com.pprior.quizz.databinding.FragmentListItemBinding
import com.pprior.quizz.ui.activities.dialogs.EditQuestionActivity
import com.pprior.quizz.ui.components.dialogs.ConfirmDialog
import com.pprior.quizz.ui.activities.dialogs.LaunchQuestionActivity

/**
* Adaptador para el RecyclerView que muestra la lista de preguntas.
*
* @param flowRepository repositorio que contiene la lista de preguntas.
*/
class RecyclerAdapter(
    private var flowRepository: FlowRepository
): RecyclerView.Adapter<RecyclerAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = FragmentListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(flowRepository.questionsList.value[position])
    override fun getItemCount() = flowRepository.questionsList.value.size

    /**
     * Vista para la tarjeta de cada pregunta.
     *
     * @property binding El vinculo para la vista de la pregunta.
     */
    inner class ViewHolder(
        private val binding: FragmentListItemBinding
    ): RecyclerView.ViewHolder(binding.root) {

        fun bind(question: Question) = with(binding) {
            // Vincula una pregunta a la vista
            titleQuestion.text = question.title

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
                flowRepository.deleteQuestion(question)
            }
        }

        private fun showLaunchQuestionDialog(question: Question, view: View) {
            val intent = Intent(view.context, LaunchQuestionActivity::class.java)
            intent.putExtra("question", question.question)
            view.context.startActivity(intent)
        }

        private fun showEditQuestionDialog(question: Question, view: View) {
            val intent = Intent(view.context, EditQuestionActivity::class.java)
            intent.putExtra("questionTitle", question.title)
            intent.putExtra("questionQuestion", question.question)
            view.context.startActivity(intent)
        }

    }

}