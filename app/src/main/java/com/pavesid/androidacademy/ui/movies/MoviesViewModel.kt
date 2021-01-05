package com.pavesid.androidacademy.ui.movies

import androidx.annotation.MainThread
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pavesid.androidacademy.data.movies.Movie
import com.pavesid.androidacademy.di.IODispatcher
import com.pavesid.androidacademy.repositories.MoviesRepository
import com.pavesid.androidacademy.utils.Resource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import timber.log.Timber

internal class MoviesViewModel @ViewModelInject constructor(
    private val repository: MoviesRepository,
    @IODispatcher private val dispatcher: CoroutineDispatcher
) : ViewModel() {

    private val _movies = MutableLiveData<Resource<List<Movie>>>()
    val movies: LiveData<Resource<List<Movie>>> = _movies

    private var page = 1
    private var isLoading = false

    private var list = mutableListOf<Movie>()

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        _movies.postValue(Resource.error(throwable.message.orEmpty(), null))
        Timber.d(throwable)
    }

    init {
        loadMovies()
    }

    /**
     * Called when a new data packet needs to be received
     */
    @MainThread
    fun loadMovies() {
        if (!isLoading) {
            viewModelScope.launch(dispatcher + exceptionHandler) {
                isLoading = true
                if (list.isEmpty()) {
                    _movies.postValue(Resource.loading(null))
                }
                val movies = repository.getMovies(page)
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

    fun updateMovies(movie: Movie) {
        viewModelScope.launch(dispatcher + exceptionHandler) {
            val index = list.indexOf(movie)
            list[index].liked = !list[index].liked
            repository.updateMovie(list[index])
            _movies.postValue(
                Resource.success(list)
            )
        }
    }
}
