package com.pprior.quizz.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.pprior.quizz.databinding.FragmentHeadBinding

class HeadFragment : Fragment() {
    // Clase que referencia al fichero xml
    private var _binding:FragmentHeadBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHeadBinding.inflate(inflater, container, false)
        return binding.root
    }
}