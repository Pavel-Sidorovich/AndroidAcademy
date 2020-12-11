package com.pavesid.androidacademy.di

import com.pavesid.androidacademy.repositories.MoviesRemoteRepository
import com.pavesid.androidacademy.repositories.MoviesRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import kotlinx.serialization.ExperimentalSerializationApi

@ExperimentalSerializationApi
@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideMoviesRepository() = MoviesRemoteRepository() as MoviesRepository
}
