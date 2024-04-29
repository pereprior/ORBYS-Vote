package com.orbys.quizz.core.extensions

import com.orbys.quizz.domain.models.Question

fun Question.getAnswersList(answer: String) = answers.value
fun Question.checkIfAnswerExists(answer: String) = answers.value.any { it.answer == answer }