package com.sertanfox.screamingarchitecture.presentation.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sertanfox.screamingarchitecture.common.Resource
import com.sertanfox.screamingarchitecture.domain.usecase.jokes.CategoriesUseCase
import com.sertanfox.screamingarchitecture.domain.usecase.jokes.RandomUseCase
import com.sertanfox.screamingarchitecture.domain.usecase.jokes.SearchUseCase
import com.sertanfox.screamingarchitecture.presentation.states.jokes.CategoriesState
import com.sertanfox.screamingarchitecture.presentation.states.jokes.RandomState
import com.sertanfox.screamingarchitecture.presentation.states.jokes.SearchState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class HomePageViewModel @Inject constructor(
    private val categoriesUseCase : CategoriesUseCase,
    private val randomUseCase : RandomUseCase,
    private val searchUseCase : SearchUseCase
) : ViewModel() {
    private val TAG = "HomePageViewModel"
    private val _welcome = MutableStateFlow<String?>("")
    val welcome: MutableStateFlow<String?> = _welcome

    private val _categoriesState = MutableStateFlow(CategoriesState())
    val categoriesState: MutableStateFlow<CategoriesState> = _categoriesState

    private val _searchState = MutableStateFlow(SearchState())
    val searchState: MutableStateFlow<SearchState> = _searchState

    private val _randomState = MutableStateFlow(RandomState())
    val randomState: MutableStateFlow<RandomState> = _randomState
    init {
        getRandom()
    }

    fun getRandomJoke(){
        getRandom()
    }

    private fun getCategories() {
        categoriesUseCase().onEach { result ->
            when (result) {
                is Resource.Success -> {
                    Log.d(TAG,"Resource.Success : "+result.data)
                    _categoriesState.value = CategoriesState(categories = result.data ?: emptyList())
                    Log.d(TAG,"state.value.categories : "+categoriesState.value.categories.toString())
                    welcome.value = categoriesState.value.categories.toString()
                    Log.d(TAG,"welcome.value : "+welcome.value)
                }
                is Resource.Error -> {
                    _categoriesState.value = CategoriesState(
                        error = result.message ?: "An unexpected error occured"
                    )
                    Log.d(TAG,"Resource.Error")
                }
                is Resource.Loading -> {
                    Log.d(TAG,"Resource.Loading")
                    _categoriesState.value = CategoriesState(isLoading = true)
                }
            }
        }.launchIn(viewModelScope)
    }

    private fun getSearch(searchText:String) {
        searchUseCase(searchText).onEach { result ->
            when (result) {
                is Resource.Success -> {
                    Log.d(TAG,"Resource.Success : "+result.data)
                    _searchState.value = SearchState(searchResult = result.data)
                    Log.d(TAG,"state.value.categories : "+searchState.value.searchResult.toString())
                    welcome.value = searchState.value.searchResult.toString()
                    Log.d(TAG,"welcome.value : "+welcome.value)
                }
                is Resource.Error -> {
                    _searchState.value = SearchState(
                        error = result.message ?: "An unexpected error occured"
                    )
                    Log.d(TAG,"Resource.Error")
                }
                is Resource.Loading -> {
                    Log.d(TAG,"Resource.Loading")
                    _searchState.value = SearchState(isLoading = true)
                }
            }
        }.launchIn(viewModelScope)
    }

    private fun getRandom() {
        randomUseCase().onEach { result ->
            when (result) {
                is Resource.Success -> {
                    Log.d(TAG,"Resource.Success : "+result.data)
                    _randomState.value = RandomState(randomJoke = result.data)
                    Log.d(TAG,"state.value.categories : "+randomState.value.randomJoke.toString())
                    welcome.value = randomState.value.randomJoke?.value.toString()
                    Log.d(TAG,"welcome.value : "+welcome.value)
                }
                is Resource.Error -> {
                    _randomState.value = RandomState(
                        error = result.message ?: "An unexpected error occured"
                    )
                    Log.d(TAG,"Resource.Error")
                }
                is Resource.Loading -> {
                    Log.d(TAG,"Resource.Loading")
                    _randomState.value = RandomState(isLoading = true)
                }
            }
        }.launchIn(viewModelScope)
    }
}