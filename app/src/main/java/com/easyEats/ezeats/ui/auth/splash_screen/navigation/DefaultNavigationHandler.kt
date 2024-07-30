package com.easyEats.ezeats.ui.auth.splash_screen.navigation

import android.content.Intent
import androidx.navigation.fragment.findNavController
import com.easyEats.ezeats.R
import com.easyEats.ezeats.ui.auth.splash_screen.SplashScreenFragment
import com.easyEats.ezeats.ui.main.main.HomeActivity

class DefaultNavigationHandler(
    private val fragment: SplashScreenFragment
) : NavigationHandler {

    override fun handleNavigation(state: NavigationState) {
        when (state) {
            NavigationState.IS_USER_VERIFIED -> {
                fragment.startActivity(Intent(fragment.requireContext(), HomeActivity::class.java))
                fragment.requireActivity().finish()
            }

            NavigationState.ONBOARDING_WATCHED -> {
                fragment.findNavController()
                    .navigate(R.id.action_splashScreenFragment_to_introScreenFragment)
            }

            NavigationState.ONBOARDING_NOT_WATCHED -> {
                fragment.findNavController()
                    .navigate(R.id.action_splashScreenFragment_to_onBoardingFragment)
            }

            NavigationState.NO_ACTION -> {
                // No action needed
            }
        }
    }
}
