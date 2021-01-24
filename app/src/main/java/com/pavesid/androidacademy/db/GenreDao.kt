package com.pavesid.androidacademy.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.pavesid.androidacademy.data.entities.GenreEntity

@Dao
interface GenreDao {

    @Insert
    suspend fun insertAllGenreEntities(genres: List<GenreEntity>)

    @Query("SELECT * FROM genre_items")
    suspend fun getAllGenreEntities(): List<GenreEntity>

    @Query("DELETE FROM genre_items")
    suspend fun deleteAllGenreEntities()
}
