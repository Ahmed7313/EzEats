package com.easyEats.ezeats.ui.auth.sign_in

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.easyEats.ezeats.MyApplication.Companion.TAG
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
        listenToOtpResult()
    }

    private fun listenToOtpResult() {
        signInViewModel.sendOtpLiveData.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Loading -> {
                    showLoading()
                }

                is Resource.Success -> {
                    hideLoading()
                    goToVerificationScreen()
                    binding.btnSignIn.isEnabled=true
                }

                is Resource.Error -> {
                    hideLoading()
                    Toasty.error(requireContext(), resource.message, Toast.LENGTH_LONG).show()
                }

                is Resource.UnAuthenticated -> {
                    hideLoading()
                    authenticateUser()
                }

                else -> {
                    hideLoading()
                }
            }
        }
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

        binding.btnSignIn.setOnClickListener {
            binding.btnSignIn.isEnabled=false
            signInViewModel.findUser()
        }
    }

    override fun onDestroyView() {
        signInViewModel.sendOtpLiveData.value = null
        signInViewModel.isOtpSent(false)
        super.onDestroyView()
    }

}

