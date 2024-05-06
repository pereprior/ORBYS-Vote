package com.orbys.quizz.di

import android.content.Context
import com.orbys.quizz.core.managers.NetworkManager
import com.orbys.quizz.data.controllers.handlers.ErrorHandler
import com.orbys.quizz.data.controllers.handlers.FileHandler
import com.orbys.quizz.data.controllers.handlers.ResponseHandler
import com.orbys.quizz.data.repositories.FileRepository
import com.orbys.quizz.data.repositories.HttpRepositoryImpl
import com.orbys.quizz.data.repositories.IFileRepository
import com.orbys.quizz.data.utils.ServerUtils
import com.orbys.quizz.domain.repositories.QuestionRepositoryImpl
import com.orbys.quizz.domain.repositories.UsersRepositoryImpl
import com.orbys.quizz.domain.usecases.question.AddAnswerUseCase
import com.orbys.quizz.domain.usecases.question.GetQuestionUseCase
import com.orbys.quizz.domain.usecases.question.GetTimerStateUseCase
import com.orbys.quizz.domain.usecases.question.IncAnswerUseCase
import com.orbys.quizz.domain.usecases.users.AddUserUseCase
import com.orbys.quizz.domain.usecases.users.GetUsersListUseCase
import com.orbys.quizz.domain.usecases.users.SetUserRespondedUseCase
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
    fun provideServerRepository() = HttpRepositoryImpl(
        GetQuestionUseCase(QuestionRepositoryImpl.getInstance()),
        IncAnswerUseCase(QuestionRepositoryImpl.getInstance()),
        AddAnswerUseCase(QuestionRepositoryImpl.getInstance()),
        GetTimerStateUseCase(QuestionRepositoryImpl.getInstance()),
        GetUsersListUseCase(UsersRepositoryImpl.getInstance()),
        AddUserUseCase(UsersRepositoryImpl.getInstance()),
        SetUserRespondedUseCase(UsersRepositoryImpl.getInstance())
    )

    @Provides
    @Singleton
    fun provideFileRepository(@ApplicationContext context: Context): IFileRepository = FileRepository.getInstance(context)

    @Provides
    @Singleton
    fun provideErrorHandler(@ApplicationContext context: Context) = ErrorHandler(context)

    @Provides
    @Singleton
    fun provideFileHandler(@ApplicationContext context: Context) = FileHandler(
        provideServerRepository(),
        context
    )

    @Provides
    @Singleton
    fun provideResponseHandler(@ApplicationContext context: Context) = ResponseHandler(
        provideServerRepository(),
        provideFileHandler(context),
    )

    @Provides
    @Singleton
    fun provideServerUtils() = ServerUtils(NetworkManager())

}