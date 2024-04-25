package com.orbys.quizz.ui.view.fragments.cards

import android.os.Bundle
import android.view.View
import com.orbys.quizz.R
import com.orbys.quizz.ui.view.fragments.add.AddBooleanQuestion

class BooleanCard: QuestionTypesCard() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.setOnClickListener {
            parentFragmentManager.beginTransaction().apply {
                replace(R.id.fragment_container, AddBooleanQuestion())
                addToBackStack(null)
                commit()
            }
        }

        with(binding) {
            cardTitle.text = context?.getText(R.string.truefalse_question_type_title)
            cardIcon.setImageResource(R.drawable.ic_boolean)
        }
    }

}