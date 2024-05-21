package com.orbys.vote.domain.usecases

import com.orbys.vote.domain.repositories.IFileRepository
import javax.inject.Inject

class ModifyFileUseCase @Inject constructor(
    private val repository: IFileRepository
) {
    suspend operator fun invoke(lineNumber: Int, content: String) { repository.modifyLineInFile(lineNumber, content) }
}