package com.easyEats.ezeats.ui.auth.otp_verification


import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.CountDownTimer
import android.util.Log
import android.view.LayoutInflater
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.easyEats.ezeats.MyApplication.Companion.TAG
import com.easyEats.ezeats.R
import com.easyEats.ezeats.base.BaseFragment
import com.easyEats.ezeats.databinding.FragmentVerificationScreenBinding
import com.easyEats.ezeats.ui.main.main.HomeActivity
import com.easyEats.ezeats.utils.Resource
import com.easyEats.ezeats.utils.validation.ValidateFields
import dagger.hilt.android.AndroidEntryPoint
import es.dmoral.toasty.Toasty


/**
 * VerificationScreenFragment is responsible for handling the OTP verification process.
 * It interacts with the VerificationViewModel to verify OTPs and resend them if necessary.
 */
@AndroidEntryPoint
class VerificationScreenFragment : BaseFragment<FragmentVerificationScreenBinding>() {

    private val verificationViewModel: VerificationViewModel by viewModels()

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
        binding.verificationViewModel = verificationViewModel
        setUpObservers()
        startCountdown()
        binding.tvResendOtp.setOnClickListener {
            verificationViewModel.sendOtp()
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
                    goToHomeScreen()
                }

                is Resource.Error -> {
                    hideLoading()
                    Toasty.error(requireContext(), resource.message, Toast.LENGTH_LONG).show()
                }

                is Resource.UnAuthenticated -> {

                }
            }
        }

        verificationViewModel.sendOtpLiveData.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Loading -> {
                    showLoading()
                    startCountdown()
                }

                is Resource.Success -> {
                    hideLoading()
                    binding.tvResendOtp.isEnabled = true
                }

                is Resource.Error -> {
                    hideLoading()
                    Toasty.error(requireContext(), resource.message, Toast.LENGTH_LONG).show()
                }

                is Resource.UnAuthenticated -> {

                }
            }
        }

    }

    private fun startCountdown() {
        val countdownTime = 30000L // 30 seconds
        // Ensure views are initialized before the countdown starts
        binding?.let { binding ->
            binding.tvResendOtp.isEnabled = false
            binding.tvResendOtp.setTextColor(requireContext().getColor(R.color.light_gray))
            binding.tvTimer.text = "00:30" // Start with 30 seconds
            binding.tvTimer.visibility = VISIBLE
            object : CountDownTimer(countdownTime, 1000L) {
                @SuppressLint("SetTextI18n")
                override fun onTick(millisUntilFinished: Long) {
                    val secondsRemaining = millisUntilFinished / 1000

                    // Disable the "Resend OTP" button during the countdown
                    binding.tvResendOtp.isEnabled = false

                    // Update the timer text with proper formatting
                    binding.tvTimer.text = "00:${if (secondsRemaining < 10) "0$secondsRemaining" else secondsRemaining}"
                }

                override fun onFinish() {
                    // Enable the "Resend OTP" button and reset styles after the countdown ends
                    binding.tvResendOtp.isEnabled = true
                    binding.tvResendOtp.setTextColor(requireContext().getColor(R.color.main_color))
                    binding.tvTimer.visibility = GONE
                }
            }.start()
        } ?: run {
            Log.e("CountdownError", "Binding is null")
        }
    }


    private fun goToHomeScreen() {
        startActivity(Intent(requireContext(), HomeActivity::class.java))
        requireActivity().finish()
    }
}