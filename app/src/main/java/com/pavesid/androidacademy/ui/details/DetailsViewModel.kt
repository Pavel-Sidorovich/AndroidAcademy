package com.pavesid.androidacademy.ui.details

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pavesid.androidacademy.data.details.Details
import com.pavesid.androidacademy.di.IODispatcher
import com.pavesid.androidacademy.repositories.MoviesRepository
import com.pavesid.androidacademy.utils.Resource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import timber.log.Timber

internal class DetailsViewModel @ViewModelInject constructor(
    private val repository: MoviesRepository,
    @IODispatcher private val dispatcher: CoroutineDispatcher
) : ViewModel() {

    private val _details = MutableLiveData< Resource<Details>>()
    val details: LiveData<Resource<Details>>
        get() = _details

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        _details.postValue(Resource.error(throwable.message.orEmpty(), null))
        Timber.d(throwable)
    }

    fun loadDetails(id: Int) {
        viewModelScope.launch(dispatcher + exceptionHandler) {
            _details.postValue(Resource.loading(null))
            val details = repository.getDetails(id)
            if (details != null) {
                _details.postValue(Resource.success(details))
            } else {
                _details.postValue(Resource.error("Value is null", null))
            }
        }
    }
}
