package com.easyEats.ezeats.ui.auth.main

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.easyEats.ezeats.databinding.ActivityAuthBinding
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
class AuthActivity : AppCompatActivity() {

    // ViewModel to observe network connectivity status
    private val authViewModel: AuthViewModel by viewModels()

    // View binding for activity_main.xml layout
    lateinit var binding: ActivityAuthBinding

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
        binding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.authViewModel = authViewModel
        binding.lifecycleOwner = this
    }

}
