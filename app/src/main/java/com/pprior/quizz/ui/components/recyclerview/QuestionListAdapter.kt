package com.pprior.quizz.ui.components.recyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pprior.quizz.databinding.FragmentListItemBinding
import com.pprior.quizz.ui.viewModels.QuestionViewModel

/**
* Adaptador para el RecyclerView que muestra la lista de preguntas.
*/
class QuestionListAdapter(
    private val viewModel: QuestionViewModel
): RecyclerView.Adapter<QuestionViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuestionViewHolder {
        val binding = FragmentListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        // ViewHolder de cada pregunta del RecyclerView
        return QuestionViewHolder(viewModel, binding)
    }

    override fun onBindViewHolder(holder: QuestionViewHolder, position: Int) = holder.bind(viewModel.getQuestions()[position])
    override fun getItemCount() = viewModel.getQuestions().size

}