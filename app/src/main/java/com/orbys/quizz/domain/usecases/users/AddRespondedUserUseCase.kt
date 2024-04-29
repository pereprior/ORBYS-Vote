package com.orbys.quizz.domain.usecases.users

import com.orbys.quizz.domain.models.User
import com.orbys.quizz.domain.repositories.IUsersRepository
import javax.inject.Inject

class AddRespondedUserUseCase @Inject constructor(
    private val repository: IUsersRepository
) {
    operator fun invoke(user: User) { repository.addRespondedUser(user) }
}