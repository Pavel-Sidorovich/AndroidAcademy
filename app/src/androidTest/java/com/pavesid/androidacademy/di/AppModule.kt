package com.pavesid.androidacademy.di

import android.content.Context
import com.pavesid.androidacademy.repositories.FakeAssetsRepository
import com.pavesid.androidacademy.repositories.MoviesRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import kotlinx.serialization.ExperimentalSerializationApi

@ExperimentalSerializationApi
@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideMoviesRepository(
        @ApplicationContext context: Context
    ) = FakeAssetsRepository(context) as MoviesRepository
}
