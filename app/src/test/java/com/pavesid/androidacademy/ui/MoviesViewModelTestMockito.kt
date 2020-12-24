package com.pavesid.androidacademy.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.pavesid.androidacademy.MainCoroutineRule
import com.pavesid.androidacademy.data.Movie
import com.pavesid.androidacademy.repositories.MoviesRemoteRepositoryTest
import com.pavesid.androidacademy.utils.Resource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class MoviesViewModelTestMockito {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private val repository = MoviesRemoteRepositoryTest()

    private val coroutineDispatcher = TestCoroutineDispatcher()

    @Mock
    private lateinit var moviesObserverMockito: Observer<Resource<List<Movie>>>

    @Test
    fun `response is success Mockito`() {
        runBlockingTest {
            repository.setShouldReturnNetworkError(false)

            val viewModel = MoviesViewModel(repository, coroutineDispatcher)
            viewModel.movies.observeForever(moviesObserverMockito)

            verify(moviesObserverMockito).onChanged(Resource.success(emptyList()))

            viewModel.movies.removeObserver(moviesObserverMockito)
        }
    }

    @Test
    fun `response is error Mockito`() {
        runBlockingTest {
            repository.setShouldReturnNetworkError(true)

            val message = "Error"
            val viewModel = MoviesViewModel(repository, coroutineDispatcher)
            viewModel.movies.observeForever(moviesObserverMockito)

            verify(moviesObserverMockito).onChanged(Resource.error(message, null))

            viewModel.movies.removeObserver(moviesObserverMockito)
        }
    }
}
