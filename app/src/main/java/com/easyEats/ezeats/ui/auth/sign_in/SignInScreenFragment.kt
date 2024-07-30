package com.easyEats.ezeats.ui.auth.sign_in

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.easyEats.ezeats.MyApplication.Companion.TAG
import com.easyEats.ezeats.R
import com.easyEats.ezeats.base.BaseFragment
import com.easyEats.ezeats.databinding.FragmentSignInScreenBinding
import com.easyEats.ezeats.utils.Resource
import com.easyEats.ezeats.utils.ValidateFields
import dagger.hilt.android.AndroidEntryPoint

/**
 * SignInScreenFragment handles the sign-in screen functionality of the application.
 * It validates the phone number input and sets up click listeners for user interactions.
 *
 * This fragment uses the `@AndroidEntryPoint` annotation to enable dependency injection with Hilt.
 */
@AndroidEntryPoint
class SignInScreenFragment : BaseFragment<FragmentSignInScreenBinding>() {

    // ViewModel to manage the sign-in logic
    private val signInViewModel: SignInViewModel by viewModels()

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
    ): FragmentSignInScreenBinding =
        FragmentSignInScreenBinding.inflate(inflater, container, false)

    /**
     * Sets up the initial views of the fragment.
     * Calls methods to validate the phone number and set up click listeners.
     */
    override fun setUpViews() {
        validatePhoneNumber()
        setUpClickListeners()
        setUpObservers()
    }

    /**
     * Validates the phone number input.
     * Observes phone number validity and listens for changes in the phone number input field.
     */
    private fun validatePhoneNumber() {
        ValidateFields.observePhoneNumberValidity(
            signInViewModel,
            binding.btnSignIn,
            requireContext(),
            viewLifecycleOwner
        )
        ValidateFields.listenToPhoneNumberChange(
            signInViewModel,
            binding.etPhoneInput
        )
    }

    private fun setUpObservers() {
        signInViewModel.loginResult.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Loading -> {
                    showLoading()
                }

                is Resource.Success -> {
                    hideLoading()
                    val user = resource.data
                    signInViewModel.sendOtp(binding.etPhoneInput.text.toString())
                    signInViewModel.saveToken(user ?: return@observe)
                    signInViewModel.saveMobileNumber(binding.etPhoneInput.text.toString())
                }

                is Resource.Error -> {
                    hideLoading()
                }
            }
        }

        signInViewModel.sendOtpLiveData.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Loading -> {
                    showLoading()
                }

                is Resource.Success -> {
                    hideLoading()
                    goToVerificationScreen()
                }

                is Resource.Error -> {
                    hideLoading()
                }
            }
        }
    }

    private fun goToVerificationScreen() {
        val action =
            SignInScreenFragmentDirections.actionSignInScreenFragmentToVerificationScreenFragment(
                binding.etPhoneInput.text.toString()
            )
        findNavController().navigate(action)
    }

    /**
     * Sets up click listeners for user interactions.
     * Navigates to the sign-up screen when the sign-up text view is clicked.
     */
    private fun setUpClickListeners() {
        binding.tvSignUp.setOnClickListener {
            findNavController().navigate(R.id.action_signInScreenFragment_to_signUpScreenFragment)
        }

        binding.btnSignIn.setOnClickListener {
            signInViewModel.login(binding.etPhoneInput.text.toString())
        }
    }
}
