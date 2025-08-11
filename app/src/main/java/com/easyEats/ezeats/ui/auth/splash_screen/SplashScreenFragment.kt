package com.easyEats.ezeats.ui.auth.splash_screen

import android.annotation.SuppressLint
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.easyEats.ezeats.MyApplication.Companion.TAG
import com.easyEats.ezeats.R
import com.easyEats.ezeats.base.BaseFragment
import com.easyEats.ezeats.databinding.FragmentSplashScreenBinding
import com.easyEats.ezeats.ui.auth.splash_screen.navigation.DefaultNavigationHandler
import com.easyEats.ezeats.ui.auth.splash_screen.navigation.NavigationState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

/**
 * SplashScreenFragment displays a splash screen when the application is launched.
 * It changes the status bar color and navigates to the OnBoardingFragment after a delay.
 *
 * This fragment uses the `@AndroidEntryPoint` annotation to enable dependency injection with Hilt.
 */
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
                    splashViewModel.isUserVerified.collect { isUserVerified ->
                        Log.d(TAG, "onResume: $isUserVerified")
                        Log.d(TAG, "onResume: $hasWatchedOnboarding")
                        val navigationHandler = DefaultNavigationHandler(this@SplashScreenFragment)
                        val currentDestination = findNavController().currentDestination?.id
                        val state = when {
                            isUserVerified -> NavigationState.IS_USER_VERIFIED
                            hasWatchedOnboarding -> NavigationState.ONBOARDING_WATCHED
                            !hasWatchedOnboarding -> NavigationState.ONBOARDING_NOT_WATCHED
                            else -> NavigationState.NO_ACTION
                        }
                        navigationHandler.handleNavigation(state)
                    }
                }
            }
        }, 3000)
    }
}
