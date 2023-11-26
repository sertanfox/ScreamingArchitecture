package com.sertanfox.screamingarchitecture.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.fragment.app.Fragment
import com.sertanfox.screamingarchitecture.R
import com.sertanfox.screamingarchitecture.databinding.ActivityMainBinding
import com.sertanfox.screamingarchitecture.presentation.fragments.CategoriesFragment
import com.sertanfox.screamingarchitecture.presentation.fragments.RandomFragment
import com.sertanfox.screamingarchitecture.presentation.fragments.SearchFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding:ActivityMainBinding
    @Suppress("DEPRECATION")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.bottomNavigationView.setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.navigation_home -> {
                    loadFragment(CategoriesFragment())
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.navigation_jokes -> {
                    loadFragment(RandomFragment())
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.navigation_notifications -> {
                    loadFragment(SearchFragment())
                    return@setOnNavigationItemSelectedListener true
                }
                else -> false
            }
        }

        loadFragment(CategoriesFragment())
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().replace(binding.fragmentContainerView.id, fragment).commit()
    }
}