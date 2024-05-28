package com.orbys.vote.domain.repositories

interface IFileRepository {
    fun deleteFile()
    suspend fun modifyLineInFile(lineNumber: Int, newContent: String)
}