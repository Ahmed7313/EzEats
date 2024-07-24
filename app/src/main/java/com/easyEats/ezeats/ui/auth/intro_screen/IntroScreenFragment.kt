package com.easyEats.ezeats.ui.auth.intro_screen

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.easyEats.ezeats.R
import com.easyEats.ezeats.base.BaseFragment
import com.easyEats.ezeats.databinding.FragmentIntroScreenBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class IntroScreenFragment : BaseFragment<FragmentIntroScreenBinding>() {

    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentIntroScreenBinding {
        return FragmentIntroScreenBinding.inflate(inflater, container, false)
    }



    override fun setUpViews() {
        changeStatusBackground()
        setUpClickListeners()
    }

    private fun setUpClickListeners() {
        binding.btnSignIn.setOnClickListener {
            findNavController().navigate(R.id.action_introScreenFragment_to_signInScreenFragment)
        }

        binding.btnSignUp.setOnClickListener {
            findNavController().navigate(R.id.action_introScreenFragment_to_signUpScreenFragment)
        }
    }

    private fun changeStatusBackground() {
        activity?.window?.statusBarColor =
            ContextCompat.getColor(requireContext(), R.color.main_color)
    }

}