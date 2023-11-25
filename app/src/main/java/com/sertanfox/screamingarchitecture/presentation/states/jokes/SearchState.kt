package com.sertanfox.screamingarchitecture.presentation.states.jokes

import com.sertanfox.screamingarchitecture.data.remote.dto.SearchDto

data class SearchState(
    val isLoading: Boolean = false,
    val searchResult: SearchDto? = null,
    val error: String = ""
)
