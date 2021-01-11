package com.pavesid.androidacademy.di

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import androidx.room.Room
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.pavesid.androidacademy.App
import com.pavesid.androidacademy.db.MoviesDao
import com.pavesid.androidacademy.db.MoviesDatabase
import com.pavesid.androidacademy.repositories.MoviesRepository
import com.pavesid.androidacademy.repositories.MoviesRepositoryImpl
import com.pavesid.androidacademy.retrofit.CacheControlInterceptor
import com.pavesid.androidacademy.retrofit.MoviesApi
import com.pavesid.androidacademy.retrofit.MoviesApiQueryInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import java.io.File
import javax.inject.Singleton
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.Cache
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit

@ExperimentalSerializationApi
@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    var api: MoviesApi

    init {
        val jsonFormat = Json { ignoreUnknownKeys = true }

        val httpCacheDirectory = File(App.thisCacheDir, "responses")
        val cacheSize = 10L * 1024 * 1024
        val cache = Cache(httpCacheDirectory, cacheSize)

        val client = OkHttpClient().newBuilder()
            .addInterceptor(MoviesApiQueryInterceptor())
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .addInterceptor(CacheControlInterceptor())
            .cache(cache)
            .build()

        val retrofit: Retrofit =
            Retrofit.Builder()
                .client(client)
                .baseUrl("https://api.themoviedb.org/3/")
                .addConverterFactory(jsonFormat.asConverterFactory("application/json".toMediaType()))
                .build()

        api = retrofit.create(MoviesApi::class.java)
    }

    @Singleton
    @Provides
    fun provideSharedPreferences(
        @ApplicationContext applicationContext: Context
    ): SharedPreferences = PreferenceManager.getDefaultSharedPreferences(applicationContext)

    @Singleton
    @Provides
    fun provideMoviesDatabase(
        @ApplicationContext context: Context
    ) = Room.databaseBuilder(context, MoviesDatabase::class.java, DATABASE_NAME).build()

    @Singleton
    @Provides
    fun provideMoviesDao(
        database: MoviesDatabase
    ) = database.moviesDao()

    @Singleton
    @Provides
    fun providesShoppingRepository(
        dao: MoviesDao
    ) = MoviesRepositoryImpl(dao, api) as MoviesRepository

    private const val DATABASE_NAME = "movies_db"
}
