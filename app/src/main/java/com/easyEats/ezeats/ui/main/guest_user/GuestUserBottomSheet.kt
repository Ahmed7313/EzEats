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



/**
 * BottomSheetDialogFragment that provides options for guest users to either sign in or sign up.
 * It displays two buttons: one for signing in and one for signing up. When clicked, these buttons navigate the user
 * to the appropriate authentication screen in the `AuthActivity`.
 */
@AndroidEntryPoint
class GuestUserBottomSheet : BottomSheetDialogFragment() {

    // Binding for the Fragment layout
    private lateinit var binding: FragmentGuestUserBottomSheetBinding

    /**
     * Inflates the layout and sets up click listeners for the sign-in and sign-up buttons.
     * The buttons initiate the navigation to the appropriate authentication screens in `AuthActivity`.
     *
     * @param inflater The LayoutInflater to inflate the fragment's view.
     * @param container The parent view that the fragment's UI should be attached to.
     * @param savedInstanceState A Bundle that stores instance state information, or null if there's no state.
     * @return The root view of the fragment.
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for the fragment
        binding = FragmentGuestUserBottomSheetBinding.inflate(layoutInflater)

        // Set up the "Sign In" button click listener
        binding.btnSignIn.setOnClickListener {
            // Start the AuthActivity and navigate to the sign-in fragment
            val intent = Intent(requireActivity(), AuthActivity::class.java)
            intent.putExtra(
                "FRAGMENT_ID",
                R.id.signInScreenFragment // Replace with the ID of the sign-in screen fragment
            )
            startActivity(intent)
        }

        // Set up the "Sign Up" button click listener
        binding.btnSignUp.setOnClickListener {
            // Start the AuthActivity and navigate to the sign-up fragment
            val intent = Intent(requireActivity(), AuthActivity::class.java)
            intent.putExtra(
                "FRAGMENT_ID",
                R.id.signUpScreenFragment // Replace with the ID of the sign-up screen fragment
            )
            startActivity(intent)
        }

        // Return the root view of the fragment
        return binding.root
    }
}
