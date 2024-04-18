package com.orbys.quizz.di

import android.content.Context
import com.orbys.quizz.domain.repositories.QuestionRepositoryImpl
import com.orbys.quizz.domain.repositories.UsersRepositoryImpl
import com.orbys.quizz.data.controllers.HttpController
import com.orbys.quizz.data.controllers.PlaceholderController
import com.orbys.quizz.data.repositories.FileRepository
import com.orbys.quizz.data.repositories.HttpRepositoryImpl
import com.orbys.quizz.data.repositories.IFileRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Módulo Dagger que proporciona las dependencias para el servidor controladores.
 */
@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Provides
    @Singleton
    fun provideQuestionController(@ApplicationContext context: Context) = HttpController(
        HttpRepositoryImpl(
            QuestionRepositoryImpl.getInstance(),
            UsersRepositoryImpl.getInstance()
        ),
        PlaceholderController(context),
        FileRepository(context)
    )

    @Provides
    @Singleton
    fun provideServerRepository() = HttpRepositoryImpl(QuestionRepositoryImpl.getInstance(), UsersRepositoryImpl.getInstance())

    @Provides
    @Singleton
    fun provideFileRepository(@ApplicationContext context: Context): IFileRepository = FileRepository(context)

    @Provides
    @Singleton
    fun providePlaceholderController(@ApplicationContext context: Context) = PlaceholderController(context)

}