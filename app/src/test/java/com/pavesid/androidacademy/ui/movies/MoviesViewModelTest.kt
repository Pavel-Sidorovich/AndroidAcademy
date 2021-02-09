package com.pavesid.androidacademy.ui.movies

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.pavesid.androidacademy.MainCoroutineRule
import com.pavesid.androidacademy.TestData
import com.pavesid.androidacademy.data.genres.Genre
import com.pavesid.androidacademy.data.movies.Movie
import com.pavesid.androidacademy.repositories.MoviesRepository
import com.pavesid.androidacademy.utils.Resource
import io.mockk.MockKAnnotations
import io.mockk.coEvery
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
class MoviesViewModelTest {

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val coroutineDispatcher = TestCoroutineDispatcher()

    private var repository: MoviesRepository = mockk()

    private val moviesObserverMockito: Observer<Resource<List<Movie>>> = mockk(relaxUnitFun = true)

    private val genresObserverMockito: Observer<Resource<List<Genre>>> = mockk(relaxUnitFun = true)

    private lateinit var viewModel: MoviesViewModel

    private val message = "error"

    @Before
    fun before() {
        MockKAnnotations.init(this)
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
        coEvery { repository.getGenresFromAPI() } returns TestData.getOneGenre()
        coEvery { repository.getGenresFromDB() } returns TestData.getOneGenre()

        viewModel.loadGenres()

        verify {
            genresObserverMockito.onChanged(Resource.success(TestData.getOneGenre()))
        }
    }

    @Test
    fun `should return response is success when repository return error genres from API and success genres from DB`() {
        coEvery { repository.getGenresFromAPI() } throws RuntimeException(message)
        coEvery { repository.getGenresFromDB() } returns TestData.getOneGenre()

        viewModel.loadGenres()

        verify {
            genresObserverMockito.onChanged(Resource.success(TestData.getOneGenre()))
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
        coEvery { repository.getMoviesFromAPI(any()) } returns TestData.getOneMovie()
        coEvery { repository.getMoviesFromDB() } returns TestData.getOneMovie()

        viewModel.loadMovies()

        verifyOrder {
            moviesObserverMockito.onChanged(Resource.loading())
            moviesObserverMockito.onChanged(Resource.success(TestData.getOneMovie()))
            moviesObserverMockito.onChanged(Resource.success(TestData.getOneMovie()))
        }
    }

    @Test
    fun `should return response is success when repository return error movies from API and success movies from DB`() {
        coEvery { repository.getMoviesFromDB() } returns TestData.getOneMovie()
        coEvery { repository.getMoviesFromAPI(1) } throws RuntimeException(message)

        viewModel.loadMovies()

        verifyOrder {
            moviesObserverMockito.onChanged(Resource.loading())
            moviesObserverMockito.onChanged(Resource.success(TestData.getOneMovie()))
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
        coEvery { repository.searchMoviesFromAPI(any()) } returns TestData.getOneMovie()

        viewModel.searchMovies("Smth")

        verify {
            moviesObserverMockito.onChanged(Resource.success(TestData.getOneMovie()))
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
        coEvery { repository.getMoviesFromDB() } returns TestData.getOneMovie()
        coEvery { repository.getMoviesFromAPI(1) } returns TestData.getOneMovie()
        coEvery { repository.getGenresFromDB() } returns TestData.getOneGenre()
        coEvery { repository.getGenresFromDB() } returns TestData.getOneGenre()

        viewModel.init(true)

        verifyOrder {
            moviesObserverMockito.onChanged(Resource.loading())
            moviesObserverMockito.onChanged(Resource.success(TestData.getOneMovie()))
            moviesObserverMockito.onChanged(Resource.success(TestData.getOneMovie()))
        }

        verify {
            genresObserverMockito.onChanged(Resource.success(TestData.getOneGenre()))
        }
    }
}
