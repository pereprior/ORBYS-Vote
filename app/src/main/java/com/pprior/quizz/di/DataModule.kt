package com.pprior.quizz.di

import com.pprior.quizz.domain.repositories.QuestionRepositoryImpl
import com.pprior.quizz.domain.repositories.UsersRepositoryImpl
import com.pprior.quizz.data.controllers.HttpController
import com.pprior.quizz.data.repositories.HttpRepositoryImpl
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