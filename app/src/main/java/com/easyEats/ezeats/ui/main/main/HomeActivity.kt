package com.easyEats.ezeats.ui.main.main

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.easyEats.ezeats.R
import com.easyEats.ezeats.databinding.ActivityHomeBinding
import dagger.hilt.android.AndroidEntryPoint
@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {

    // View binding for activity_home.xml
    private lateinit var binding: ActivityHomeBinding

    // NavController for managing fragment navigation
    private lateinit var navController: NavController

    // ViewModel for the activity, provided by Hilt
    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Initialize the view binding
        binding = ActivityHomeBinding.inflate(layoutInflater)
        // Set the root view from the binding as the content view
        setContentView(binding.root)
        // Set the ViewModel for data binding and provide the lifecycle owner
        binding.mainViewModel = mainViewModel
        binding.lifecycleOwner = this // Ensure LiveData updates the UI automatically
        // Set up the Bottom Navigation and NavHostFragment
        setUpBottomNavigationWithNavHostFragment()
    }

    /**
     * Initializes and configures the Bottom Navigation View and the Navigation Host Fragment.
     */
    private fun setUpBottomNavigationWithNavHostFragment() {
        // Retrieve the NavHostFragment from the fragment manager
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment

        // Initialize the NavController from the NavHostFragment
        navController = navHostFragment.navController

        // Set up the Bottom Navigation View with the NavController
        binding.navView.setupWithNavController(navController)
    }
}

