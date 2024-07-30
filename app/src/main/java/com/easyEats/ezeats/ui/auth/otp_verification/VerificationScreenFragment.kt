package com.easyEats.ezeats.ui.auth.otp_verification


import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.easyEats.ezeats.base.BaseFragment
import com.easyEats.ezeats.databinding.FragmentVerificationScreenBinding
import com.easyEats.ezeats.ui.main.main.HomeActivity
import com.easyEats.ezeats.utils.Resource
import com.easyEats.ezeats.utils.ValidateFields
import dagger.hilt.android.AndroidEntryPoint


/**
 * VerificationScreenFragment is responsible for handling the OTP verification process.
 * It interacts with the VerificationViewModel to verify OTPs and resend them if necessary.
 */
@AndroidEntryPoint
class VerificationScreenFragment : BaseFragment<FragmentVerificationScreenBinding>() {

    private val verificationViewModel: VerificationViewModel by viewModels()
    private val args: VerificationScreenFragmentArgs by navArgs()

    /**
     * Inflates the view binding for this fragment.
     *
     * @param inflater LayoutInflater to use for inflating the view.
     * @param container Optional parent view that the fragment's UI should be attached to.
     * @return The inflated view binding instance.
     */
    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentVerificationScreenBinding.inflate(inflater, container, false)

    /**
     * Called to initialize the fragment's views.
     * Sets up the observers and click listeners.
     */
    override fun setUpViews() {
        setUpObservers()
        setUpClickListeners()
    }

    /**
     * Sets up click listeners for the verify button and resend OTP text view.
     */
    private fun setUpClickListeners() {
        binding.btnVerify.setOnClickListener {
            verificationViewModel.verifyOtp(args.phonrNumber, binding.etOTP.text.toString())
        }

        binding.tvResendOtp.setOnClickListener {
            verificationViewModel.sendOtp(args.phonrNumber)
        }
    }

    /**
     * Sets up observers for the ViewModel's LiveData objects.
     */
    private fun setUpObservers() {
        ValidateFields.listenToOtpChange(verificationViewModel, binding.etOTP)

        // Set up OTP validity observer
        ValidateFields.observeOtpValidity(
            verificationViewModel,
            binding.btnVerify,
            requireContext(),
            viewLifecycleOwner
        )

        verificationViewModel.verifyOtpLiveData.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Loading -> {
                    showLoading()
                }

                is Resource.Success -> {
                    hideLoading()
                    verificationViewModel.saveUserVerification(true)
                    goToHomeScreen()
                }

                is Resource.Error -> {
                    hideLoading()
                }
            }
        }

        verificationViewModel.sendOtpLiveData.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Loading -> {
                    showLoading()
                }

                is Resource.Success -> {
                    hideLoading()
                }

                is Resource.Error -> {
                    hideLoading()
                }
            }
        }
    }

    private fun goToHomeScreen() {
        startActivity(Intent(requireContext(), HomeActivity::class.java))
        requireActivity().finish()
    }
}