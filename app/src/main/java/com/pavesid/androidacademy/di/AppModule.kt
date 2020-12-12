package com.pavesid.androidacademy.di

import com.pavesid.androidacademy.repositories.MoviesRemoteRepository
import com.pavesid.androidacademy.repositories.MoviesRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import kotlinx.serialization.ExperimentalSerializationApi

@ExperimentalSerializationApi
@Module
@InstallIn(SingletonComponent::class)
abstract class AppModule {

    @Singleton
    @Binds
    abstract fun bindMoviesRepository(
        moviesRemoteRepository: MoviesRemoteRepository
    ): MoviesRepository
}
