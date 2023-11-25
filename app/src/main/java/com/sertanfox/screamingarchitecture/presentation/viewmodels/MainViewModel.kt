package com.sertanfox.screamingarchitecture.presentation.viewmodels

import androidx.lifecycle.ViewModel
import com.sertanfox.screamingarchitecture.domain.usecase.ExampleUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val usecase:ExampleUseCase
) :ViewModel() {
}