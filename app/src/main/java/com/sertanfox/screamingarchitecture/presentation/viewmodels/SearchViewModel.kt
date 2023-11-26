package com.sertanfox.screamingarchitecture.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sertanfox.screamingarchitecture.common.Resource
import com.sertanfox.screamingarchitecture.domain.usecase.jokes.SearchUseCase
import com.sertanfox.screamingarchitecture.presentation.states.jokes.SearchState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject
@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchUseCase : SearchUseCase
) : ViewModel() {

    private val _searchState = MutableStateFlow(SearchState())
    val searchState: MutableStateFlow<SearchState> = _searchState

    fun search(searchText: String){
        getSearch(searchText)
    }

    private fun getSearch(searchText:String) {
        searchUseCase(searchText).onEach { result ->
            when (result) {
                is Resource.Success -> {
                    _searchState.value = SearchState(searchResult = result.data)
                }
                is Resource.Error -> {
                    _searchState.value = SearchState(error = result.message ?: "An unexpected error occured")
                }
                is Resource.Loading -> {
                    _searchState.value = SearchState(isLoading = true)
                }
            }
        }.launchIn(viewModelScope)
    }
}