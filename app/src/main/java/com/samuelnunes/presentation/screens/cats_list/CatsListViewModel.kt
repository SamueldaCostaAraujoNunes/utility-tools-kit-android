package com.samuelnunes.presentation.screens.cats_list

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.samuelnunes.domain.entity.Breed
import com.samuelnunes.domain.use_case.GetAllBreedsUseCase
import com.samuelnunes.domain.use_case.GetCatsGifsUseCase
import com.samuelnunes.utility_tool_kit.domain.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class CatsListViewModel @Inject constructor(
    private var getAllBreedsUseCase: GetAllBreedsUseCase,
    private var getCatsGifsUseCase: GetCatsGifsUseCase
): ViewModel() {

    fun getAllBreeds(): LiveData<Result<List<Breed>>> = getAllBreedsUseCase().asLiveData()

    fun getCatsGifs(): LiveData<Result<List<Breed.Image>>> = getCatsGifsUseCase().asLiveData()

}