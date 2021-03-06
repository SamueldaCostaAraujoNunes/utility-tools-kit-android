package com.samuelnunes.presentation.screens.cats_list

import androidx.lifecycle.*
import com.samuelnunes.data.dto.response.error.NotFoundError
import com.samuelnunes.domain.entity.Breed
import com.samuelnunes.domain.use_case.GetAllBreedsUseCase
import com.samuelnunes.domain.use_case.GetCatsGifsUseCase
import com.samuelnunes.utility_tool_kit.domain.Result
import com.samuelnunes.utility_tool_kit.utils.UiText
import com.samuelnunes.utils.R
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class CatsListViewModel @Inject constructor(
    private var getAllBreedsUseCase: GetAllBreedsUseCase,
    private var getCatsGifsUseCase: GetCatsGifsUseCase
): ViewModel() {

    private val _loading: MutableLiveData<Int> = MutableLiveData(0)
    private val _error: MutableLiveData<UiText> = MutableLiveData()
    private val _gifs: MutableLiveData<List<Breed.Image>> = MutableLiveData()
    private val _breeds: MutableLiveData<List<Breed>> = MutableLiveData()

    val loading: LiveData<Boolean>
        get() = _loading.map { it > 0 }
    val error: LiveData<UiText>
        get() = _error
    val gifs: LiveData<List<Breed.Image>>
        get() = _gifs
    val breeds: LiveData<List<Breed>>
        get() = _breeds

    init {
        fetchNewGifs()
        fetchBreeds()
    }

    private fun fetchBreeds() {
        viewModelScope.launch {
            getAllBreedsUseCase().collect { result ->
                hasLoading(result)
                when (result) {
                    is Result.Success -> _breeds.value = result.data
                    is Result.Error -> handleError(result, "Gatos")
                    else -> {}
                }
            }
        }
    }

    fun fetchNewGifs() {
        viewModelScope.launch {
            getCatsGifsUseCase().collect { result ->
                hasLoading(result)
                when (result) {
                    is Result.Success -> _gifs.value = result.data
                    is Result.Error -> handleError(result, "Gifs")
                    else -> {}
                }
            }
        }
    }

    private fun handleError(result: Result.Error<*>, source: String) {
        val errorResponse = result.errorResponse
        _error.value = if(errorResponse is NotFoundError) {
            UiText.DynamicString(errorResponse.message)
        } else {
            UiText.StringResource(R.string.error_in_fetch, source)
        }
    }

    private fun hasLoading(result: Result<*>) {
        if(result is Result.Loading){
            _loading.value = _loading.value?.plus(1)
        } else {
            _loading.value= _loading.value?.minus(1)
        }
    }

}