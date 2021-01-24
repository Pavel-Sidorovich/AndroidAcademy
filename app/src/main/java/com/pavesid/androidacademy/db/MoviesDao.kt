package com.pavesid.androidacademy.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.pavesid.androidacademy.data.entities.MovieEntity

@Dao
interface MoviesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllMovieEntities(movie: List<MovieEntity>)

    @Query("DELETE FROM movie_items WHERE movie_id = :id")
    suspend fun deleteMovieEntity(id: Long)

    @Query("SELECT * FROM movie_items ORDER BY popularity DESC")
    suspend fun getAllMovieEntities(): List<MovieEntity>

    @Query("SELECT * FROM movie_items WHERE genre_ids LIKE :id ORDER BY popularity DESC")
    suspend fun getMoviesByGenre(id: String): List<MovieEntity>

    @Query("DELETE FROM movie_items")
    suspend fun deleteAllMovieEntities()
}
