package com.pavesid.androidacademy.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.pavesid.androidacademy.MainCoroutineRule
import com.pavesid.androidacademy.data.Movie
import com.pavesid.androidacademy.repositories.MoviesRemoteRepositoryTest
import com.pavesid.androidacademy.utils.Resource
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

    private val coroutineDispatcher = TestCoroutineDispatcher()

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
        repository.setShouldReturnNetworkError(false)

        val viewModel = MoviesViewModel(repository, coroutineDispatcher)
        viewModel.movies.observeForever(moviesObserverMockito)

        verify {
            moviesObserverMockito.onChanged(Resource.success(emptyList()))
        }
        viewModel.movies.removeObserver(moviesObserverMockito)
    }

    @Test
    fun `should return response is error when repository throw exception`() {
        val message = "Error"
        repository.setShouldReturnNetworkError(true)

        val viewModel = MoviesViewModel(repository, coroutineDispatcher)
        viewModel.movies.observeForever(moviesObserverMockito)

        verify {
            moviesObserverMockito.onChanged(Resource.error(message, null))
        }

        viewModel.movies.removeObserver(moviesObserverMockito)
    }
}
