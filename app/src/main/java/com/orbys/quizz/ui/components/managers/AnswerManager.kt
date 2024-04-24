package com.orbys.quizz.ui.components.managers

import android.content.Context
import android.widget.LinearLayout
import android.widget.TextView
import com.orbys.quizz.R

abstract class AnswerManager(context: Context, layout: LinearLayout) {
    init {
        val textView = TextView(context).apply {
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            text = context.getString(R.string.question_answer_title)
        }

        layout.addView(textView)
    }
}