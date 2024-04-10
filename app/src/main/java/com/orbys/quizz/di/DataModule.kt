package com.orbys.quizz.di

import com.orbys.quizz.domain.repositories.QuestionRepositoryImpl
import com.orbys.quizz.domain.repositories.UsersRepositoryImpl
import com.orbys.quizz.data.controllers.HttpController
import com.orbys.quizz.data.repositories.HttpRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
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
    fun provideQuestionController() = HttpController(HttpRepositoryImpl(QuestionRepositoryImpl.getInstance(), UsersRepositoryImpl.getInstance()))

    @Provides
    @Singleton
    fun provideServerRepository() = HttpRepositoryImpl(QuestionRepositoryImpl.getInstance(), UsersRepositoryImpl.getInstance())

}