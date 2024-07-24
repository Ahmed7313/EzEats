package com.easyEats.ezeats.ui.auth.main

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewTreeObserver
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.easyEats.ezeats.R
import com.easyEats.ezeats.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

/**
 * MainActivity serves as the entry point of the application, handling the initialization of UI components,
 * observing network connectivity status, and updating the UI accordingly.
 *
 * This activity uses the `@AndroidEntryPoint` annotation to enable dependency injection with Hilt.
 *
 * @property authViewModel An instance of [AuthViewModel] to observe network connectivity status.
 * @property binding An instance of [ActivityMainBinding] to handle view binding.
 */
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    // ViewModel to observe network connectivity status
    private val authViewModel: AuthViewModel by viewModels()

    // View binding for activity_main.xml layout
    lateinit var binding: ActivityMainBinding

    /**
     * Called when the activity is first created.
     * Initializes the UI components and sets up the content view.
     *
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down
     *                           then this Bundle contains the data it most recently supplied in [onSaveInstanceState].
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    /**
     * Called when the activity is resumed.
     * Sets up the observer for network connectivity status to update the UI accordingly.
     */
    override fun onResume() {
        super.onResume()
        authViewModel.isConnected.observe(this) { isConnected ->
            updateConnectionStatus(isConnected)
        }
    }

    /**
     * Updates the visibility of UI components based on the network connectivity status.
     *
     * @param isConnected Boolean indicating the network connectivity status.
     *                    - `true`: The device is connected to the internet.
     *                    - `false`: The device is not connected to the internet.
     */
    private fun updateConnectionStatus(isConnected: Boolean) {
        if (isConnected) {
            binding.fragmentContainerView.visibility = View.VISIBLE
            binding.clInternetConnectionError.visibility = View.GONE
        } else {
            binding.fragmentContainerView.visibility = View.GONE
            binding.clInternetConnectionError.visibility = View.VISIBLE
        }
    }
}
