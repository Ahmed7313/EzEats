package com.easyEats.ezeats.ui.auth.splash_screen

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.easyEats.ezeats.R
import com.easyEats.ezeats.base.BaseFragment
import com.easyEats.ezeats.databinding.FragmentSplashScreenBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

/**
 * SplashScreenFragment displays a splash screen when the application is launched.
 * It changes the status bar color and navigates to the OnBoardingFragment after a delay.
 *
 * This fragment uses the `@AndroidEntryPoint` annotation to enable dependency injection with Hilt.
 */
@SuppressLint("CustomSplashScreen")
@AndroidEntryPoint
class SplashScreenFragment : BaseFragment<FragmentSplashScreenBinding>() {

    private val splashViewModel: SplashViewModel by viewModels()

    /**
     * Inflates the view binding for this fragment.
     *
     * @param inflater The LayoutInflater object that can be used to inflate any views in the fragment.
     * @param container If non-null, this is the parent view that the fragment's UI should be attached to.
     * @return The inflated binding object.
     */
    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentSplashScreenBinding.inflate(inflater, container, false)

    /**
     * Sets up the initial views of the fragment.
     * Changes the status bar color to the main color.
     */
    override fun setUpViews() {
        changeStatusBackground()
    }

    /**
     * Changes the status bar color to the main color defined in resources.
     */
    private fun changeStatusBackground() {
        activity?.window?.statusBarColor =
            ContextCompat.getColor(requireContext(), R.color.main_color)
    }

    /**
     * Called when the fragment is resumed.
     * Navigates to the OnBoardingFragment after a 3-second delay.
     */
    override fun onResume() {
        super.onResume()
        Handler(Looper.getMainLooper()).postDelayed({
            lifecycleScope.launch {
                splashViewModel.isUserWatchOnBoardingScreen.collect { hasWatchedOnboarding ->
                    // Avoid multiple navigation actions
                    val currentDestination = findNavController().currentDestination?.id
                    if (hasWatchedOnboarding && currentDestination == R.id.splashScreenFragment) {
                        findNavController().navigate(R.id.action_splashScreenFragment_to_introScreenFragment)
                    } else if (!hasWatchedOnboarding && currentDestination == R.id.splashScreenFragment) {
                        findNavController().navigate(R.id.action_splashScreenFragment_to_onBoardingFragment)
                    }
                }
            }
        }, 3000)
    }
}
