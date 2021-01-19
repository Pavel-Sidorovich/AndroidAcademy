package com.pavesid.androidacademy.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.pavesid.androidacademy.data.entities.MovieEntity

@Dao
interface MoviesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovie(movie: MovieEntity)

    @Query("DELETE FROM movie_items WHERE movie_id = :id")
    suspend fun deleteMovie(id: Long)

    @Query("SELECT * FROM movie_items")
    suspend fun getAllMoviesEntity(): List<MovieEntity>
}
