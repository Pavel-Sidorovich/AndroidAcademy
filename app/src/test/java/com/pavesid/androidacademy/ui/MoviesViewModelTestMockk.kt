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
import io.mockk.verify
import io.mockk.verifyOrder
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class MoviesViewModelTestMockk {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private val coroutineDispatcher = TestCoroutineDispatcher()

    private val repository = MoviesRemoteRepositoryTest()

    private lateinit var viewModel: MoviesViewModel

    @MockK
    lateinit var moviesObserverMockito: Observer<Resource<List<Movie>>>

    @Before
    fun before() {
        moviesObserverMockito = mockk()
        every { moviesObserverMockito.onChanged(any()) } answers { }
    }

    @After
    fun after() {
        viewModel.movies.removeObserver(moviesObserverMockito)
    }

    @Test
    fun `should return response is success when repository return data`() {
        repository.setShouldReturnNetworkError(false)

        viewModel = MoviesViewModel(repository, coroutineDispatcher)
        viewModel.movies.observeForever(moviesObserverMockito)
        viewModel.getMovies()

        verifyOrder {
            moviesObserverMockito.onChanged(Resource.loading(null))
            moviesObserverMockito.onChanged(Resource.success(emptyList()))
        }
    }

    @Test
    fun `should return response is error when repository throw exception`() {
        val message = "Error"
        repository.setShouldReturnNetworkError(true)

        viewModel = MoviesViewModel(repository, coroutineDispatcher)
        viewModel.movies.observeForever(moviesObserverMockito)
        viewModel.getMovies()

        verifyOrder {
            moviesObserverMockito.onChanged(Resource.loading(null))
            moviesObserverMockito.onChanged(Resource.error(message, null))
        }
    }

    @Test
    fun `should add movies to repository`() {
        viewModel = MoviesViewModel(repository, coroutineDispatcher)
        viewModel.movies.observeForever(moviesObserverMockito)
        addMoviesToRepository(repository)
        viewModel.getMovies()

        verifyOrder {
            moviesObserverMockito.onChanged(Resource.loading(null))
            moviesObserverMockito.onChanged(Resource.success(listOfOneElement()))
        }
    }

    @Test
    fun `should add movie to repository`() {
        viewModel = MoviesViewModel(repository, coroutineDispatcher)
        viewModel.movies.observeForever(moviesObserverMockito)
        addMovieToRepository(repository)
        viewModel.getMovies()

        verifyOrder {
            moviesObserverMockito.onChanged(Resource.loading(null))
            moviesObserverMockito.onChanged(Resource.success(listOfOneElement()))
        }
    }

    @Test
    fun `should update movie in repository`() {
        viewModel = MoviesViewModel(repository, coroutineDispatcher)
        viewModel.movies.observeForever(moviesObserverMockito)
        viewModel.getMovies()
        addMoviesToRepository(repository)
        updateMovieInViewModel(viewModel)

        verify {
            moviesObserverMockito.onChanged(Resource.success(updatedListOfOneElement()))
        }
    }
}
