package com.orbys.vote.ui.components.managers

import android.content.Context
import android.text.InputFilter
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import androidx.appcompat.content.res.AppCompatResources
import com.orbys.vote.R
import com.orbys.vote.domain.models.Answer
import kotlinx.coroutines.flow.MutableStateFlow

/**
 * Clase abstracta que se encarga de añadir a la vista un formulario personalizado de respuestas.
 *
 * @param context Contexto de la aplicación.
 */
abstract class AnswerManager(private val context: Context) {

    protected val answerFields = mutableListOf<EditText>()
    abstract val type: Int

    // Añadir un campo de texto a la vista
    abstract fun addAnswerField()

    // Crear un campo de texto para la respuesta
    fun createAnswerField(hintText: String, maxLength: Int) = EditText(context).apply {
        val params = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        params.setMargins(1, 8, 0, 8)
        layoutParams = params
        background = AppCompatResources.getDrawable(context, R.drawable.bg_textbox)
        hint = hintText
        inputType = type
        id = View.generateViewId()
        textSize = 8f
        filters = arrayOf(InputFilter.LengthFilter(maxLength))
        setPadding(20,20,20,20)
    }

    // Comprueba si hay alguna respuesta vacia
    fun anyAnswerIsEmpty()= answerFields.any { it.text.isEmpty() }

    // Devuelve una lista con el texto de las respuestas
    fun getAnswersText() = answerFields.map { it.text.toString() }

    // Devuelve el flow con la lista de respuestas
    fun getAnswers() = MutableStateFlow(answerFields.map { Answer(it.text.toString()) })

    // Comprueba si alguna respuesta contiene un caracter incompatible (";" o "\" o "")
    fun anyAnswerContainsInvalidCharacter() = answerFields.any { it.text.contains(Regex("[;\\\\\"]")) }
}