package com.sertanfox.screamingarchitecture.presentation.viewmodels

import androidx.lifecycle.ViewModel
import com.sertanfox.screamingarchitecture.domain.usecase.ExampleUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomePageViewModel @Inject constructor(
    private val exampleUseCase : ExampleUseCase
) : ViewModel() {
    val welcome = "Welcome to Home Page Fragment"
}