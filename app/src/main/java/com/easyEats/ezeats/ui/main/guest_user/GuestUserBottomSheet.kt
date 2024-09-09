package com.easyEats.ezeats.ui.main.guest_user

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.easyEats.ezeats.R
import com.easyEats.ezeats.databinding.FragmentGuestUserBottomSheetBinding
import com.easyEats.ezeats.ui.auth.main.AuthActivity
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class GuestUserBottomSheet : BottomSheetDialogFragment() {
    private lateinit var binding: FragmentGuestUserBottomSheetBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentGuestUserBottomSheetBinding.inflate(layoutInflater)
        binding.btnSignIn.setOnClickListener {
            val intent = Intent(requireActivity(), AuthActivity::class.java)
            intent.putExtra(
                "FRAGMENT_ID",
                R.id.signInScreenFragment
            ) // Replace with your fragment ID
            startActivity(intent)
        }

        binding.btnSignUp.setOnClickListener {
            val intent = Intent(requireActivity(), AuthActivity::class.java)
            intent.putExtra(
                "FRAGMENT_ID",
                R.id.signUpScreenFragment
            ) // Replace with your fragment ID
            startActivity(intent)
        }
        return binding.root
    }

}