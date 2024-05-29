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
 * Clase abstracta que se encarga de gestionar los campos para personalizar las respuestas de una pregunta.
 *
 * @param context Contexto de la aplicación.
 * @property answerFields Lista que contiene los campos en los que podemos modificar las respuestas de la pregunta desde la vista.
 * @property fieldsType Tipo de datos que admiten los campos de la lista.
 */
abstract class AnswerFieldsManager(private val context: Context) {

    protected val answerFields = mutableListOf<EditText>()
    abstract val fieldsType: Int

    /** Función abstracta para añadir un nuevo campo a la lista */
    abstract fun addAnswerField()

    /**
     * Crea un campo para introducir una respuesta.
     *
     * @param hintText Texto de ayuda que se mostrará en el campo de texto.
     * @param maxLength Cantidad máxima de caracteres que se pueden introducir.
     */
    fun createAnswerField(hintText: String, maxLength: Int) = EditText(context).apply {
        val params = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        params.setMargins(1, 8, 0, 8)
        layoutParams = params
        background = AppCompatResources.getDrawable(context, R.drawable.bg_textbox)
        hint = hintText
        inputType = fieldsType
        id = View.generateViewId()
        textSize = 8f
        filters = arrayOf(InputFilter.LengthFilter(maxLength))
        setPadding(20,20,20,20)
    }

    /** Comprueba si el contenido de algún campo de la lista es nulo o vacío */
    fun anyAnswerIsEmpty()= answerFields.any { it.text.isNullOrEmpty() }

    /** Comprueba si algún campo de la lista contiene un carácter incompatible ( ; \ " ) */
    fun anyAnswerContainsInvalidCharacter() = answerFields.any { it.text.contains(Regex("[;\\\\\"]")) }

    /** Devuelve una lista con el contenido de cada campo de la lista */
    fun getAnswersText() = answerFields.map { it.text.toString() }

    /** Convierte la lista de los campos en un flujo de respuestas */
    fun getAnswers() = MutableStateFlow(answerFields.map { Answer(it.text.toString()) })
}