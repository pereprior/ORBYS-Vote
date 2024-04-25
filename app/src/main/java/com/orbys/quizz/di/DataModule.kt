package com.orbys.quizz.di

import android.content.Context
import com.orbys.quizz.data.controllers.HttpController
import com.orbys.quizz.data.controllers.handlers.FileHandler
import com.orbys.quizz.data.controllers.handlers.ResponseHandler
import com.orbys.quizz.data.repositories.FileRepository
import com.orbys.quizz.data.repositories.HttpRepositoryImpl
import com.orbys.quizz.data.repositories.IFileRepository
import com.orbys.quizz.domain.repositories.QuestionRepositoryImpl
import com.orbys.quizz.domain.repositories.UsersRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Módulo Dagger que proporciona las dependencias para el servidor y sus controladores.
 */
@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Provides
    @Singleton
    fun provideServerRepository() = HttpRepositoryImpl(QuestionRepositoryImpl.getInstance(), UsersRepositoryImpl.getInstance())

    @Provides
    @Singleton
    fun provideFileRepository(@ApplicationContext context: Context): IFileRepository = FileRepository.getInstance(context)

    @Provides
    @Singleton
    fun provideFileHandler(@ApplicationContext context: Context) = FileHandler(
        HttpRepositoryImpl(
            QuestionRepositoryImpl.getInstance(),
            UsersRepositoryImpl.getInstance()
        ),
        context
    )

    @Provides
    @Singleton
    fun provideQuestionController(@ApplicationContext context: Context) = HttpController(
        ResponseHandler(
            HttpRepositoryImpl(
                QuestionRepositoryImpl.getInstance(),
                UsersRepositoryImpl.getInstance()
            ),
            FileHandler(
                HttpRepositoryImpl(
                    QuestionRepositoryImpl.getInstance(),
                    UsersRepositoryImpl.getInstance()
                ),
                context
            ),
            context
        ),

        FileHandler(
            HttpRepositoryImpl(
                QuestionRepositoryImpl.getInstance(),
                UsersRepositoryImpl.getInstance()
            ),
            context
        )
    )

}