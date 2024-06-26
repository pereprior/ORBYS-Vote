package com.orbys.vote.ui.viewmodels

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.orbys.vote.core.extensions.isNetworkAvailable
import com.orbys.vote.domain.models.Question
import com.orbys.vote.domain.usecases.GenerateQuestionUseCase
import com.orbys.vote.domain.usecases.GetHttpServiceUseCase
import com.orbys.vote.domain.usecases.GetQuestionUseCase
import com.orbys.vote.domain.usecases.ModifyFileLineUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

/** ViewModel que proporciona funciones para interactuar con los casos de uso de la lista de preguntas */
@HiltViewModel
class QuestionViewModel @Inject constructor(
    private val getQuestionUseCase: GetQuestionUseCase,
    private val generateQuestionUseCase: GenerateQuestionUseCase,
    private val modifyFileLineUseCase: ModifyFileLineUseCase,
    private val getHttpServiceUseCase: GetHttpServiceUseCase
): ViewModel() {

    /** Devuelve la pregunta activa */
    fun getQuestion() = getQuestionUseCase()

    /** Genera una nueva pregunta */
    fun generateQuestion(question: Question) { generateQuestionUseCase(question) }

    /** Modifica una línea del fichero de resultados CSV */
    fun modifyFile(lineNumber: Int, content: String) { viewModelScope.launch { modifyFileLineUseCase(lineNumber, content) } }

    /** Devuelve una instancia del servicio que inicia el servidor http */
    fun getHttpService() = getHttpServiceUseCase()

    /** Comprueba si hay conexión a Internet */
    fun isNetworkAvailable(activity: AppCompatActivity) = activity.isNetworkAvailable()

}