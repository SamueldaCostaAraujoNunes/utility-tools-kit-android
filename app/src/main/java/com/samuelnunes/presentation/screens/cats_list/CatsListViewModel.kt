package com.samuelnunes.presentation.screens.cats_list

import androidx.lifecycle.*
import com.samuelnunes.data.dto.response.error.NotFoundError
import com.samuelnunes.domain.entity.Breed
import com.samuelnunes.domain.use_case.GetAllBreedsUseCase
import com.samuelnunes.domain.use_case.GetCatsGifsUseCase
import com.samuelnunes.utility_tool_kit.domain.Resource
import com.samuelnunes.utility_tool_kit.network.NetworkConnectivityObserver
import com.samuelnunes.utility_tool_kit.utils.UiText
import com.samuelnunes.utils.R
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CatsListViewModel @Inject constructor(
    private var getAllBreedsUseCase: GetAllBreedsUseCase,
    private var getCatsGifsUseCase: GetCatsGifsUseCase,
    private var networkConnectivityObserver: NetworkConnectivityObserver
) : ViewModel() {

    private val _loading: MutableLiveData<Int> = MutableLiveData(0)
    private val _error: MutableLiveData<UiText> = MutableLiveData()
    private val _gifs: MutableLiveData<List<Breed.Image>> = MutableLiveData()
    private val _breeds: MutableLiveData<List<Breed>> = MutableLiveData()
    private val _networkConnectivity: MutableLiveData<Boolean> = MutableLiveData()

    val loading: LiveData<Boolean>
        get() = _loading.map { it > 0 }
    val error: LiveData<UiText>
        get() = _error
    val gifs: LiveData<List<Breed.Image>>
        get() = _gifs
    val breeds: LiveData<List<Breed>>
        get() = _breeds
    val networkConnectivity: LiveData<Boolean>
        get() = _networkConnectivity

    init {
        viewModelScope.launch {
            networkConnectivityObserver.observe().collect { status ->
                val hasConnection = status.hasConnection()
                _networkConnectivity.value = hasConnection
                if (hasConnection) {
                    fetchNewGifs()
                }
            }
        }
        fetchBreeds()
    }

    private suspend fun <T : Resource<*>> Flow<T>.collectLoading(collector: FlowCollector<T>) = collect { value ->
        if (value is Resource.Loading<*>) {
            _loading.value = _loading.value?.plus(1)
        } else {
            _loading.value = _loading.value?.minus(1)
        }
        collector.emit(value)
    }

    fun fetchBreeds() {
        viewModelScope.launch {
            getAllBreedsUseCase().collectLoading { result ->
                when (result) {
                    is Resource.Success -> _breeds.value = result.data
                    is Resource.Error -> handleError(result, "Gatos")
                    is Resource.Empty -> {}
                    else -> {}
                }
            }
        }
    }

    fun fetchNewGifs() {
        viewModelScope.launch {
            getCatsGifsUseCase().collectLoading { result ->
                when (result) {
                    is Resource.Success -> _gifs.value = result.data
                    is Resource.Error -> handleError(result, "Gifs")
                    else -> {}
                }
            }
        }
    }

    private fun handleError(resource: Resource.Error<*>, source: String) {
        val errorResponse = resource.errorData
        _error.value = if (errorResponse is NotFoundError) {
            UiText.DynamicString(errorResponse.message)
        } else {
            UiText.StringResource(R.string.error_in_fetch_cats, source)
        }
    }
}