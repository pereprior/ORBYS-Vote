package com.pprior.quizz.di

import com.pprior.quizz.domain.repositories.QuestionRepositoryImpl
import com.pprior.quizz.domain.repositories.UsersRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Módulo Dagger que proporciona las dependencias para los repositorios de datos.
 */
@Module
@InstallIn(SingletonComponent::class)
object DomainModule {

    @Provides
    @Singleton
    fun provideQuestionRepository() = QuestionRepositoryImpl.getInstance()

    @Provides
    @Singleton
    fun provideUsersRepository() = UsersRepositoryImpl.getInstance()
}