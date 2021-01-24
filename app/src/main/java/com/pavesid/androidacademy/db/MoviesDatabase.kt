package com.pavesid.androidacademy.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.pavesid.androidacademy.data.entities.GenreEntity
import com.pavesid.androidacademy.data.entities.MovieEntity
import com.pavesid.androidacademy.data.entities.MovieLikeEntity

@Database(
    entities = [MovieLikeEntity::class, MovieEntity::class, GenreEntity::class],
    version = 1,
    exportSchema = false
)
abstract class MoviesDatabase : RoomDatabase() {

    abstract fun moviesLikeDao(): MoviesLikeDao

    abstract fun moviesDao(): MoviesDao

    abstract fun genreDao(): GenreDao
}
