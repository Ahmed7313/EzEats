package com.easyEats.ezeats.ui.auth.sign_in

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.easyEats.ezeats.MyApplication.Companion.TAG
import com.easyEats.ezeats.R
import com.easyEats.ezeats.base.BaseFragment
import com.easyEats.ezeats.databinding.FragmentSignInScreenBinding
import com.easyEats.ezeats.ui.auth.main.AuthViewModel
import com.easyEats.ezeats.ui.auth.sign_in.google_facebook_login.FacebookLoginHelper
import com.easyEats.ezeats.ui.auth.sign_in.google_facebook_login.GoogleLoginHelper
import com.easyEats.ezeats.ui.auth.sign_in.google_facebook_login.SocialLoginCallback
import com.easyEats.ezeats.ui.main.main.HomeActivity
import com.easyEats.ezeats.utils.Resource
import com.easyEats.ezeats.utils.validation.ValidateFields
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.parse.ParseUser
import dagger.hilt.android.AndroidEntryPoint
import es.dmoral.toasty.Toasty
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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
                Toasty.error(
                    requireContext(),
                    "Google Login Error: $errorMessage",
                    Toast.LENGTH_SHORT
                ).show()
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
//            onGoogleLoginClick()
            gmail2()
        }
        binding.btnFacebook.setOnClickListener {
            onFacebookLoginClick()
        }
    }


    private val activityResultGoogleSignIn = registerForActivityResult(
        ActivityResultContracts.StartIntentSenderForResult()
    ) { result ->
        when (result.resultCode) {
            Activity.RESULT_OK -> {
                try {
                    val credential = Identity.getSignInClient(requireActivity())
                        .getSignInCredentialFromIntent(result.data)

                    val idToken = credential.googleIdToken
                    val email = credential.id

                    if (idToken != null) {
                        Log.d(TAG, "Email: $email")
                        Log.d(TAG, "googleIdToken: ${credential.googleIdToken}")
                        Log.d(TAG, "id: ${credential.id}")
//                        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
//                            signInViewModel.socialLogin(credential.googleIdToken.toString(), email).collect { result ->
//                                withContext(Dispatchers.Main) {
//                                    when (result) {
//                                        is Resource.Success -> {
//                                            Log.d(TAG, "Social login successful")
//                                            // Handle success - maybe navigate to next screen
//                                        }
//
//                                        is Resource.Error -> {
//                                            Log.e(TAG, "Social login failed: ${result.message}")
//                                            Toasty.error(
//                                                requireContext(),
//                                                result.message,
//                                                Toast.LENGTH_SHORT
//                                            ).show()
//                                        }
//
//                                        is Resource.Loading -> {
//                                            // Show loading indicator
//                                        }
//
//                                        is Resource.UnAuthenticated -> {
//                                            // Handle unauthenticated state
//                                        }
//
//                                        else -> {}
//                                    }
//                                }
//                            }
//                        }

                    } else {
                        Log.e(TAG, "Google ID token is null")
                        Toasty.error(requireContext(), "Sign in failed", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: ApiException) {
                    Log.e(TAG, "Google sign in failed: ${e.message}", e)
                    Toasty.error(
                        requireContext(),
                        "Sign in failed: ${e.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            Activity.RESULT_CANCELED -> {
                Log.d(TAG, "Sign in cancelled")
                Toasty.info(requireContext(), "Sign in cancelled", Toast.LENGTH_SHORT).show()
            }
        }
    }


    fun gmail() {
        val signInRequest = BeginSignInRequest.builder()
            .setGoogleIdTokenRequestOptions(
                BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                    .setSupported(true)
                    .setServerClientId(getString(R.string.default_web_client_id))
                    .setFilterByAuthorizedAccounts(false)
                    .build()
            )
            .build()

        Identity.getSignInClient(requireActivity())
            .beginSignIn(signInRequest)
            .addOnSuccessListener { result ->
                try {
                    val intentSenderRequest =
                        IntentSenderRequest.Builder(result.pendingIntent).build()
                    activityResultGoogleSignIn.launch(intentSenderRequest)
                } catch (e: Exception) {
                    Log.e(TAG, "Failed to start sign in: ${e.message}", e)
                    Toasty.error(requireContext(), "Sign in failed", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { e ->
                Log.e(TAG, "Sign in failed: ${e.message}", e)
                Toasty.error(requireContext(), "Sign in failed", Toast.LENGTH_SHORT).show()
            }
    }

    private lateinit var googleSignInClient: GoogleSignInClient

    private val RC_SIGN_IN = 1001
    private val firebaseAuth = FirebaseAuth.getInstance()


    // New activity result API
    private val signInLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            handleSignInResult(task)
        }
    }

    fun gmail2() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .requestIdToken(getString(R.string.default_web_client_id)) // For backend verification
            .requestServerAuthCode(getString(R.string.default_web_client_id)) // For exchange to access token
            .build()

        googleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)
        val signInIntent = googleSignInClient.signInIntent
        signInLauncher.launch(signInIntent);
    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)

            // You can get these
            val idToken = account.idToken
            val authCode = account.serverAuthCode
            val email = account.email


            Log.d("GoogleSignIn", "Email: $email")
            Log.d("GoogleSignIn", "ID Token: $idToken")
            Log.d("GoogleSignIn", "Auth Code: $authCode")

            val credential = GoogleAuthProvider.getCredential(idToken, null)
            firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(requireActivity()) { authResult ->
                    if (authResult.isSuccessful) {
                        val user = firebaseAuth.currentUser
                        Log.d("GoogleSignIn", "✅ Firebase sign in successful")
                        Log.d("GoogleSignIn", "Name: ${user?.displayName}")
                        Log.d("GoogleSignIn", "Email: ${user?.email}")
                        Log.d("GoogleSignIn", "Phone: ${user?.phoneNumber}")
                        Log.d("GoogleSignIn", "uid: ${user?.uid}")
                        //const {token, googleId, email} = request.params;
                        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
                            email?.let { nonNullEmail ->
                                signInViewModel.socialLogin(idToken!!, authCode!!, nonNullEmail)
                                    .collect {
                                        if (it is Resource.Success<String>) {
                                            val intent = Intent(
                                                requireContext(),
                                                HomeActivity::class.java
                                            )
                                            requireActivity().finishAffinity()
                                            startActivity(
                                             intent
                                            )
                                        } else if (it is Resource.Loading) {
                                            showLoading()
                                        } else if (it is Resource.Error) {
                                            hideLoading()
                                            Toast.makeText(context, it.message, Toast.LENGTH_SHORT)
                                                .show()
                                        } else {
                                            hideLoading()
                                        }
                                    }
                            } ?: run {
                                Log.e(
                                    "GoogleSignIn",
                                    "Email is null, cannot proceed with social login"
                                )
                                Toasty.error(
                                    requireContext(),
                                    "Email not available from Google account",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }


                    } else {
                        Log.e("GoogleSignIn", "❌ Firebase sign in failed", authResult.exception)
                    }
                }


            // Send authCode to backend to get access token
        } catch (e: ApiException) {
            Log.e("GoogleSignIn", "Sign-in failed: ${e.statusCode}", e)
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

