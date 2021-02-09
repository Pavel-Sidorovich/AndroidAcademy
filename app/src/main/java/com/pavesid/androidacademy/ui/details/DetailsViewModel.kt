package com.pavesid.androidacademy.ui.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pavesid.androidacademy.data.details.DetailsWithCredits
import com.pavesid.androidacademy.di.IODispatcher
import com.pavesid.androidacademy.repositories.MoviesRepository
import com.pavesid.androidacademy.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.Locale
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import timber.log.Timber

@HiltViewModel
internal class DetailsViewModel @Inject constructor(
    private val repository: MoviesRepository,
    @IODispatcher private val dispatcher: CoroutineDispatcher
) : ViewModel() {

    private val _details = MutableLiveData<Resource<DetailsWithCredits>>()
    val detailsWithCredits: LiveData<Resource<DetailsWithCredits>>
        get() = _details

    private var currentLocale = ""
    private var currentId = Long.MIN_VALUE

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        _details.postValue(Resource.error(throwable.message.orEmpty()))
        Timber.d(throwable)
    }

    fun loadDetails(id: Long) {
        if (currentLocale != Locale.getDefault().toLanguageTag() || currentId != id) {
            viewModelScope.launch(dispatcher + exceptionHandler) {
                _details.postValue(Resource.loading())
                val details = repository.getDetails(id)
                _details.postValue(Resource.success(details))
            }
            currentLocale = Locale.getDefault().toLanguageTag()
            currentId = id
        }
    }
}
