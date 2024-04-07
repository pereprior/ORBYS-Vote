package com.pprior.quizz.ui.fragments.types

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.pprior.quizz.databinding.FragmentTypesItemBinding

/**
 * Clase que sirve como base para los fragmentos que representan a las tarjetas de tipos de preguntas.
 *
 * @property binding Enlace común a la vista que comparten todas las clases que extienden.
 */
abstract class TypesItemFragment : Fragment() {

    protected lateinit var binding: FragmentTypesItemBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTypesItemBinding.inflate(inflater, container, false)
        return binding.root
    }

}