package com.orbys.quizz.di

import android.content.Context
import com.orbys.quizz.data.repositories.FileRepository
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

    @Provides
    @Singleton
    fun provideFileRepository(@ApplicationContext context: Context): IFileRepository = FileRepository(context)
}