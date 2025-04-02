package com.easyEats.ezeats.ui.main.rating

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.easyEats.ezeats.R
import com.easyEats.ezeats.databinding.FragmentRateUsBinding
import com.google.android.material.button.MaterialButton
import dagger.hilt.android.AndroidEntryPoint
/**
 * A DialogFragment that displays a "Rate Us" dialog to the user with options to rate now or later.
 * The fragment communicates with the parent activity or fragment using a callback interface.
 */
@AndroidEntryPoint
class RateUsFragment : DialogFragment() {

    // Binding instance for accessing the views in the fragment layout.
    private lateinit var binding: FragmentRateUsBinding

    /**
     * Interface for handling the dialog actions (rate now or later).
     */
    interface DialogCallback {
        /**
         * Called when the "Rate Us" button is clicked.
         */
        fun onRateUsClicked()

        /**
         * Called when the "Rate Later" button is clicked.
         */
        fun onRateLaterClicked()
    }

    // Variable to hold the reference to the DialogCallback.
    private var callback: DialogCallback? = null

    /**
     * Sets the DialogCallback to communicate with the parent fragment/activity.
     *
     * @param callback The callback to handle dialog interactions.
     */
    fun setDialogCallback(callback: DialogCallback?) {
        this.callback = callback
    }

    /**
     * Inflates the layout for this dialog fragment and returns the root view.
     *
     * @param inflater The LayoutInflater used to inflate the layout.
     * @param container The parent view that the fragment's UI will be attached to.
     * @param savedInstanceState The previous saved state, if any.
     * @return The root view of the dialog fragment's layout.
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this dialog fragment
        binding = FragmentRateUsBinding.inflate(layoutInflater)
        return binding.root
    }

    /**
     * Sets up the click listeners for the "Rate Us" and "Rate Later" buttons.
     *
     * @param view The root view of the fragment's layout.
     * @param savedInstanceState The previous saved state, if any.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Handle "Rate Us" button click
        binding.btnRateUs.setOnClickListener {
            // Notify the callback when the "Rate Us" button is clicked
            callback?.onRateUsClicked()
        }

        // Handle "Rate Later" button click
        binding.btnLater.setOnClickListener {
            // Dismiss the dialog when "Rate Later" is clicked
            dismiss()
        }
    }

    /**
     * Sets the dialog's background drawable when the dialog starts.
     */
    override fun onStart() {
        super.onStart()
        // Apply a custom rounded background to the dialog
        dialog?.window?.setBackgroundDrawableResource(R.drawable.rounded_dialog_background)
    }
}

