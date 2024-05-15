package com.orbys.vote.domain.repositories

import java.io.File

interface IFileRepository {
    fun getFile(): File
    fun deleteFile()
}