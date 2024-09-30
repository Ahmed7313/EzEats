package com.easyEats.ezeats.ui.auth.sign_up

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.easyEats.ezeats.R
import com.easyEats.ezeats.base.BaseFragment
import com.easyEats.ezeats.databinding.FragmentSignUpScreenBinding
import com.easyEats.ezeats.utils.Resource
import com.easyEats.ezeats.utils.validation.ValidateFields
import com.parse.ParseUser
import dagger.hilt.android.AndroidEntryPoint
import es.dmoral.toasty.Toasty

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
        binding.signUpViewModel = signUpViewModel
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
                    signUpViewModel.saveNecessaryData(resource)
                    binding.btnSignUp.isEnabled = true
                }

                is Resource.Error -> {
                    hideLoading()
                    Toasty.error(requireContext(), resource.message, Toast.LENGTH_LONG).show()
                }

                else -> {
                    hideLoading()
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
                    Toasty.error(requireContext(), resource.message, Toast.LENGTH_LONG).show()
                }

                else -> {
//                    hideLoading()
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
        // Navigate to Sign In screen when "Sign In" text view is clicked
        binding.tvSignIn.setOnClickListener {
            findNavController().navigate(R.id.action_signUpScreenFragment_to_signInScreenFragment)
        }
        binding.btnSignUp.setOnClickListener {
            binding.btnSignUp.isEnabled=false
            signUpViewModel.register()

        }
    }

    override fun onDestroyView() {
        signUpViewModel.registrationResult.value = null
        signUpViewModel.sendOtpLiveData.value = null
        signUpViewModel.isOtpSent(false)
        super.onDestroyView()
    }
}