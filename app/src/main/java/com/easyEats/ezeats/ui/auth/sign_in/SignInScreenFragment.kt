package com.easyEats.ezeats.ui.auth.sign_in

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.easyEats.ezeats.MyApplication.Companion.TAG
import com.easyEats.ezeats.R
import com.easyEats.ezeats.base.BaseFragment
import com.easyEats.ezeats.databinding.FragmentSignInScreenBinding
import com.easyEats.ezeats.ui.auth.main.AuthViewModel
import com.easyEats.ezeats.ui.auth.sign_in.google_facebook_login.FacebookLoginHelper
import com.easyEats.ezeats.ui.auth.sign_in.google_facebook_login.GoogleLoginHelper
import com.easyEats.ezeats.ui.auth.sign_in.google_facebook_login.SocialLoginCallback
import com.easyEats.ezeats.utils.Resource
import com.easyEats.ezeats.utils.validation.ValidateFields
import com.parse.ParseUser
import dagger.hilt.android.AndroidEntryPoint
import es.dmoral.toasty.Toasty

@AndroidEntryPoint
class SignInScreenFragment : BaseFragment<FragmentSignInScreenBinding>() {

    private val signInViewModel: SignInViewModel by viewModels()
    private val authViewModel: AuthViewModel by activityViewModels()

    private lateinit var googleLoginHelper: GoogleLoginHelper
    private lateinit var facebookLoginHelper: FacebookLoginHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        googleLoginHelper = GoogleLoginHelper(this, object : SocialLoginCallback {
            override fun onLoginSuccess(phoneOrEmail: String) {
                Log.d("LoginFragment", "Google Login Success: $phoneOrEmail")
                Toasty.success(requireContext(), "Google Login Success", Toast.LENGTH_SHORT).show()
            }

            override fun onLoginError(errorMessage: String) {
                Log.e("LoginFragment", "Google Login Error: $errorMessage")
                Toasty.error(requireContext(), "Google Login Error: $errorMessage", Toast.LENGTH_SHORT).show()
            }
        })

        facebookLoginHelper = FacebookLoginHelper(this, object : SocialLoginCallback {
            override fun onLoginSuccess(phoneOrEmail: String) {
                Log.d("LoginFragment", "Facebook Login Success: $phoneOrEmail")
            }

            override fun onLoginError(errorMessage: String) {
                Log.e("LoginFragment", "Facebook Login Error: $errorMessage")
            }
        })
    }


    private fun onGoogleLoginClick() {
        googleLoginHelper.signIn()
    }

    fun onFacebookLoginClick() {
//        facebookLoginHelper.signIn()
    }


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
                    authViewModel.sessionToken.value = signInViewModel.userSessionToken
                    goToVerificationScreen()
                    binding.btnSignIn.isEnabled = true
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
            binding.btnSignIn.isEnabled = false
            signInViewModel.findUser()
        }

        binding.btnGmail.setOnClickListener {
            onGoogleLoginClick()
        }
        binding.btnFacebook.setOnClickListener {
            onFacebookLoginClick()
        }
    }

    override fun onDestroyView() {
        signInViewModel.sendOtpLiveData.value = null
        signInViewModel.isOtpSent(false)
        super.onDestroyView()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == GoogleLoginHelper.GOOGLE_SIGN_IN_REQUEST_CODE) {
            googleLoginHelper.handleSignInResult(data)
        } else {
//            facebookLoginHelper.handleActivityResult(requestCode, resultCode, data)
        }
    }
}

