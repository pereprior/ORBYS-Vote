package com.orbys.vote.domain.usecases

import com.orbys.vote.domain.repositories.IFileRepository
import javax.inject.Inject

/** Caso de uso para modificar el contenido de una línea del fichero CSV que contiene los resultados de la pregunta */
class ModifyFileLineUseCase @Inject constructor(
    private val repository: IFileRepository
) {
    suspend operator fun invoke(lineNumber: Int, content: String) { repository.modifyLineInFile(lineNumber, content) }
}