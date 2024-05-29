package com.orbys.vote.core.di

import android.content.Context
import com.orbys.vote.data.repositories.ClientRepositoryImpl
import com.orbys.vote.data.repositories.FileRepositoryImpl
import com.orbys.vote.data.repositories.QuestionRepositoryImpl
import com.orbys.vote.data.services.HttpService
import com.orbys.vote.domain.repositories.IClientRepository
import com.orbys.vote.domain.repositories.IFileRepository
import com.orbys.vote.domain.repositories.IQuestionRepository
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
    fun provideUsersRepository(): IClientRepository = ClientRepositoryImpl.getInstance()

    @Provides
    @Singleton
    fun provideFileRepository(@ApplicationContext context: Context): IFileRepository = FileRepositoryImpl.getInstance(context)

    @Provides
    @Singleton
    fun provideHttpService() = HttpService()

}