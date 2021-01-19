package com.pavesid.androidacademy.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.pavesid.androidacademy.data.movies.Movie

@Database(
    entities = [Movie::class],
    version = 1,
    exportSchema = false
)
abstract class MoviesDatabase : RoomDatabase() {

    abstract fun moviesDao(): MoviesDao
}
