package com.sertanfox.screamingarchitecture.presentation.states.jokes

data class CategoriesState(
    val isLoading: Boolean = false,
    val categories: List<String> = emptyList(),
    val error: String = ""
)
