package com.easyEats.ezeats.ui.auth.sign_in

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.easyEats.ezeats.R
import com.easyEats.ezeats.base.BaseFragment
import com.easyEats.ezeats.databinding.FragmentSignInScreenBinding
import com.easyEats.ezeats.utils.Resource
import com.easyEats.ezeats.utils.validation.ValidateFields
import com.parse.ParseUser
import dagger.hilt.android.AndroidEntryPoint
import es.dmoral.toasty.Toasty
@AndroidEntryPoint
class SignInScreenFragment : BaseFragment<FragmentSignInScreenBinding>() {

    private val signInViewModel: SignInViewModel by viewModels()

    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentSignInScreenBinding =
        FragmentSignInScreenBinding.inflate(inflater, container, false)

    override fun setUpViews() {
        binding.signInViewModel = signInViewModel
        validatePhoneNumber()
        setUpClickListeners()
        setUpObservers()
    }

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
                    sauceNecessaryData(resource)
                }
                is Resource.Error -> {
                    hideLoading()
                    Toasty.error(requireContext(), resource.message, Toast.LENGTH_LONG).show()
                }
                else->{
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
                    Toasty.error(requireContext(), resource.message, Toast.LENGTH_LONG).show()
                }
                else->{
                    hideLoading()
                }
            }
        }
    }

    private fun sauceNecessaryData(resource: Resource.Success<ParseUser>) {
        val user = resource.data
        signInViewModel.sendOtp()
        signInViewModel.saveToken(user ?: return)
        signInViewModel.saveMobileNumber()
    }

    private fun goToVerificationScreen() {
        val action =
            SignInScreenFragmentDirections.actionSignInScreenFragmentToVerificationScreenFragment(
                signInViewModel.phoneNumber.value.toString()
            )
        findNavController().navigate(action)
    }

    private fun setUpClickListeners() {
        binding.tvSignUp.setOnClickListener {
            findNavController().navigate(R.id.action_signInScreenFragment_to_signUpScreenFragment)
        }
    }

    override fun onDestroyView() {
        signInViewModel.loginResult.value=null
        signInViewModel.sendOtpLiveData.value=null
        super.onDestroyView()
    }
}

