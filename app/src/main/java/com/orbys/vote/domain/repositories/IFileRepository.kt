package com.orbys.vote.domain.repositories

/** Inversión de dependencias para el repositorio que gestiona las operaciones con ficheros */
interface IFileRepository {
    fun deleteFile()
    suspend fun modifyLineInFile(lineNumber: Int, newContent: String)
}