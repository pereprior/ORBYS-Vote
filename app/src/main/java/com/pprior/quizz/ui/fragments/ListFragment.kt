package com.pprior.quizz.ui.fragments

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.pprior.quizz.R
import com.pprior.quizz.data.flow.FlowRepository
import com.pprior.quizz.databinding.FragmentListBinding
import com.pprior.quizz.data.adapters.RecyclerAdapter
import com.pprior.quizz.ui.activities.dialogs.AddQuestionActivity
import kotlinx.coroutines.launch
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

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setAddQuestionDialog()
        setUpRecyclerView()

        // Corrutina para observar los cambios en la lista de preguntas
        viewLifecycleOwner.lifecycleScope.launch {
            flowRepository.questionUpdated.collect { _ ->
                // Actualizar el RecyclerView cuando la lista de preguntas cambia
                binding.fragmentListRecyclerView.adapter?.notifyDataSetChanged()
            }
        }

    }

    private fun setAddQuestionDialog() {
        // Boton flotante para mostrar la actividad de añadir preguntas
        binding.fab.setOnClickListener {
            val intent = Intent(it.context, AddQuestionActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setUpRecyclerView() {
        // Configuramos el recyclerView con la lista de preguntas
        binding.fragmentListRecyclerView.apply {
            setHasFixedSize(true)
            layoutManager = GridLayoutManager(context, resources.getInteger(R.integer.column_number))
            adapter = RecyclerAdapter(flowRepository)
        }
    }

}