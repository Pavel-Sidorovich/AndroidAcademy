package com.pavesid.androidacademy.di

import com.pavesid.androidacademy.repositories.MoviesRepository
import com.pavesid.androidacademy.repositories.remote.MoviesRemoteRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideMoviesRepository() =
        MoviesRemoteRepository() as MoviesRepository // FakeAssetsRepository(context) as MoviesRepository
}
