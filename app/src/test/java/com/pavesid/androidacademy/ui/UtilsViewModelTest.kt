package com.pavesid.androidacademy.ui

import com.pavesid.androidacademy.data.Movie
import com.pavesid.androidacademy.repositories.MoviesRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest

@ExperimentalCoroutinesApi
object UtilsViewModelTest {

    internal fun addMoviesToRepository(repository: MoviesRepository) {
        runBlockingTest {
            repository.insertMovies(
                listOfOneElement()
            )
        }
    }

    internal fun addMovieToRepository(repository: MoviesRepository) {
        runBlockingTest {
            repository.insertMovie(
                listOfOneElement()[0]
            )
        }
    }

    internal fun updateMovieInViewModel(viewModel: MoviesViewModel) {
        runBlockingTest {
            viewModel.updateMovies(
                listOfOneElement()[0]
            )
        }
    }

    internal fun listOfOneElement() = listOf(
        Movie(
            id = 0,
            title = "1",
            overview = "1",
            poster = "1",
            backdrop = "1",
            ratings = 1f,
            numberOfRatings = 1,
            minimumAge = 1,
            runtime = 1,
            genres = emptyList(),
            actors = emptyList(),
            liked = false
        )
    )

    internal fun updatedListOfOneElement() = listOf(
        Movie(
            id = 0,
            title = "1",
            overview = "1",
            poster = "1",
            backdrop = "1",
            ratings = 1f,
            numberOfRatings = 1,
            minimumAge = 1,
            runtime = 1,
            genres = emptyList(),
            actors = emptyList(),
            liked = true
        )
    )
}
