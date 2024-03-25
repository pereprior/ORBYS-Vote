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
import com.pprior.quizz.domain.viewModels.QuestionViewModel
import com.pprior.quizz.ui.components.dialogs.AddQuestionDialog
import org.koin.java.KoinJavaComponent
import org.koin.java.KoinJavaComponent.inject

/**
 * Fragmento que muestra la lista de preguntas.
 *
 * Se inicializa el ViewModel que gestiona la lista de preguntas y se configura
 */
class ListFragment: Fragment() {

    private lateinit var binding: FragmentListBinding
    private val viewModel: QuestionViewModel by inject(QuestionViewModel::class.java)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Dialogo para añadir preguntas a la lista
        val dialog = AddQuestionDialog(
            viewModel = viewModel,
            context = requireContext()
        )

        // Cuando se cierre el dialogo, actualizamos la lista de preguntas
        dialog.setOnDismissListener {
            setUpRecyclerView(viewModel)
        }

        // Boton flotante para mostrar el dialogo de añadir preguntas
        binding.fab.setOnClickListener {
            dialog.show()
        }
    }

    private fun setUpRecyclerView(viewModel: QuestionViewModel) {
        // Si aun no hay preguntas, no pintamos el recyclerView
        if (viewModel.getQuestionsList().isEmpty()) {
            return
        }

        // Configuramos el recyclerView con la lista de preguntas
        binding.fragmentListRecyclerView.apply {
            setHasFixedSize(true)
            layoutManager = GridLayoutManager(context, resources.getInteger(R.integer.column_number))
            adapter = RecyclerAdapter(viewModel, viewLifecycleOwner)
        }
    }
}