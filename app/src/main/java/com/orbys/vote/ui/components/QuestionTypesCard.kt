package com.orbys.vote.ui.components

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.orbys.vote.databinding.CardTypesQuestionBinding
import com.orbys.vote.domain.models.AnswerType

/**
 * Clase que extiende de [FrameLayout]
 * Representa una tarjeta con el tipo de pregunta y su icono correspondiente.
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