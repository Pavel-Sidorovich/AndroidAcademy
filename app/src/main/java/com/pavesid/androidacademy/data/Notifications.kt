package com.pavesid.androidacademy.data

import com.pavesid.androidacademy.db.movies.MovieEntity

interface Notifications {
    fun initialize()
    fun showNotification(movie: MovieEntity)
    fun dismissNotification(movieId: Long)
}
