package com.pavesid.androidacademy.ui

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pavesid.androidacademy.data.Movie
import com.pavesid.androidacademy.repositories.MoviesRepository
import com.pavesid.androidacademy.utils.Resource
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber

class MoviesViewModel @ViewModelInject constructor(
    private val repository: MoviesRepository
) : ViewModel() {

    private val _movies = MutableLiveData<Resource<List<Movie>>>()
    val movies: LiveData<Resource<List<Movie>>> = _movies

    private var list = emptyList<Movie>()

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        _movies.postValue(Resource.error(throwable.message ?: "", null))
        Timber.d(throwable)
    }

    init {
        viewModelScope.launch(Dispatchers.IO + exceptionHandler) {
            _movies.postValue(Resource.loading(null))
            list = repository.getMoviesFromDB().let {
                if (it.isEmpty()) {
                    val list = repository.getMovies()
                    repository.insertMovies(list)
                    return@let list
                } else it
            }
            _movies.postValue(
                Resource.success(list)
            )
        }
    }

    fun updateMovies(movie: Movie) {
        viewModelScope.launch(Dispatchers.IO + exceptionHandler) {
            val index = list.indexOf(movie)
            list[index].liked = !list[index].liked
            repository.updateMovie(list[index])
            _movies.postValue(
                Resource.success(list)
            )
        }
    }
}
