package com.easyEats.ezeats.ui.auth.sign_up

import android.app.Activity
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


//    private lateinit var googleSignInClient: GoogleSignInClient
//    private val RC_SIGN_IN = 1001
//    private val signInLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
//        if (result.resultCode == Activity.RESULT_OK) {
//            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
//            handleSignInResult(task)
//        } else {
//            Toast.makeText(requireContext(), "Cancelled", Toast.LENGTH_SHORT).show()
//        }
//    }

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
//            val signInIntent = googleSignInClient.signInIntent
//            signInLauncher.launch(signInIntent)
//            gmail()
            gmail()


        }
    }

    fun gmail(){
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
                    val intentSenderRequest = IntentSenderRequest.Builder(result.pendingIntent).build()
                    activityResultGoogleSignIn.launch(intentSenderRequest)
                } catch (e: Exception) {
                    Log.e(MyApplication.Companion.TAG, "Failed to start sign in: ${e.message}", e)
                    Toasty.error(requireContext(), "Sign in failed", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { e ->
                Log.e(MyApplication.Companion.TAG, "Sign in failed: ${e.message}", e)
                Toasty.error(requireContext(), "Sign in failed", Toast.LENGTH_SHORT).show()
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
                        Log.d(MyApplication.Companion.TAG, "Email: $email")
                        Log.d(MyApplication.Companion.TAG, "googleIdToken: ${credential.googleIdToken}")
                        Log.d(MyApplication.Companion.TAG, "id: ${credential.id}")
                        signUpViewModel.socialLogin(credential.id, email)
                    } else {
                        Log.e(MyApplication.Companion.TAG, "Google ID token is null")
                        Toasty.error(requireContext(), "Sign in failed", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: ApiException) {
                    Log.e(MyApplication.Companion.TAG, "Google sign in failed: ${e.message}", e)
                    Toasty.error(requireContext(), "Sign in failed: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
            Activity.RESULT_CANCELED -> {
                Log.d(MyApplication.Companion.TAG, "Sign in cancelled")
                Toasty.info(requireContext(), "Sign in cancelled", Toast.LENGTH_SHORT).show()
            }
        }
    }



    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var firebaseAuth: FirebaseAuth


    private  val TAG = "SignUpScreenFragment"
    private val googleSignInLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
        try {
            val account = task.getResult(ApiException::class.java)
            val idToken = account.idToken
            val accessToken = account.serverAuthCode // Only available if requested
            Log.d("GoogleSignIn", "🟢 ID Token: $idToken")
            Log.d("GoogleSignIn", "🟢 Email: ${account.email}")

            if (idToken != null) {
                firebaseAuthWithGoogle(idToken, account.email)
            } else {
                Log.e("GoogleSignIn", "❌ ID Token is null")
            }

        } catch (e: ApiException) {
            Log.e("GoogleSignIn", "❌ Google sign in failed: ${e.localizedMessage}")
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String, email: String?) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)

        firebaseAuth.signInWithCredential(credential)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    val user = firebaseAuth.currentUser
                    val name = user?.displayName ?: ""
                    val email = user?.email ?: ""
                    val phone = user?.phoneNumber ?: ""

                    Log.d("FirebaseAuth", "✅ Sign-in success")
                    Log.d("FirebaseAuth", "Name: $name")
                    Log.d("FirebaseAuth", "Email: $email")
                    Log.d("FirebaseAuth", "Phone: $phone")

                    // TODO: Call your backend logic here
                    googleSignInBackendCall(idToken, email)

                } else {
                    Log.e("FirebaseAuth", "❌ Sign-in failed: ${task.exception?.localizedMessage}")
                }
            }
    }

    private fun googleSignInBackendCall(token: String, email: String?) {
        // Replace this with your actual API call
        Log.d("Backend", "📞 Sending token and email to backend: $token, $email")

        // Example pseudo-logic
        // if (success) {
        //     sendBroadcast or notify observers
        // } else {
        //     showError()
        // }
    }


    fun signInWithGoogle() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id)) // from google-services.json
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(requireContext(), gso)
        firebaseAuth = FirebaseAuth.getInstance()

        val signInIntent = googleSignInClient.signInIntent
        googleSignInLauncher.launch(signInIntent)
    }

//    fun gmail(){
//        Log.d(TAG, "gmail: here")
//        val options = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//            .requestId()
//            .requestEmail()
//            .requestProfile()
//            .build()
//        val googleClient = GoogleSignIn.getClient(requireActivity(), options)
//        activityResultGoogleSignIn.launch(googleClient.signInIntent)
//    }

    override fun onDestroyView() {
        signUpViewModel.registrationResult.value = null
        signUpViewModel.sendOtpLiveData.value = null
        signUpViewModel.isOtpSent(false)
        super.onDestroyView()
    }
}