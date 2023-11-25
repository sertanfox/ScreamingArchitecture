package com.sertanfox.screamingarchitecture.presentation.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.sertanfox.screamingarchitecture.databinding.FragmentHomePageBinding
import com.sertanfox.screamingarchitecture.presentation.viewmodels.HomePageViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomePageFragment : Fragment() {
    private lateinit var binding : FragmentHomePageBinding
    private val viewModel : HomePageViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomePageBinding.inflate(inflater,container,false)
        initDataBinding()
        return binding.root
    }

    private fun initDataBinding(){
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
    }

}