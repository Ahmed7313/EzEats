package com.easyEats.ezeats.ui.main.complaint

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.easyEats.ezeats.MyApplication.Companion.TAG
import com.easyEats.ezeats.R
import com.easyEats.ezeats.databinding.FragmentComplaintBinding
import dagger.hilt.android.AndroidEntryPoint



/**
 * A DialogFragment to allow users to submit complaints.
 * This fragment captures the user's full name, email, and complaint message.
 * Upon submission, it calls a callback function to handle the complaint.
 */
@AndroidEntryPoint
class ComplaintFragment : DialogFragment() {

    private lateinit var binding: FragmentComplaintBinding

    /**
     * Interface for callback to handle complaint submission and confirmation actions.
     */
    interface DialogCallback {
        fun onSubmitComplaint(
            name: String, email: String, message: String,
            complaintSavedSuccessfully: (Boolean) -> Unit
        )

    }

    // Reference to the callback implementation
    private var callback: DialogCallback? = null

    /**
     * Sets the callback for dialog actions.
     *
     * @param callback The DialogCallback implementation.
     */
    fun setDialogCallback(callback: DialogCallback?) {
        this.callback = callback
        Log.d(TAG, "setDialogCallback: ${this.callback.hashCode()}")
    }

    /**
     * Inflates the complaint dialog layout and applies the rounded dialog style.
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentComplaintBinding.inflate(layoutInflater)
        setStyle(STYLE_NORMAL, R.style.RoundedDialog)
        return binding.root
    }

    /**
     * Sets up click listeners for submitting and confirming the complaint.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Submit button click listener
        binding.btnSubmitComplaint.setOnClickListener {
            if (isFullNameNotEmpty() && isEmailNotEmpty() && isComplaintNotEmpty()) {
                setDialogCallback(callback)
                callback?.onSubmitComplaint(
                    binding.tvFullName.text.toString(),
                    binding.tvEmail.text.toString(),
                    binding.tvComplaint.text.toString()
                ) { isComplaintSentSuccessfully ->
                    if (isComplaintSentSuccessfully) {
                        binding.llEnterComplaint.visibility = GONE
                        binding.llComplainSuccess.visibility = VISIBLE
                    }
                }
            }
        }

        // Confirm button click listener
        binding.btnConfirm.setOnClickListener {
            dismiss()
        }
    }

    /**
     * Sets the dialog window dimensions on start.
     */
    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }

    /**
     * Validates if the full name field is not empty.
     *
     * @return True if the full name is filled, false otherwise.
     */
    private fun isFullNameNotEmpty(): Boolean {
        if (binding.tvFullName.text?.isEmpty() == true)
            Toast.makeText(requireContext(), "Full Name is required", Toast.LENGTH_LONG).show()
        return binding.tvFullName.text?.isNotEmpty() ?: false
    }

    /**
     * Validates if the email field is not empty.
     *
     * @return True if the email is filled, false otherwise.
     */
    private fun isEmailNotEmpty(): Boolean {
        if (binding.tvEmail.text?.isEmpty() == true)
            Toast.makeText(requireContext(), "Email is required", Toast.LENGTH_LONG).show()
        return binding.tvEmail.text?.isNotEmpty() ?: false
    }

    /**
     * Validates if the complaint message field is not empty.
     *
     * @return True if the complaint message is filled, false otherwise.
     */
    private fun isComplaintNotEmpty(): Boolean {
        if (binding.tvComplaint.text?.isEmpty() == true)
            Toast.makeText(requireContext(), "Complain Field is required", Toast.LENGTH_LONG).show()
        return binding.tvComplaint.text?.isNotEmpty() ?: false
    }
}
