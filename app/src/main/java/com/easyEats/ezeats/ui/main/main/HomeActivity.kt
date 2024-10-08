package com.easyEats.ezeats.ui.main.main

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.get
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.easyEats.ezeats.MyApplication.Companion.TAG
import com.easyEats.ezeats.R
import com.easyEats.ezeats.databinding.ActivityHomeBinding
import com.google.android.gms.common.internal.Objects
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {

    // View binding for activity_home.xml
    lateinit var binding: ActivityHomeBinding

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
        binding.navView.itemIconTintList = null
        setUpBottomNavigationWithNavHostFragment()
        handleIntent(intent)

        lifecycleScope.launch {
            mainViewModel.userLocationAddressMutableLiveData.collect {
                Log.d(TAG, "onCreate:a7a ${it?.address}")
                Log.d(TAG, "onCreate:a7a ${it?.lat}")
                Log.d(TAG, "onCreate:a7a ${it?.lng}")
            }
        }

    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        handleIntent(intent)
    }

    private fun handleIntent(intent: Intent) {
        if (intent.action == Intent.ACTION_VIEW) {
            val data = HashMap<String, Any>()
            data["isOpenShareLink"] = true
            data["restaurantId"] = intent.data?.lastPathSegment.toString()
            mainViewModel.isOpenShareLink.value = data
        }
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
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.homeScreenFragment, R.id.qrScreenFragment, R.id.percentScreenFragment, R.id.profileScreenFragment, R.id.pointScreenFragment -> {
                    binding.navView.visibility = VISIBLE
                }

                else -> {
                    binding.navView.visibility = GONE
                }
            }
        }


        // Set up the Bottom Navigation View with the NavController
        binding.navView.setupWithNavController(navController)

    }
}

