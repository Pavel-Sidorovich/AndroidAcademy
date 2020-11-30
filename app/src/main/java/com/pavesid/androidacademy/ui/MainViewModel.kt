package com.pavesid.androidacademy.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pavesid.androidacademy.data.local.FakeRepository
import com.pavesid.androidacademy.data.local.model.Movie
import com.pavesid.androidacademy.data.local.model.MoviePreview
import com.pavesid.androidacademy.utils.Resource
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainViewModel() : ViewModel() {

    private val _moviesPreview = MutableLiveData<Resource<List<MoviePreview>>>()
    val moviesPreview: LiveData<Resource<List<MoviePreview>>> = _moviesPreview

    private val _movie = MutableLiveData<Resource<Movie>>()
    val movie: LiveData<Resource<Movie>> = _movie

    init {
        viewModelScope.launch {
            _moviesPreview.postValue(Resource.loading(null))
            delay(2000L)
            _moviesPreview.postValue(Resource.success(FakeRepository.getAllPreviews()))
        }
    }

    fun getMovie(id: Int) {
        viewModelScope.launch {
            _movie.postValue(Resource.loading(null))
            delay(2000)
            _movie.postValue(Resource.success(FakeRepository.getMovieById(id)))
        }
    }
}
