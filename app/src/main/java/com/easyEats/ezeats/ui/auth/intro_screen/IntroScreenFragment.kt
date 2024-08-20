package com.easyEats.ezeats.ui.auth.intro_screen

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.easyEats.ezeats.MyApplication.Companion.TAG
import com.easyEats.ezeats.R
import com.easyEats.ezeats.base.BaseFragment
import com.easyEats.ezeats.databinding.FragmentIntroScreenBinding
import com.easyEats.ezeats.ui.main.main.HomeActivity
import dagger.hilt.android.AndroidEntryPoint

/**
 * IntroScreenFragment is the initial screen of the app that provides navigation options to
 * sign in or sign up. It extends BaseFragment with FragmentIntroScreenBinding.
 */
@AndroidEntryPoint
class IntroScreenFragment : BaseFragment<FragmentIntroScreenBinding>() {

    /**
     * Inflates the view binding for this fragment.
     *
     * @param inflater LayoutInflater to use for inflating the view.
     * @param container Optional parent view that the fragment's UI should be attached to.
     * @return The inflated view binding instance.
     */
    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentIntroScreenBinding {
        return FragmentIntroScreenBinding.inflate(inflater, container, false)
    }

    /**
     * Called to initialize the fragment's views.
     * Sets up the status bar background and click listeners.
     */
    override fun setUpViews() {
        changeStatusBackground()
        setUpClickListeners()
    }

    /**
     * Sets up click listeners for the sign in and sign up buttons.
     * Navigates to the respective fragments on button clicks.
     */
    private fun setUpClickListeners() {
        binding.btnSignIn.setOnClickListener {
            findNavController().navigate(R.id.action_introScreenFragment_to_signInScreenFragment)
        }

        binding.btnSignUp.setOnClickListener {
            findNavController().navigate(R.id.action_introScreenFragment_to_signUpScreenFragment)
        }
        binding.btnContinueAsGuest.setOnClickListener {
            startActivity(Intent(requireContext(), HomeActivity::class.java))
            requireActivity().finish()
        }
    }

    /**
     * Changes the status bar background color to the main color.
     */
    private fun changeStatusBackground() {
        activity?.window?.statusBarColor =
            ContextCompat.getColor(requireContext(), R.color.main_color)
    }
}