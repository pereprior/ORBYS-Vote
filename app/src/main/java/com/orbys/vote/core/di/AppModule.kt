package com.orbys.vote.core.di

import android.content.Context
import com.orbys.vote.data.controllers.handlers.FileHandler
import com.orbys.vote.data.controllers.handlers.ResponseHandler
import com.orbys.vote.data.repositories.FileRepository
import com.orbys.vote.data.repositories.QuestionRepositoryImpl
import com.orbys.vote.data.repositories.UsersRepositoryImpl
import com.orbys.vote.data.services.HttpService
import com.orbys.vote.domain.repositories.IFileRepository
import com.orbys.vote.domain.repositories.IQuestionRepository
import com.orbys.vote.domain.repositories.IUsersRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Módulo Dagger que proporciona las dependencias a las diferentes clases de la aplicación.
 */
@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideApplicationContext(@ApplicationContext context: Context) = context

    @Provides
    @Singleton
    fun provideQuestionRepository(): IQuestionRepository = QuestionRepositoryImpl.getInstance()

    @Provides
    @Singleton
    fun provideUsersRepository(): IUsersRepository = UsersRepositoryImpl.getInstance()

    @Provides
    @Singleton
    fun provideFileRepository(@ApplicationContext context: Context): IFileRepository = FileRepository.getInstance(context)

    @Provides
    @Singleton
    fun provideHttpService() = HttpService()

    @Provides
    @Singleton
    fun provideFileHandler(@ApplicationContext context: Context) = FileHandler(
        QuestionRepositoryImpl.getInstance(), UsersRepositoryImpl.getInstance(), context
    )

    @Provides
    @Singleton
    fun provideResponseHandler(@ApplicationContext context: Context) = ResponseHandler(
        QuestionRepositoryImpl.getInstance(), UsersRepositoryImpl.getInstance(), provideFileHandler(context)
    )

}