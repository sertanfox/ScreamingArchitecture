package com.sertanfox.screamingarchitecture.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.sertanfox.screamingarchitecture.databinding.FragmentRandomBinding
import com.sertanfox.screamingarchitecture.presentation.viewmodels.RandomViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RandomFragment : Fragment() {

    private lateinit var binding: FragmentRandomBinding
    private val viewModel: RandomViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRandomBinding.inflate(inflater, container, false)
        initDataBinding()
        return binding.root
    }

    private fun initDataBinding() {
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
    }
}