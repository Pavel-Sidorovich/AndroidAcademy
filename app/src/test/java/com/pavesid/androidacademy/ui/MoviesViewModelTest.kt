package com.pavesid.androidacademy.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.pavesid.androidacademy.MainCoroutineRule
import com.pavesid.androidacademy.data.Movie
import com.pavesid.androidacademy.repositories.MoviesRepository
import com.pavesid.androidacademy.utils.Resource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.doReturn
import org.mockito.Mockito.doThrow
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class MoviesViewModelTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    var coroutineDispatcher = TestCoroutineDispatcher()

    @Mock
    private lateinit var repository: MoviesRepository

    @Mock
    private lateinit var moviesObserver: Observer<Resource<List<Movie>>>

    @Test
    fun `response is success`() {
        runBlockingTest {
            doReturn(emptyList<Movie>())
                .`when`(repository)
                .getMovies()
            val viewModel = MoviesViewModel(repository, coroutineDispatcher)
            viewModel.movies.observeForever(moviesObserver)
            verify(repository).getMovies()
            verify(moviesObserver).onChanged(Resource.success(emptyList()))
            viewModel.movies.removeObserver(moviesObserver)
        }
    }

    @Test
    fun `response is error`() {
        runBlockingTest {
            val message = "Error"
            doThrow(RuntimeException(message))
                .`when`(repository)
                .getMovies()
            val viewModel = MoviesViewModel(repository, coroutineDispatcher)
            viewModel.movies.observeForever(moviesObserver)
            verify(repository).getMovies()
            verify(moviesObserver).onChanged(Resource.error(message, null))
            viewModel.movies.removeObserver(moviesObserver)
        }
    }
}
