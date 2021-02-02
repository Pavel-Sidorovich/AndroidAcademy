package com.pavesid.androidacademy.ui.movies

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.pavesid.androidacademy.MainCoroutineRule
import com.pavesid.androidacademy.TestHelper
import com.pavesid.androidacademy.data.genres.Genre
import com.pavesid.androidacademy.data.movies.Movie
import com.pavesid.androidacademy.repositories.MoviesRepository
import com.pavesid.androidacademy.utils.Resource
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import io.mockk.verifyOrder
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class MoviesViewModelTest {

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val coroutineDispatcher = TestCoroutineDispatcher()

    @MockK
    lateinit var repository: MoviesRepository

    @MockK
    lateinit var moviesObserverMockito: Observer<Resource<List<Movie>>>
    @MockK
    lateinit var genresObserverMockito: Observer<Resource<List<Genre>>>

    private lateinit var viewModel: MoviesViewModel

    private val message = "error"

    @Before
    fun before() {
        MockKAnnotations.init(this)
        every { moviesObserverMockito.onChanged(any()) } answers {}
        viewModel = MoviesViewModel(repository, coroutineDispatcher)
        viewModel.movies.observeForever(moviesObserverMockito)
        viewModel.genres.observeForever(genresObserverMockito)
    }

    @After
    fun clear() {
        viewModel.movies.removeObserver(moviesObserverMockito)
        viewModel.genres.removeObserver(genresObserverMockito)
    }

    @Test
    fun `should return response is loading when repository return empty movies`() {
        coEvery { repository.getMoviesFromAPI(any()) } returns emptyList()
        coEvery { repository.getMoviesFromDB() } returns emptyList()

        viewModel.loadMovies()

        verify {
            moviesObserverMockito.onChanged(Resource.loading())
        }
    }

    @Test
    fun `should return response is success when repository return genres success from API and DB`() {
        coEvery { repository.getGenresFromAPI() } returns TestHelper.getOneGenre()
        coEvery { repository.getGenresFromDB() } returns TestHelper.getOneGenre()

        viewModel.loadGenres()

        verify {
            genresObserverMockito.onChanged(Resource.success(TestHelper.getOneGenre()))
        }
    }

    @Test
    fun `should return response is success when repository return error genres from API and success genres from DB`() {
        coEvery { repository.getGenresFromAPI() } throws RuntimeException(message)
        coEvery { repository.getGenresFromDB() } returns TestHelper.getOneGenre()

        viewModel.loadGenres()

        verify {
            genresObserverMockito.onChanged(Resource.success(TestHelper.getOneGenre()))
        }
    }

    @Test
    fun `should return response is error when repository return error genres from DB`() {
        coEvery { repository.getGenresFromDB() } throws RuntimeException(message)

        viewModel.loadGenres()

        verify {
            genresObserverMockito.onChanged(Resource.error(message))
        }
    }

    @Test
    fun `should return response is success when repository return success movies from API and DB`() {
        coEvery { repository.getMoviesFromAPI(any()) } returns TestHelper.getOneMovie()
        coEvery { repository.getMoviesFromDB() } returns TestHelper.getOneMovie()

        viewModel.loadMovies()

        verifyOrder {
            moviesObserverMockito.onChanged(Resource.loading())
            moviesObserverMockito.onChanged(Resource.success(TestHelper.getOneMovie()))
            moviesObserverMockito.onChanged(Resource.success(TestHelper.getOneMovie()))
        }
    }

    @Test
    fun `should return response is success when repository return error movies from API and success movies from DB`() {
        coEvery { repository.getMoviesFromDB() } returns TestHelper.getOneMovie()
        coEvery { repository.getMoviesFromAPI(1) } throws RuntimeException(message)

        viewModel.loadMovies()

        verifyOrder {
            moviesObserverMockito.onChanged(Resource.loading())
            moviesObserverMockito.onChanged(Resource.success(TestHelper.getOneMovie()))
        }
    }

    @Test
    fun `should return response is error when repository return error movies from DB`() {
        coEvery { repository.getMoviesFromDB() } throws RuntimeException(message)

        viewModel.loadMovies()

        verifyOrder {
            moviesObserverMockito.onChanged(Resource.loading())
            moviesObserverMockito.onChanged(Resource.error(message))
        }
    }

    @Test
    fun `should return response is success when repository return success movies from API in search`() {
        coEvery { repository.searchMoviesFromAPI(any()) } returns TestHelper.getOneMovie()

        viewModel.searchMovies("Smth")

        verify {
            moviesObserverMockito.onChanged(Resource.success(TestHelper.getOneMovie()))
        }
    }

    @Test
    fun `should return response is error when repository return error movies from API in search`() {
        coEvery { repository.searchMoviesFromAPI(any()) } throws RuntimeException(message)

        viewModel.searchMovies("Smth")

        verify {
            moviesObserverMockito.onChanged(Resource.error(message))
        }
    }

    @Test
    fun `should return response is success when reload`() {
        coEvery { repository.getMoviesFromDB() } returns TestHelper.getOneMovie()
        coEvery { repository.getMoviesFromAPI(1) } returns TestHelper.getOneMovie()
        coEvery { repository.getGenresFromDB() } returns TestHelper.getOneGenre()
        coEvery { repository.getGenresFromDB() } returns TestHelper.getOneGenre()

        viewModel.init(true)

        verifyOrder {
            moviesObserverMockito.onChanged(Resource.loading())
            moviesObserverMockito.onChanged(Resource.success(TestHelper.getOneMovie()))
            moviesObserverMockito.onChanged(Resource.success(TestHelper.getOneMovie()))
        }

        verify {
            genresObserverMockito.onChanged(Resource.success(TestHelper.getOneGenre()))
        }
    }
}
