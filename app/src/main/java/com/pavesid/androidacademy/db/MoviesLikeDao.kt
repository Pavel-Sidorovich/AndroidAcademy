package com.pavesid.androidacademy.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.pavesid.androidacademy.data.entities.MovieLikeEntity

@Dao
interface MoviesLikeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovieLikeEntity(movieLike: MovieLikeEntity)

    @Query("DELETE FROM movie_like_items WHERE movie_id = :id")
    suspend fun deleteMovieLikeEntity(id: Long)

    @Query("SELECT * FROM movie_like_items")
    suspend fun getAllMovieLikeEntities(): List<MovieLikeEntity>
}
