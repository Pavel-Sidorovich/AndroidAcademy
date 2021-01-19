package com.pavesid.androidacademy.ui.movies

import androidx.annotation.MainThread
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pavesid.androidacademy.data.entities.MovieEntity
import com.pavesid.androidacademy.data.genres.Genre
import com.pavesid.androidacademy.data.movies.Movie
import com.pavesid.androidacademy.di.IODispatcher
import com.pavesid.androidacademy.repositories.MoviesRepository
import com.pavesid.androidacademy.utils.Resource
import java.util.Locale
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Job
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

    private var searchProgress = false

    private var moviesPage = 1

    private var currentGenre = Long.MIN_VALUE
    private var genresList = emptyList<Genre>()

    private var listOfMovies = mutableListOf<Movie>()

    private var currentJob: Job? = null
    private var debounceJob: Job? = null

    internal var searchQuery = ""
    private var searchPage = 1
    private var searchList = mutableListOf<Movie>()

    private var currentLocale = ""

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        _movies.postValue(Resource.error(throwable.message.orEmpty()))
        Timber.d(throwable)
    }

    private val exceptionHandlerGenres = CoroutineExceptionHandler { _, throwable ->
        _genres.postValue(Resource.error(throwable.message.orEmpty()))
        Timber.d(throwable)
    }

    fun init(reload: Boolean = false) {
        if (reload) {
            moviesPage = 1
            searchPage = 1
            reload()
        } else if (currentLocale != Locale.getDefault().toLanguageTag()) {
            reload()
        }
    }

    private fun reload() {
        currentLocale = Locale.getDefault().toLanguageTag()
        if (searchQuery != "") {
            searchMovies(searchQuery)
        } else {
            loadMovies(currentGenre, true)
        }
        loadGenres()
    }

    /**
     * Called when a new data packet needs to be received
     */
    @MainThread
    fun loadMovies(genre: Long = currentGenre, recreate: Boolean = false) {
        if (currentGenre != genre) {
            cancelAllJob()
            searchProgress = false
            genresList.forEach { it.isChecked = false }
            genresList.find { it.id == genre }?.isChecked = true
            _genres.postValue(Resource.success(genresList))
        }
        if (!searchProgress) {
            currentJob = viewModelScope.launch(dispatcher + exceptionHandler) {
                if (currentGenre != genre || recreate) {
                    moviesPage = 1
                    listOfMovies.clear()
                    currentGenre = genre
                }
                if (listOfMovies.isEmpty()) {
                    _movies.postValue(Resource.loading())
                }

                val movies = if (currentGenre == Long.MIN_VALUE) {
                    repository.getMovies(moviesPage)
                } else {
                    repository.getMoviesByGenre(id = genre, moviesPage)
                }

                if (movies.isNotEmpty()) {
                    listOfMovies.addAll(movies)
                    moviesPage++
                    _movies.postValue(
                        Resource.success(listOfMovies)
                    )
                }
            }
        } else {
            searchMovies(searchQuery, true)
        }
    }

    @MainThread
    fun searchMovies(query: String = searchQuery, needMore: Boolean = false) {
        if (searchQuery != query) {
            cancelAllJob()
        }
        debounceJob = viewModelScope.launch(dispatcher + exceptionHandler) {
            searchProgress = true
            if (!needMore) {
                searchList.clear()
                searchPage = 1
                searchQuery = query
            }
            if (searchQuery != "") {
                val movies = repository.searchMovies(searchQuery, searchPage)
                searchPage++
                searchList.addAll(movies)
                _movies.postValue(Resource.success(searchList))
            } else {
                searchProgress = false
                _movies.postValue(Resource.success(listOfMovies))
            }
        }
    }

    @MainThread
    fun loadGenres() {
        viewModelScope.launch(dispatcher + exceptionHandlerGenres) {
            genresList = repository.getGenres()
            _genres.postValue(Resource.success(genresList))
        }
    }

    fun updateLike(movieEntity: MovieEntity) {
        viewModelScope.launch(dispatcher + exceptionHandler) {
            repository.updateMovie(movieEntity)
        }
        listOfMovies.find { it.id == movieEntity.id }?.liked = movieEntity.liked
        searchList.find { it.id == movieEntity.id }?.liked = movieEntity.liked
    }

    private fun cancelAllJob() {
        currentJob?.cancel()
        debounceJob?.cancel()
    }
}
