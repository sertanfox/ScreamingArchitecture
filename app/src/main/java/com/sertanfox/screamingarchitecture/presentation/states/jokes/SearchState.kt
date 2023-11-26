package com.sertanfox.screamingarchitecture.presentation.states.jokes

data class SearchState(
    val isLoading: Boolean = false,
    //val searchResult: SearchDto? = null,
    val searchResult: List<String>? = null,
    val error: String = ""
)
