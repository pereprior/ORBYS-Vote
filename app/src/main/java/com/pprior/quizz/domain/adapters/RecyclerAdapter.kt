package com.pprior.quizz.domain.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pprior.quizz.R
import com.pprior.quizz.domain.models.Question

class RecyclerAdapter(
    // Lista de items que se mostrarán en el RecyclerView
    private var questions: List<Question> = emptyList(),
): RecyclerView.Adapter<RecyclerAdapter.ViewHolder>() {

    // Nos devolverá el view holder con los datos que se mostrarán en el RecyclerView
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
        return ViewHolder(view)
    }

    // Coge cada una de las posiciones y las pasa al view holder
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = questions[position]
        holder.bind(item)
    }

    // Nos devuelve el número de items que se mostrarán en el RecyclerView
    override fun getItemCount(): Int {
        return questions.size
    }

    // Clase que nos permite manejar los elementos que se mostrarán en el RecyclerView
    class ViewHolder(
        view: View
    ): RecyclerView.ViewHolder(view)  {
        private val title: View = view.findViewById(R.id.question_title)

        fun bind(item: Question) {
            
        }
    }
}