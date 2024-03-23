package com.pprior.quizz.domain.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pprior.quizz.domain.models.Question
import com.pprior.quizz.databinding.FragmentListItemBinding
import com.pprior.quizz.ui.components.dialogs.LaunchQuestionDialog

/**
 * Adaptador para el RecyclerView que muestra la lista de preguntas.
 *
 * @param questions lista de perguntas a mostrar.
 */
class RecyclerAdapter(
    private var questions: List<Question> = emptyList(),
): RecyclerView.Adapter<RecyclerAdapter.ViewHolder>() {

    // Crea un nuevo ViewHolder para una pregunta.
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = FragmentListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    // Asigna los valores a los componentes de la vista
    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(questions[position])

    // Devuelve la cantidad de preguntas
    override fun getItemCount() = questions.size

    /**
     * Vista para una pregunta.
     *
     * @property binding El vinculo para la vista de la pregunta.
     */
    inner class ViewHolder(private val binding: FragmentListItemBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(question: Question) = with(binding) {
            // Vincula una pregunta a la vista
            titleQuestion.text = question.title

            // Muestra el dialogo para poder contestar esa pregunta
            launchButton.setOnClickListener { LaunchQuestionDialog(question.question, it.context).show() }
        }
    }
}