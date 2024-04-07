package com.pprior.quizz.ui.viewModels

import androidx.lifecycle.ViewModel
import com.pprior.quizz.domain.repositories.UsersRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * ViewModel que proporciona funciones para interactuar con los casos de uso de la lista de usuarios que ya han respondido.
 */
@HiltViewModel
class UserViewModel @Inject constructor(
    private val repositoryImpl: UsersRepositoryImpl
): ViewModel() {
    fun clearRespondedUsers() = repositoryImpl.clearRespondedUsers()
}