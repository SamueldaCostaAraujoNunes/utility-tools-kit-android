package com.samuelnunes.presentation.screens.cats_detail

import androidx.lifecycle.*
import com.samuelnunes.data.dto.response.error.NotFoundError
import com.samuelnunes.domain.entity.Breed
import com.samuelnunes.domain.use_case.GetBreedUseCase
import com.samuelnunes.utility_tool_kit.domain.Resource
import com.samuelnunes.utility_tool_kit.network.NetworkConnectivityObserver
import com.samuelnunes.utility_tool_kit.utils.UiText
import com.samuelnunes.utils.R
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CatDetailViewModel @Inject constructor(
    private val getBreedUseCase: GetBreedUseCase,
    private var networkConnectivityObserver: NetworkConnectivityObserver
) : ViewModel() {

    private val _loading: MutableLiveData<Int> = MutableLiveData(0)
    private val _error: MutableLiveData<UiText> = MutableLiveData()
    private val _breed: MutableLiveData<Breed> = MutableLiveData()
    private val _networkConnectivity: MutableLiveData<Boolean> = MutableLiveData()

    val loading: LiveData<Boolean>
        get() = _loading.map { it > 0 }
    val error: LiveData<UiText>
        get() = _error
    val breed: LiveData<Breed>
        get() = _breed
    val networkConnectivity: LiveData<Boolean>
        get() = _networkConnectivity

    init {
        viewModelScope.launch {
            networkConnectivityObserver.observe().collect { status ->
                _networkConnectivity.value = status.hasConnection()
            }
        }
    }

    fun getCat(id: String) {
        viewModelScope.launch {
            getBreedUseCase(id).collect { breed ->
                hasLoading(breed)
                when (breed) {
                    is Resource.Success -> _breed.value = breed.data
                    is Resource.Error -> handleError(breed)
                    else -> {}
                }
            }
        }
    }

    private fun handleError(resource: Resource.Error<*>) {
        val errorResponse = resource.errorData
        _error.value = if (errorResponse is NotFoundError) {
            UiText.DynamicString(errorResponse.message)
        } else {
            UiText.StringResource(R.string.error_in_fetch_cat)
        }
    }

    private fun hasLoading(resource: Resource<*>) {
        if (resource is Resource.Loading) {
            _loading.value = _loading.value?.plus(1)
        } else {
            _loading.value = _loading.value?.minus(1)
        }
    }

}