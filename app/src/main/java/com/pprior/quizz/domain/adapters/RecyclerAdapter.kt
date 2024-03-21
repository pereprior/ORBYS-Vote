package com.pprior.quizz.domain.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pprior.quizz.domain.models.Question
import com.pprior.quizz.databinding.FragmentListItemBinding
import com.pprior.quizz.ui.components.dialogs.LaunchQuestionDialog

class RecyclerAdapter(
    // Lista de preguntas que se mostrarán en el RecyclerView
    private var questions: List<Question> = emptyList(),
): RecyclerView.Adapter<RecyclerAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = FragmentListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = questions[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int {
        return questions.size
    }

    // Clase que nos permite manejar los elementos que se mostrarán en el RecyclerView
    class ViewHolder(
        private val binding: FragmentListItemBinding
    ): RecyclerView.ViewHolder(binding.root) {

        fun bind(question: Question) {
            binding.titleQuestion.text = question.title

            binding.launchButton.setOnClickListener {
                LaunchQuestionDialog(
                    question = question.question,
                    it.context
                ).show()
            }
        }
    }
}