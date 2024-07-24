package com.easyEats.ezeats.utils

import android.content.Context
import android.content.res.ColorStateList
import android.text.Editable
import android.text.TextWatcher
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.chaos.view.PinView
import com.easyEats.ezeats.R
import com.easyEats.ezeats.ui.auth.main.AuthViewModel
import com.easyEats.ezeats.ui.auth.sign_up.SignUpViewModel
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
object ValidateFields {

    /**
     * Sets up OTP validity observation on the button.
     *
     * @param viewModel The ViewModel that contains OTP validity LiveData.
     * @param button The button whose enabled state will be updated based on OTP validity.
     * @param context The context to get color resources.
     * @param lifecycleOwner The LifecycleOwner to observe the LiveData.
     */
    fun observeOtpValidity(
        viewModel: AuthViewModel,
        button: MaterialButton,
        context: Context,
        lifecycleOwner: LifecycleOwner
    ) {
        viewModel.isOtpValid.observe(lifecycleOwner, Observer { isValid ->
            button.isEnabled = isValid
            val color = if (isValid) {
                ContextCompat.getColor(context, R.color.main_color)
            } else {
                ContextCompat.getColor(context, R.color.light_gray)
            }
            button.backgroundTintList = ColorStateList.valueOf(color)
        })
    }

    /**
     * Adds a text change listener to the OTP input field.
     *
     * @param viewModel The ViewModel to call the validation function.
     * @param pinView The PinView to listen for text changes.
     */
    fun listenToOtpChange(
        viewModel: AuthViewModel,
        pinView: PinView
    ) {
        pinView.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                // No action needed after text changes
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // No action needed before text changes
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.validateOtp(s.toString())
            }
        })
    }

    // Other existing methods...

    /**
     * Sets up phone number validity observation on the button.
     *
     * @param viewModel The ViewModel that contains phone number validity LiveData.
     * @param button The button whose enabled state will be updated based on phone number validity.
     * @param context The context to get color resources.
     * @param lifecycleOwner The LifecycleOwner to observe the LiveData.
     */
    fun observePhoneNumberValidity(
        viewModel: AuthViewModel,
        button: MaterialButton,
        context: Context,
        lifecycleOwner: LifecycleOwner
    ) {
        viewModel.isPhoneNumberValid.observe(lifecycleOwner, Observer { isValid ->
            button.isEnabled = isValid
            val color = if (isValid) {
                ContextCompat.getColor(context, R.color.main_color)
            } else {
                ContextCompat.getColor(context, R.color.light_gray)
            }
            button.backgroundTintList = ColorStateList.valueOf(color)
        })
    }

    /**
     * Adds a text change listener to the phone number input field.
     *
     * @param viewModel The ViewModel to call the validation function.
     * @param editText The EditText to listen for text changes.
     */
    fun listenToPhoneNumberChange(
        viewModel: AuthViewModel,
        editText: TextInputEditText
    ) {
        editText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                // No action needed after text changes
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // No action needed before text changes
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.validatePhoneNumber(s.toString())
            }
        })
    }

    /**
     * Sets up form validity observation on the button for SignUpViewModel.
     *
     * @param viewModel The ViewModel that contains form validity LiveData.
     * @param button The button whose enabled state will be updated based on form validity.
     * @param context The context to get color resources.
     * @param lifecycleOwner The LifecycleOwner to observe the LiveData.
     */
    fun observeFormValidity(
        viewModel: SignUpViewModel,
        button: MaterialButton,
        context: Context,
        lifecycleOwner: LifecycleOwner
    ) {
        viewModel.isFormValid.observe(lifecycleOwner, Observer { isValid ->
            button.isEnabled = isValid
            val color = if (isValid) {
                ContextCompat.getColor(context, R.color.main_color)
            } else {
                ContextCompat.getColor(context, R.color.light_gray)
            }
            button.backgroundTintList = ColorStateList.valueOf(color)
        })
    }

    /**
     * Adds a text change listener to the name input field for SignUpViewModel.
     *
     * @param viewModel The ViewModel to call the validation function.
     * @param editText The EditText to listen for text changes.
     */
    fun listenToNameChange(
        viewModel: SignUpViewModel,
        editText: TextInputEditText
    ) {
        editText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                // No action needed after text changes
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // No action needed before text changes
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.validateName(s.toString())
            }
        })
    }
}




