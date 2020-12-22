package com.pavesid.androidacademy.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.Dispatchers

@Module
@InstallIn(SingletonComponent::class)
object DispatchersModule {

    @IODispatcher
    @Provides
    fun provideDispatchersIO() = Dispatchers.IO
}
