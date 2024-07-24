package com.easyEats.ezeats.ui.auth.otp_verification


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.easyEats.ezeats.base.BaseFragment
import com.easyEats.ezeats.databinding.FragmentVerificationScreenBinding
import com.easyEats.ezeats.utils.Resource
import com.easyEats.ezeats.utils.ValidateFields
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class VerificationScreenFragment : BaseFragment<FragmentVerificationScreenBinding>() {

    private val verificationViewModel: VerificationViewModel by viewModels()
    private val args: VerificationScreenFragmentArgs by navArgs()

    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentVerificationScreenBinding.inflate(inflater, container, false)

    override fun setUpViews() {
        setUpObservers()
        setUpClickListeners()
    }

    private fun setUpClickListeners() {
        binding.btnVerify.setOnClickListener {
            verificationViewModel.verifyOtp(args.phonrNumber, binding.etOTP.text.toString())
        }

        binding.tvResendOtp.setOnClickListener {
            verificationViewModel.sendOtp(args.phonrNumber)
        }
    }

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

}