package com.orbys.quizz.di

import android.content.Context
import com.orbys.quizz.core.managers.NetworkManager
import com.orbys.quizz.data.controllers.handlers.FileHandler
import com.orbys.quizz.data.controllers.handlers.ResponseHandler
import com.orbys.quizz.data.repositories.FileRepository
import com.orbys.quizz.data.repositories.HttpRepositoryImpl
import com.orbys.quizz.data.repositories.IFileRepository
import com.orbys.quizz.data.utils.ServerUtils
import com.orbys.quizz.domain.repositories.IQuestionRepository
import com.orbys.quizz.domain.repositories.IUsersRepository
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
    fun provideServerRepository() = HttpRepositoryImpl(
        GetQuestionUseCase(provideQuestionRepository()),
        IncAnswerUseCase(provideQuestionRepository()),
        AddAnswerUseCase(provideQuestionRepository()),
        GetTimerStateUseCase(provideQuestionRepository()),
        GetUsersListUseCase(provideUsersRepository()),
        AddUserUseCase(provideUsersRepository()),
        SetUserRespondedUseCase(provideUsersRepository())
    )

    @Provides
    @Singleton
    fun provideFileRepository(@ApplicationContext context: Context): IFileRepository = FileRepository.getInstance(context)

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