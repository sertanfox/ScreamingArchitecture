package com.sertanfox.screamingarchitecture.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sertanfox.screamingarchitecture.common.Resource
import com.sertanfox.screamingarchitecture.domain.usecase.jokes.CategoriesUseCase
import com.sertanfox.screamingarchitecture.presentation.states.jokes.CategoriesState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class CategoriesViewModel @Inject constructor(
    private val categoriesUseCase : CategoriesUseCase
) : ViewModel() {

    private val _categoriesState = MutableStateFlow(CategoriesState())
    val categoriesState: MutableStateFlow<CategoriesState> = _categoriesState

    init {
        getCategories()
    }

    fun getCategoriesButton(){
        getCategories()
    }

    private fun getCategories() {
        categoriesUseCase().onEach { result ->
            when (result) {
                is Resource.Success -> {
                    _categoriesState.value = CategoriesState(categories = result.data)
                }
                is Resource.Error -> {
                    _categoriesState.value = CategoriesState(error = result.message ?: "An unexpected error occured")
                }
                is Resource.Loading -> {
                    _categoriesState.value = CategoriesState(isLoading = true)
                }
            }
        }.launchIn(viewModelScope)
    }
}