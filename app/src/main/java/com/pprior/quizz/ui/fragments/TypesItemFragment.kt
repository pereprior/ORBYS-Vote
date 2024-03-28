package com.pprior.quizz.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.pprior.quizz.databinding.FragmentTypesItemBinding

class TypesItemFragment : Fragment() {

    private lateinit var binding: FragmentTypesItemBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTypesItemBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.cardTitle.text = "Yes/No"
        binding.cardDescription.text = "Preguntas de respuesta binaria"
    }

}