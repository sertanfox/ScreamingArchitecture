package com.sertanfox.screamingarchitecture.presentation.states.jokes

import com.sertanfox.screamingarchitecture.data.remote.dto.RandomDto

data class RandomState(
    val isLoading: Boolean = false,
    val randomJoke: RandomDto? = null,
    val error: String = ""
)