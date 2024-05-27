package com.orbys.vote.ui.viewmodels

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.orbys.vote.core.extensions.isNetworkAvailable
import com.orbys.vote.domain.models.Question
import com.orbys.vote.domain.usecases.AddQuestionUseCase
import com.orbys.vote.domain.usecases.GetQuestionUseCase
import com.orbys.vote.domain.usecases.ModifyFileUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel que proporciona funciones para interactuar con los casos de uso de la lista de preguntas.
 */
@HiltViewModel
class QuestionViewModel @Inject constructor(
    private val getQuestionUseCase: GetQuestionUseCase,
    private val addQuestionUseCase: AddQuestionUseCase,
    private val modifyFileUseCase: ModifyFileUseCase
): ViewModel() {

    fun getQuestion() = getQuestionUseCase()
    fun addQuestion(question: Question) { addQuestionUseCase(question) }
    fun modifyFile(lineNumber: Int, content: String) { viewModelScope.launch { modifyFileUseCase(lineNumber, content) } }
    fun isNetworkAvailable(activity: AppCompatActivity) = activity.isNetworkAvailable()

}