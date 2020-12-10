package com.pavesid.androidacademy.di

import android.content.Context
import com.pavesid.androidacademy.R
import com.pavesid.androidacademy.ui.movies.MoviesItemDecoration
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.FragmentComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.FragmentScoped

@Module
@InstallIn(FragmentComponent::class)
object MoviesModule {

    @Provides
    @FragmentScoped
    internal fun provideDecoration(
        @ApplicationContext context: Context
    ) = MoviesItemDecoration(
        spaceSize = context.resources.getDimensionPixelSize(R.dimen.spacing_normal_16)
    )
}
