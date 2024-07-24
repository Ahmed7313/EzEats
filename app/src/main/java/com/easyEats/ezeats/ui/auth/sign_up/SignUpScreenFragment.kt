package com.easyEats.ezeats.ui.auth.sign_up

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.easyEats.ezeats.R
import com.easyEats.ezeats.base.BaseFragment
import com.easyEats.ezeats.databinding.FragmentSignUpScreenBinding
import com.easyEats.ezeats.utils.Resource
import com.easyEats.ezeats.utils.ValidateFields
import dagger.hilt.android.AndroidEntryPoint

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
    ) = FragmentSignUpScreenBinding.inflate(inflater, container, false)

    /**
     * Sets up the views and initializes necessary listeners.
     */
    override fun setUpViews() {
        validateFields()
        setUpClickListeners()
        setUpObservers()
    }

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

    private fun setUpObservers() {
        signUpViewModel.registrationResult.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Loading -> {
                    showLoading()
                }

                is Resource.Success -> {
                    hideLoading()
                    val user = resource.data
                    signUpViewModel.saveToken(user ?: return@observe)
                    Log.d("TAG", "Ahmed Saad Kenawy: $user ")
                    goToVerificationScreen()

                }

                is Resource.Error -> {
                    hideLoading()
                    val errorMessage = resource.message
                    Log.d("TAG", "Ahmed Saad Kenawy: $errorMessage ")
                }
            }
        }

    }

    private fun goToVerificationScreen() {
        val action =
            SignUpScreenFragmentDirections.actionSignUpScreenFragmentToVerificationScreenFragment(
                binding.etPhoneInput.text.toString()
            )
        findNavController().navigate(action)
    }

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
