package com.orbys.quizz.domain.models

import com.orbys.quizz.R

// Clase que representa los tipos de respuesta que puede tener una pregunta.
enum class AnswerType(val titleResId: Int, val iconResId: Int) {
    NONE(R.string.empty_answers_error, R.drawable.ic_help),
    YES_NO(R.string.yesno_question_type_title, R.drawable.ic_yesno),
    BOOLEAN(R.string.truefalse_question_type_title, R.drawable.ic_boolean),
    STARS(R.string.stars_question_type_title, R.drawable.ic_star),
    NUMERIC(R.string.numeric_question_type_title, R.drawable.ic_number),
    OTHER(R.string.other_question_type_title, R.drawable.ic_others)
}