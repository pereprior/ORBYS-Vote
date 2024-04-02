package com.pprior.quizz.ui.components.recyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.pprior.quizz.domain.models.Question
import com.pprior.quizz.databinding.FragmentListItemBinding

/**
* Adaptador para el RecyclerView que muestra la lista de preguntas.
*/
class QuestionAdapter(
    private var questionList: List<Question>
): RecyclerView.Adapter<QuestionViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuestionViewHolder {
        val binding = FragmentListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return QuestionViewHolder(binding)
    }

    override fun onBindViewHolder(holder: QuestionViewHolder, position: Int) = holder.bind(questionList[position])
    override fun getItemCount() = questionList.size

}