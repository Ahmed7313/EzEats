package com.easyEats.ezeats.ui.auth.sign_up

import android.app.Activity
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.credentials.GetCredentialRequest
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.easyEats.ezeats.MyApplication
import com.easyEats.ezeats.R
import com.easyEats.ezeats.base.BaseFragment
import com.easyEats.ezeats.databinding.FragmentSignUpScreenBinding
import com.easyEats.ezeats.ui.auth.sign_in.SignInViewModel
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
import dagger.hilt.android.AndroidEntryPoint
import es.dmoral.toasty.Toasty
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


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
    private val signInViewModel: SignInViewModel by viewModels()
    private  val TAG = "SignUpScreenFragment"
    private val firebaseAuth = FirebaseAuth.getInstance()
    private lateinit var googleSignInClient: GoogleSignInClient


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
//        googleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)

        // Navigate to Sign In screen when "Sign In" text view is clicked
        binding.tvSignIn.setOnClickListener {
            findNavController().navigate(R.id.action_signUpScreenFragment_to_signInScreenFragment)
        }
        binding.btnSignUp.setOnClickListener {
            binding.btnSignUp.isEnabled=false
            signUpViewModel.register()
        }
        binding.imgGoogle.setOnClickListener {

            gmail2()


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


    private val signInLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            handleSignInResult(task)
        }
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
        signUpViewModel.registrationResult.value = null
        signUpViewModel.sendOtpLiveData.value = null
        signUpViewModel.isOtpSent(false)
        super.onDestroyView()
    }
}