package com.sertanfox.screamingarchitecture.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sertanfox.screamingarchitecture.common.Resource
import com.sertanfox.screamingarchitecture.domain.usecase.jokes.RandomUseCase
import com.sertanfox.screamingarchitecture.presentation.states.jokes.RandomState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class RandomViewModel @Inject constructor(
    private val randomUseCase : RandomUseCase,
) : ViewModel() {

    private val _randomState = MutableStateFlow(RandomState())
    val randomState: MutableStateFlow<RandomState> = _randomState

    init {
        getRandom()
    }

    fun random(){
        getRandom()
    }

    private fun getRandom() {
        randomUseCase().onEach { result ->
            when (result) {
                is Resource.Success -> {
                    _randomState.value = RandomState(randomJoke = result.data)
                }
                is Resource.Error -> {
                    _randomState.value = RandomState(error = result.message ?: "An unexpected error occured")
                }
                is Resource.Loading -> {
                    _randomState.value = RandomState(isLoading = true)
                }
            }
        }.launchIn(viewModelScope)
    }
}