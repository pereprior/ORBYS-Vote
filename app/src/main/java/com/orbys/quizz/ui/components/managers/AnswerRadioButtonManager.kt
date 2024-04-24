package com.orbys.quizz.ui.components.managers

import android.content.Context
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import com.orbys.quizz.R

class AnswerRadioButtonManager(
    private val context: Context,
    private val layout: LinearLayout
) : AnswerManager(context, layout) {

    fun createRadioGroup() {
        val radioGroup = RadioGroup(context).apply {
            id = R.id.filter_users_group
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            orientation = RadioGroup.HORIZONTAL

            addView(createRadioButton(R.id.non_filter_users_question_option, true))
            addView(createTextView(R.string.yesno_question_type_title))

            addView(createRadioButton(R.id.filter_users_question_option, false))
            addView(createTextView(R.string.truefalse_question_type_title))
        }

        layout.addView(radioGroup)
    }

    private fun createRadioButton(id: Int, isChecked: Boolean): RadioButton {
        return RadioButton(context).apply {
            this.id = id
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            scaleX = 0.6f
            scaleY = 0.6f
            this.isChecked = isChecked
        }
    }

    private fun createTextView(textResId: Int): TextView {
        return TextView(context).apply {
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            text = context.getString(textResId)
        }
    }

    fun getCheckedRadioButtonId(): Int {
        return layout.findViewById<RadioGroup>(R.id.filter_users_group).checkedRadioButtonId
    }

}