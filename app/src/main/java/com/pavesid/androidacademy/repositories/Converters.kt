package com.pavesid.androidacademy.repositories

import androidx.room.TypeConverter

class Converters {
    @TypeConverter
    fun fromList(ids: List<Long>): String = ids.joinToString()

    @TypeConverter
    fun toList(data: String): List<Long> = data.split(",").map { it.toLong() }
}
