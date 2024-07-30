package com.easyEats.ezeats.ui.auth.sign_up

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.easyEats.ezeats.MyApplication.Companion.TAG
import com.easyEats.ezeats.R
import com.easyEats.ezeats.base.BaseFragment
import com.easyEats.ezeats.databinding.FragmentSignUpScreenBinding
import com.easyEats.ezeats.utils.Resource
import com.easyEats.ezeats.utils.ValidateFields
import dagger.hilt.android.AndroidEntryPoint

/**
 * SignUpScreenFragment handles the sign-up screen functionality of the application.
 * It validates the form inputs, manages user interactions, and observes the registration process.
 *
 * This fragment uses the `@AndroidEntryPoint` annotation to enable dependency injection with Hilt.
 */
@AndroidEntryPoint
class SignUpScreenFragment : BaseFragment<FragmentSignUpScreenBinding>() {

    // ViewModel instance for managing UI-related data in a lifecycle-conscious way
    private val signUpViewModel: SignUpViewModel by viewModels()

    /**
     * Inflates the binding layout for this fragment.
     *
     * @param inflater LayoutInflater to inflate the binding
     * @param container ViewGroup that contains the layout
     * @return The inflated binding instance
     */
    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentSignUpScreenBinding = FragmentSignUpScreenBinding.inflate(inflater, container, false)

    /**
     * Sets up the views and initializes necessary listeners.
     */
    override fun setUpViews() {
        validateFields()
        setUpClickListeners()
        setUpObservers()
    }

    /**
     * Validates the form fields for the sign-up process.
     * Observes changes in the input fields and updates the form validity.
     */
    private fun validateFields() {
        ValidateFields.observeFormValidity(
            signUpViewModel,
            binding.btnSignUp,
            requireContext(),
            viewLifecycleOwner
        )
        ValidateFields.listenToPhoneNumberChange(
            signUpViewModel,
            binding.etPhoneInput
        )
        ValidateFields.listenToNameChange(
            signUpViewModel,
            binding.etNameInput
        )
    }

    /**
     * Sets up observers to listen for changes in the ViewModel's LiveData.
     * Observes the registration result and handles different states.
     */
    private fun setUpObservers() {
        signUpViewModel.registrationResult.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Loading -> {
                    showLoading()
                }

                is Resource.Success -> {
                    hideLoading()
                    signUpViewModel.saveMobileNumber(binding.etPhoneInput.text.toString())
                    signUpViewModel.saveToken(resource.data)
                    signUpViewModel.saveName(binding.etNameInput.text.toString())
                    signUpViewModel.sendOtp(binding.etPhoneInput.text.toString())

                }

                is Resource.Error -> {
                    hideLoading()
                }

                else -> {

                }
            }
        }

        signUpViewModel.sendOtpLiveData.observe(viewLifecycleOwner) { resource ->
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

    /**
     * Navigates to the verification screen with the phone number input.
     */
    private fun goToVerificationScreen() {
        val action =
            SignUpScreenFragmentDirections.actionSignUpScreenFragmentToVerificationScreenFragment(
                binding.etPhoneInput.text.toString()
            )
        findNavController().navigate(action)
    }

    /**
     * Sets up click listeners for user interactions.
     * Handles the Sign Up and Sign In button clicks.
     */
    private fun setUpClickListeners() {
        // Navigate to verification screen when Sign Up button is clicked
        binding.btnSignUp.setOnClickListener {
            signUpViewModel.register(
                binding.etPhoneInput.text.toString(),
                binding.etNameInput.text.toString()
            )
        }

        // Navigate to Sign In screen when "Sign In" text view is clicked
        binding.tvSignIn.setOnClickListener {
            findNavController().navigate(R.id.action_signUpScreenFragment_to_signInScreenFragment)
        }
    }
}