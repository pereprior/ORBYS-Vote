package com.orbys.quizz.di

import com.orbys.quizz.domain.repositories.QuestionRepositoryImpl
import com.orbys.quizz.domain.repositories.UsersRepositoryImpl
import com.orbys.quizz.domain.usecases.AddQuestionUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Módulo Dagger que proporciona las dependencias para los repositorios del dominio.
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

    @Provides
    @Singleton
    fun provideAddQuestionUseCase() = AddQuestionUseCase(QuestionRepositoryImpl.getInstance())

}