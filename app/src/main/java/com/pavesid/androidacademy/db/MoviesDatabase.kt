package com.pavesid.androidacademy.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.pavesid.androidacademy.db.genries.GenreDao
import com.pavesid.androidacademy.db.genries.GenreEntity
import com.pavesid.androidacademy.db.likes.MovieLikeEntity
import com.pavesid.androidacademy.db.likes.MoviesLikeDao
import com.pavesid.androidacademy.db.movies.MovieEntity
import com.pavesid.androidacademy.db.movies.MoviesDao

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
