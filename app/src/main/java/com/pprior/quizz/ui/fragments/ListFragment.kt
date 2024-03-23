package com.pprior.quizz.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.pprior.quizz.R
import com.pprior.quizz.databinding.FragmentListBinding
import com.pprior.quizz.domain.adapters.RecyclerAdapter
import com.pprior.quizz.domain.models.Question
import com.pprior.quizz.ui.viewmodels.QuestionViewModel
import com.pprior.quizz.ui.components.dialogs.AddQuestionDialog

/**
 * Fragmento que muestra la lista de preguntas.
 *
 * Se inicializa el ViewModel que gestiona la lista de preguntas y se configura
 */
class ListFragment : Fragment() {

    private lateinit var binding: FragmentListBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Inicializamos el ViewModel que gestiona la lista de preguntas
        val viewModel = QuestionViewModel()

        // Dialogo para añadir preguntas a la lista
        val dialog = AddQuestionDialog(
            viewModel = viewModel,
            context = requireContext()
        )

        // Cuando se cierre el dialogo, actualizamos la lista de preguntas
        dialog.setOnDismissListener {
            setUpRecyclerView(viewModel.getQuestionsList())
        }

        // Boton flotante para mostrar el dialogo de añadir preguntas
        binding.fab.setOnClickListener {
            dialog.show()
        }
    }

    private fun setUpRecyclerView(list: List<Question>) {
        // Si aun no hay preguntas, no pintamos el recyclerView
        if (list.isEmpty()) {
            return
        }

        // Configuramos el recyclerView con la lista de preguntas
        binding.fragmentListRecyclerView.apply {
            setHasFixedSize(true)
            // Numero de columnas en el recyclerView dependiendo del ancho de la pantalla del dispositivo
            layoutManager = GridLayoutManager(context, resources.getInteger(R.integer.column_number))
            adapter = RecyclerAdapter(questions = list)
        }
    }
}