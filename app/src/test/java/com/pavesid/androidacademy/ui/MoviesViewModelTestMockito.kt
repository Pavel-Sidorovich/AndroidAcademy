package com.pavesid.androidacademy.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.google.common.truth.Truth.assertThat
import com.pavesid.androidacademy.MainCoroutineRule
import com.pavesid.androidacademy.data.Movie
import com.pavesid.androidacademy.repositories.MoviesRemoteRepositoryTest
import com.pavesid.androidacademy.utils.Resource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentCaptor
import org.mockito.Captor
import org.mockito.Mock
import org.mockito.Mockito.times
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

    private lateinit var viewModel: MoviesViewModel

    @Captor
    private lateinit var capt: ArgumentCaptor<Resource<List<Movie>>>

    @Mock
    private lateinit var moviesObserverMockito: Observer<Resource<List<Movie>>>

    @After
    fun after() {
        viewModel.movies.removeObserver(moviesObserverMockito)
    }

    @Test
    fun `should return response is success when repository return data`() = runBlockingTest {
        repository.setShouldReturnNetworkError(false)

        viewModel = MoviesViewModel(repository, coroutineDispatcher)
        viewModel.movies.observeForever(moviesObserverMockito)
        viewModel.getMovies()
        verify(moviesObserverMockito, times(2)).onChanged(capt.capture())

        val values = capt.allValues
        assertThat(values[0]).isEqualTo(Resource.loading(null))
    }

    @Test
    fun `should return response is error when repository throw exception`() = runBlockingTest {
        repository.setShouldReturnNetworkError(true)

        val message = "Error"
        viewModel = MoviesViewModel(repository, coroutineDispatcher)
        viewModel.movies.observeForever(moviesObserverMockito)
        viewModel.getMovies()

        verify(moviesObserverMockito).onChanged(Resource.loading(null))
        verify(moviesObserverMockito).onChanged(Resource.error(message, null))
    }
}
