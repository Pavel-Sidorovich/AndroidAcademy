package com.pavesid.androidacademy.ui.movies

import androidx.annotation.MainThread
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pavesid.androidacademy.data.genres.Genre
import com.pavesid.androidacademy.data.movies.Movie
import com.pavesid.androidacademy.di.IODispatcher
import com.pavesid.androidacademy.repositories.MoviesRepository
import com.pavesid.androidacademy.utils.Resource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber

internal class MoviesViewModel @ViewModelInject constructor(
    private val repository: MoviesRepository,
    @IODispatcher private val dispatcher: CoroutineDispatcher
) : ViewModel() {

    private val _movies = MutableLiveData<Resource<List<Movie>>>()
    val movies: LiveData<Resource<List<Movie>>> = _movies

    private val _genres = MutableLiveData<Resource<List<Genre>>>()
    val genres: LiveData<Resource<List<Genre>>> = _genres

    private var page = 1
    private var isLoading = false
    private var currentGenre = -1

    private var list = mutableListOf<Movie>()

    private var currentJob: Job? = null
    private var debounceJob: Job? = null

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        _movies.postValue(Resource.error(throwable.message.orEmpty(), null))
        Timber.d(throwable)
    }

    private val exceptionHandlerGenres = CoroutineExceptionHandler { _, throwable ->
        _genres.postValue(Resource.error(throwable.message.orEmpty(), null))
        Timber.d(throwable)
    }

    init {
        loadMovies(currentGenre)
        loadGenres()
    }

    /**
     * Called when a new data packet needs to be received
     */
    @MainThread
    fun loadMovies(genre: Int = currentGenre) {
        if (currentGenre != genre) {
            cancelAllJob()
            isLoading = false
        }
        if (!isLoading) {
            currentJob = viewModelScope.launch(dispatcher + exceptionHandler) {
                isLoading = true
                if (currentGenre != genre) {
                    page = 1
                    list.clear()
                    currentGenre = genre
                }
                if (list.isEmpty()) {
                    _movies.postValue(Resource.loading(null))
                }
                val movies = repository.getMoviesByGenre(id = genre, page)

                if (movies.isNotEmpty()) {
                    list.addAll(movies)
                    page++
                    _movies.postValue(
                        Resource.success(list)
                    )
                }
                isLoading = false
            }
        }
    }

    @MainThread
    fun searchMovies(query: String) {
        isLoading = false
        if (!isLoading) {
            cancelAllJob()
            debounceJob = viewModelScope.launch(dispatcher + exceptionHandler) {
                delay(500)
                if (query != "") {
                    val movies = repository.searchMovies(query, 1)
                    _movies.postValue(Resource.success(movies))
                } else {
                    _movies.postValue(Resource.success(list))
                }
            }
        }
    }

    @MainThread
    fun loadGenres() {
        genres.value?.data ?: viewModelScope.launch(dispatcher + exceptionHandlerGenres) {
            val genres = repository.getGenres()
            val list = mutableListOf<Genre>()
            list.add(Genre(-1, "All", true))
            list.addAll(genres)
            _genres.postValue(Resource.success(list))
        }
    }

    private fun cancelAllJob() {
        currentJob?.cancel()
        debounceJob?.cancel()
    }
}
