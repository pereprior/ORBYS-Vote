package com.orbys.quizz.domain.usecases

import com.orbys.quizz.data.services.HttpService
import javax.inject.Inject

class GetHttpServiceUseCase @Inject constructor(
    private val service: HttpService
) {
    operator fun invoke() = service
}