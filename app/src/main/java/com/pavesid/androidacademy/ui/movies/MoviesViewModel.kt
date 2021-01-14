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

    private var page = 1

    private var currentGenre = -1

    private var list = mutableListOf<Movie>()

    private var currentJob: Job? = null
    private var debounceJob: Job? = null

    private var currentQuery = ""
    private var searchPage = 1
    private var searchList = mutableListOf<Movie>()

    private var currentLocale = ""

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        _movies.postValue(Resource.error(throwable.message.orEmpty(), null))
        Timber.d(throwable)
    }

    private val exceptionHandlerGenres = CoroutineExceptionHandler { _, throwable ->
        _genres.postValue(Resource.error(throwable.message.orEmpty(), null))
        Timber.d(throwable)
    }

    fun init() {
        if (currentLocale != Locale.getDefault().toLanguageTag()) {
            currentLocale = Locale.getDefault().toLanguageTag()
            if (currentQuery != "") {
                searchMovies(currentQuery)
            } else {
                loadMovies(currentGenre, true)
            }
            loadGenres()
        }
    }

    /**
     * Called when a new data packet needs to be received
     */
    @MainThread
    fun loadMovies(genre: Int = currentGenre, recreate: Boolean = false) {
        if (currentGenre != genre) {
            cancelAllJob()
            searchProgress = false
        }
        if (!searchProgress) {
            currentJob = viewModelScope.launch(dispatcher + exceptionHandler) {
                if (currentGenre != genre || recreate) {
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
            }
        } else {
            searchMovies(currentQuery, true)
        }
    }

    @MainThread
    fun searchMovies(query: String = currentQuery, needMore: Boolean = false) {
        if (currentQuery != query) {
            cancelAllJob()
        }
        debounceJob = viewModelScope.launch(dispatcher + exceptionHandler) {
            searchProgress = true
            if (!needMore) {
                searchList.clear()
                searchPage = 1
                currentQuery = query
            }
            if (currentQuery != "") {
                val movies = repository.searchMovies(currentQuery, searchPage)
                searchPage++
                searchList.addAll(movies)
                _movies.postValue(Resource.success(searchList))
            } else {
                searchProgress = false
                _movies.postValue(Resource.success(list))
            }
        }
    }

    @MainThread
    fun loadGenres() {
        viewModelScope.launch(dispatcher + exceptionHandlerGenres) {
            val genres = repository.getGenres()
//            val list = mutableListOf<Genre>()
//            list.add(Genre(-1, context.getString(R.string.all), true))
//            list.addAll(genres)
            _genres.postValue(Resource.success(genres))
        }
    }

    private fun cancelAllJob() {
        currentJob?.cancel()
        debounceJob?.cancel()
    }
}
