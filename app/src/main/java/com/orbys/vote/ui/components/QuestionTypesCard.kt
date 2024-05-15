package com.orbys.vote.ui.components

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.orbys.vote.databinding.CardTypesQuestionBinding
import com.orbys.vote.domain.models.AnswerType

/**
 * Clase que sirve como base para las vistas que representan a las tarjetas de tipos de preguntas.
 *
 * @property binding Enlace común a la vista que comparten todas las clases que extienden.
 */
class QuestionTypesCard(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private var binding: CardTypesQuestionBinding = CardTypesQuestionBinding.inflate(LayoutInflater.from(context), this, true)

    fun bindCard(answerType: AnswerType, isCard: Boolean = true) {
        with(binding) {
            cardTitle.text = context.getText(answerType.titleResId)
            cardIcon.setImageResource(answerType.iconResId)
            if (!isCard) {
                questionTypeCard.setBackgroundColor(Color.TRANSPARENT)
            }
        }
    }

}