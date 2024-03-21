package com.pprior.quizz.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pprior.quizz.R
import com.pprior.quizz.databinding.FragmentListBinding
import com.pprior.quizz.domain.adapters.RecyclerAdapter
import com.pprior.quizz.domain.models.Question
import com.pprior.quizz.ui.viewmodels.QuestionViewModel
import com.pprior.quizz.ui.components.dialogs.AddQuestionDialog

class ListFragment : Fragment() {
    // Variables that reference the xml
    private var _listBinding: FragmentListBinding? = null
    private val listBinding get() = _listBinding!!


    private lateinit var recyclerView: RecyclerView
    private val viewModel = QuestionViewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _listBinding = FragmentListBinding.inflate(inflater, container, false)
        return listBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val dialog = AddQuestionDialog(
            viewModel = viewModel,
            context = requireContext()
        )

        dialog.setOnDismissListener {
            // Actualiza el recycler view cada vez que se cierre el dialogo
            setUpRecyclerView(viewModel.getQuestionsList())
        }

        // Muestra el dialogo para crear una nueva pregunta
        listBinding.fab.setOnClickListener {
            dialog.show()
        }
    }

    // Update the recyclerView with the new data
    private fun setUpRecyclerView(list: List<Question>) {
        val adapter = RecyclerAdapter(questions = list)
        if (list.isEmpty()) {
            return
        }

        // Reference the RecyclerView from the xml
        recyclerView = listBinding.fragmentListRecyclerView
        // Set a fixed size for the RecyclerView
        recyclerView.setHasFixedSize(true)
        // Assign a layout to the RecyclerView
        val columnNumber = resources.getInteger(R.integer.column_number)
        recyclerView.layoutManager = GridLayoutManager(context, columnNumber)
        // Assign the adapter with the default elements
        recyclerView.adapter = adapter
    }
}