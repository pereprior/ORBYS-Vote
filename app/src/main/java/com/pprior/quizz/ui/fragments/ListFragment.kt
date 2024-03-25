package com.pprior.quizz.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.pprior.quizz.R
import com.pprior.quizz.data.flow.FlowRepository
import com.pprior.quizz.databinding.FragmentListBinding
import com.pprior.quizz.domain.adapters.RecyclerAdapter
import com.pprior.quizz.ui.components.dialogs.AddQuestionDialog
import org.koin.java.KoinJavaComponent.inject

/**
 * Fragmento que muestra la lista de preguntas.
 *
 * Se inicializa el ViewModel que gestiona la lista de preguntas y se configura
 */
class ListFragment: Fragment() {

    private lateinit var binding: FragmentListBinding
    private val flowRepository: FlowRepository by inject(FlowRepository::class.java)

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
            flowRepository = flowRepository,
            context = requireContext()
        )

        // Cuando se cierre el dialogo, actualizamos la lista de preguntas
        dialog.setOnDismissListener {
            setUpRecyclerView()
        }

        // Boton flotante para mostrar el dialogo de añadir preguntas
        binding.fab.setOnClickListener {
            dialog.show()
        }
    }

    private fun setUpRecyclerView() {
        // Si aun no hay preguntas, no pintamos el recyclerView
        if (flowRepository.getQuestionsList().isEmpty()) {
            return
        }

        // Configuramos el recyclerView con la lista de preguntas
        binding.fragmentListRecyclerView.apply {
            setHasFixedSize(true)
            layoutManager = GridLayoutManager(context, resources.getInteger(R.integer.column_number))
            adapter = RecyclerAdapter(flowRepository, viewLifecycleOwner)
        }
    }
}