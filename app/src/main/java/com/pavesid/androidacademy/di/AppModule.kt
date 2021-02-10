package com.pavesid.androidacademy.di

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import androidx.room.Room
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequest
import androidx.work.PeriodicWorkRequestBuilder
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.pavesid.androidacademy.BuildConfig
import com.pavesid.androidacademy.db.MoviesDatabase
import com.pavesid.androidacademy.db.genries.GenreDao
import com.pavesid.androidacademy.db.likes.MoviesLikeDao
import com.pavesid.androidacademy.db.movies.MoviesDao
import com.pavesid.androidacademy.repositories.MoviesRepository
import com.pavesid.androidacademy.repositories.MoviesRepositoryImpl
import com.pavesid.androidacademy.retrofit.CacheControlInterceptor
import com.pavesid.androidacademy.retrofit.MoviesApi
import com.pavesid.androidacademy.retrofit.MoviesApiQueryInterceptor
import com.pavesid.androidacademy.services.MoviesWorker
import com.pavesid.androidacademy.utils.NetworkMonitor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import java.io.File
import java.util.concurrent.TimeUnit
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

    @Singleton
    @Provides
    fun provideJson(): Json = Json { ignoreUnknownKeys = true }

    @Singleton
    @Provides
    fun provideClient(
        @ApplicationContext context: Context
    ): OkHttpClient {
        val httpCacheDirectory = File(context.cacheDir, "responses")
        val cacheSize = 10L * 1024 * 1024
        val cache = Cache(httpCacheDirectory, cacheSize)

        return OkHttpClient().newBuilder()
            .addInterceptor(MoviesApiQueryInterceptor())
            .addInterceptor(HttpLoggingInterceptor().setLevel(if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE))
            .addInterceptor(CacheControlInterceptor())
            .cache(cache)
            .readTimeout(15, TimeUnit.SECONDS)
            .build()
    }

    @Singleton
    @Provides
    fun providesApi(
        client: OkHttpClient,
        jsonFormat: Json
    ): MoviesApi = Retrofit.Builder()
        .client(client)
        .baseUrl("https://api.themoviedb.org/3/")
        .addConverterFactory(jsonFormat.asConverterFactory("application/json".toMediaType()))
        .build()
        .create(MoviesApi::class.java)

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
    fun provideMoviesLikeDao(
        database: MoviesDatabase
    ) = database.moviesLikeDao()

    @Singleton
    @Provides
    fun provideMoviesDao(
        database: MoviesDatabase
    ) = database.moviesDao()

    @Singleton
    @Provides
    fun provideGenreDao(
        database: MoviesDatabase
    ) = database.genreDao()

    @Singleton
    @Provides
    fun providesMoviesRepository(
        moviesLikeDao: MoviesLikeDao,
        moviesDao: MoviesDao,
        genreDao: GenreDao,
        api: MoviesApi
    ) = MoviesRepositoryImpl(moviesLikeDao, moviesDao, genreDao, api) as MoviesRepository

    @Singleton
    @Provides
    fun provideNetworkMonitor(
        @ApplicationContext context: Context
    ): NetworkMonitor = NetworkMonitor(context)

    @Singleton
    @Provides
    fun provideConstraints(): Constraints =
        Constraints.Builder().setRequiredNetworkType(NetworkType.UNMETERED)
            .setRequiresCharging(true).build()

    @Singleton
    @Provides
    fun providePeriodicWorkRequest(constraints: Constraints): PeriodicWorkRequest =
        PeriodicWorkRequestBuilder<MoviesWorker>(8, TimeUnit.HOURS)
            .setConstraints(constraints)
            .build()

    private const val DATABASE_NAME = "movies_db"
}
