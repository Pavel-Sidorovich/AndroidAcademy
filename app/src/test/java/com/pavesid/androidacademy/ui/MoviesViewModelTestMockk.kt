package com.pavesid.androidacademy.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.pavesid.androidacademy.MainCoroutineRule
import com.pavesid.androidacademy.data.Movie
import com.pavesid.androidacademy.repositories.MoviesRemoteRepositoryTest
import com.pavesid.androidacademy.ui.UtilsViewModelTest.addMovieToRepository
import com.pavesid.androidacademy.ui.UtilsViewModelTest.addMoviesToRepository
import com.pavesid.androidacademy.ui.UtilsViewModelTest.listOfOneElement
import com.pavesid.androidacademy.ui.UtilsViewModelTest.updateMovieInViewModel
import com.pavesid.androidacademy.ui.UtilsViewModelTest.updatedListOfOneElement
import com.pavesid.androidacademy.utils.Resource
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.mockk.verifyOrder
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class MoviesViewModelTestMockk {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private val repository = MoviesRemoteRepositoryTest()

    @MockK
    lateinit var moviesObserverMockito: Observer<Resource<List<Movie>>>

    @Before
    fun before() {
        moviesObserverMockito = mockk()
        every { moviesObserverMockito.onChanged(any()) } answers {}
    }

    @Test
    fun `should return response is success when repository return data`() {
        val viewModel = MoviesViewModel(repository)
        viewModel.movies.observeForever(moviesObserverMockito)

        verifyOrder {
            moviesObserverMockito.onChanged(Resource.loading(null))
            moviesObserverMockito.onChanged(Resource.success(emptyList()))
        }
        viewModel.movies.removeObserver(moviesObserverMockito)
    }

    @Test
    fun `should return response is error when repository throw exception`() {
        val message = "Error"
        repository.setShouldReturnNetworkError(true)

        val viewModel = MoviesViewModel(repository)
        viewModel.movies.observeForever(moviesObserverMockito)

        verifyOrder {
            moviesObserverMockito.onChanged(Resource.loading(null))
            moviesObserverMockito.onChanged(Resource.error(message, null))
        }

        viewModel.movies.removeObserver(moviesObserverMockito)
    }

    @Test
    fun `should add movies to repository`() {
        val viewModel = MoviesViewModel(repository)
        viewModel.movies.observeForever(moviesObserverMockito)
        addMoviesToRepository(repository)

        verifyOrder {
            moviesObserverMockito.onChanged(Resource.loading(null))
            moviesObserverMockito.onChanged(Resource.success(listOfOneElement()))
        }

        viewModel.movies.removeObserver(moviesObserverMockito)
    }

    @Test
    fun `should add movie to repository`() {
        val viewModel = MoviesViewModel(repository)
        viewModel.movies.observeForever(moviesObserverMockito)
        addMovieToRepository(repository)

        verifyOrder {
            moviesObserverMockito.onChanged(Resource.loading(null))
            moviesObserverMockito.onChanged(Resource.success(listOfOneElement()))
        }

        viewModel.movies.removeObserver(moviesObserverMockito)
    }

    @Test
    fun `should update movie in repository`() {
        val viewModel = MoviesViewModel(repository)
        viewModel.movies.observeForever(moviesObserverMockito)
        addMoviesToRepository(repository)
        updateMovieInViewModel(viewModel)

        verifyOrder {
            moviesObserverMockito.onChanged(Resource.loading(null))
            moviesObserverMockito.onChanged(Resource.success(updatedListOfOneElement()))
        }

        viewModel.movies.removeObserver(moviesObserverMockito)
    }
}
