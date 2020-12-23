package com.pavesid.androidacademy.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.pavesid.androidacademy.MainCoroutineRule
import com.pavesid.androidacademy.data.Movie
import com.pavesid.androidacademy.repositories.MoviesRepository
import com.pavesid.androidacademy.utils.Resource
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class MoviesViewModelTestMockk {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    var coroutineDispatcher = TestCoroutineDispatcher()

    @MockK
    lateinit var repositoryMockito: MoviesRepository

    @MockK
    lateinit var moviesObserverMockito: Observer<Resource<List<Movie>>>

    @Before
    fun before() {
        repositoryMockito = mockk()
        moviesObserverMockito = mockk()
        every { moviesObserverMockito.onChanged(any()) } answers {}
    }

    @Test
    fun `response is success Mockk`() {
            coEvery {
                repositoryMockito.getMovies()
            } answers {
                emptyList()
            }
            val viewModel = MoviesViewModel(repositoryMockito, coroutineDispatcher)
            viewModel.movies.observeForever(moviesObserverMockito)
            coVerify { repositoryMockito.getMovies() }
            verify {
                moviesObserverMockito.onChanged(Resource.success(emptyList()))
            }
            viewModel.movies.removeObserver(moviesObserverMockito)
    }

    @Test
    fun `response is error Mockk`() {
            val message = "Error"

            coEvery {
                repositoryMockito.getMovies()
            } answers {
                throw java.lang.RuntimeException(message)
            }
            val viewModel = MoviesViewModel(repositoryMockito, coroutineDispatcher)
            viewModel.movies.observeForever(moviesObserverMockito)
            coVerify { repositoryMockito.getMovies() }
            verify {
                moviesObserverMockito.onChanged(Resource.error(message, null))
            }
            viewModel.movies.removeObserver(moviesObserverMockito)
        }
}
