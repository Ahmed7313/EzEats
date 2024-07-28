package com.easyEats.ezeats.ui.main.main

import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.easyEats.ezeats.R
import com.easyEats.ezeats.databinding.ActivityHomeBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private lateinit var navController: NavController
    private var backPressedOnce = false
    private var backPressJob: Job? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Get the NavHostFragment
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        // Get the NavController from the NavHostFragment
        navController = navHostFragment.navController

        // Setup BottomNavigationView with NavController
        binding.navView.setupWithNavController(navController)
    }

    override fun onBackPressed() {
        if (navController.currentBackStackEntry?.destination?.id == R.id.homeScreenFragment ||
            navController.currentBackStackEntry?.destination?.id == R.id.percentScreenFragment ||
            navController.currentBackStackEntry?.destination?.id == R.id.profileScreenFragment ||
            navController.currentBackStackEntry?.destination?.id == R.id.pointScreenFragment
        ) {
            if (backPressedOnce) {
                // If back was pressed once before, exit the activity
                super.onBackPressed()
                return
            }

            this.backPressedOnce = true
            Toast.makeText(this, getString(R.string.press_back_again_to_exit), Toast.LENGTH_SHORT).show()

            // Reset the flag after 2 seconds using coroutine
            backPressJob?.cancel() // Cancel any previous job
            backPressJob = MainScope().launch(Dispatchers.Main) {
                delay(2000) // 2 seconds delay
                backPressedOnce = false
            }
        } else {
            super.onBackPressed()
        }
    }
}