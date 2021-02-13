package com.pavesid.androidacademy

import com.pavesid.androidacademy.data.details.Details
import com.pavesid.androidacademy.data.details.DetailsWithCredits
import com.pavesid.androidacademy.data.genres.Genre
import com.pavesid.androidacademy.data.movies.Movie

object TestData {

    fun getOneMovie(): List<Movie> = listOf(
        Movie(
            id = 0,
            title = "",
            overview = "",
            poster = "",
            backdrop = "",
            ratings = 0f,
            numberOfRatings = 0,
            minimumAge = 0,
            runtime = 0,
            genres = emptyList(),
            liked = false
        )
    )

    fun getOneGenre() = listOf(
        Genre(
            0, ""
        )
    )

    fun getDetails() = DetailsWithCredits(
        Details(
            id = 1L,
            title = "",
            overview = "",
            poster = "",
            backdrop = "",
            genres = emptyList(),
            ratings = 0f,
            numberOfRatings = 0,
            adult = true,
            runtime = 0
        ),
        emptyList(),
        emptyList()
    )
}
