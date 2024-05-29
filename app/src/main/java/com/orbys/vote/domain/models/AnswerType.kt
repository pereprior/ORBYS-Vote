package com.orbys.vote.domain.models

import com.orbys.vote.R

/**
 * Clase que representa los tipos de respuestas que puede tener una pregunta.
 *
 * @param titleResId Identificador del recurso de texto que representa el título del tipo de respuesta.
 * @param iconResId Identificador del recurso de imagen que representa el icono del tipo de respuesta.
 */
enum class AnswerType(val titleResId: Int, val iconResId: Int) {
    NONE(R.string.empty_answers_error, R.drawable.ic_help),
    YES_NO(R.string.yesno_question_type_title, R.drawable.ic_yesno),
    BOOLEAN(R.string.truefalse_question_type_title, R.drawable.ic_boolean),
    STARS(R.string.stars_question_type_title, R.drawable.ic_star),
    NUMERIC(R.string.numeric_question_type_title, R.drawable.ic_number),
    OTHER(R.string.other_question_type_title, R.drawable.ic_others)
}