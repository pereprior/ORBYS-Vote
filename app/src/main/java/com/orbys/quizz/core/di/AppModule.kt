package com.orbys.quizz.core.di

import android.content.Context
import com.orbys.quizz.core.managers.NetworkManager
import com.orbys.quizz.data.controllers.handlers.FileHandler
import com.orbys.quizz.data.controllers.handlers.ResponseHandler
import com.orbys.quizz.data.repositories.FileRepository
import com.orbys.quizz.data.repositories.QuestionRepositoryImpl
import com.orbys.quizz.data.repositories.UsersRepositoryImpl
import com.orbys.quizz.data.services.HttpService
import com.orbys.quizz.domain.repositories.IFileRepository
import com.orbys.quizz.domain.repositories.IQuestionRepository
import com.orbys.quizz.domain.repositories.IUsersRepository
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
    fun provideNetworkManager() = NetworkManager()

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