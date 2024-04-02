package com.pprior.quizz.ui.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.pprior.quizz.databinding.FragmentTypesBinding
import com.pprior.quizz.ui.activities.dialogs.AddQuestionActivity
import com.pprior.quizz.ui.activities.dialogs.types.AddBarQuestion
import com.pprior.quizz.ui.activities.dialogs.types.AddOtherQuestion
import com.pprior.quizz.ui.activities.dialogs.types.AddStarsQuestion
import com.pprior.quizz.ui.activities.dialogs.types.AddYesNoQuestion

class TypesFragment : Fragment() {

    private lateinit var binding: FragmentTypesBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTypesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setQuestionTypesCards()
    }

    private fun setQuestionTypesCards() {
        binding.yesNoCard.setOnClickListener {
            val intent = Intent(context, AddYesNoQuestion::class.java)
            startActivity(intent)
            activity?.finish()
        }

        binding.starCard.setOnClickListener {
            val intent = Intent(context, AddStarsQuestion::class.java)
            startActivity(intent)
            activity?.finish()
        }
        binding.barCard.setOnClickListener {
            val intent = Intent(context, AddBarQuestion::class.java)
            startActivity(intent)
            activity?.finish()
        }

        binding.otherCard.setOnClickListener {
            val intent = Intent(context, AddOtherQuestion::class.java)
            startActivity(intent)
            activity?.finish()
        }
    }

}