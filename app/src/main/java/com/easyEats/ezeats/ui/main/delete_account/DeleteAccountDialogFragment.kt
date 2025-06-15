package com.easyEats.ezeats.ui.main.delete_account

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.easyEats.ezeats.R
import com.easyEats.ezeats.databinding.DialogDeleteAccountBinding

/**
 * A dialog fragment that displays a delete account confirmation dialog.
 * The dialog includes options to delete or cancel the delete operation.
 */
class DeleteAccountDialogFragment : DialogFragment() {

    // Binding object to access views in the dialog layout
    private lateinit var binding: DialogDeleteAccountBinding

    /**
     * Callback interface to communicate actions back to the parent fragment or activity.
     */
    interface DialogCallback {
        /**
         * Called when the delete button is clicked.
         */
        fun onDeleteClicked()

        /**
         * Called when the cancel button is clicked.
         */
        fun onCancelClicked()
    }

    // Callback instance to handle dialog button actions
    private var callback: DialogCallback? = null

    /**
     * Sets the dialog callback to allow communication with the parent component.
     * @param callback an implementation of DialogCallback to handle dialog actions
     */
    fun setDialogCallback(callback: DialogCallback) {
        this.callback = callback
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout using view binding
        binding = DialogDeleteAccountBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set click listener for delete button, invoke callback, and dismiss the dialog
        binding.btnDelete.setOnClickListener {
            callback?.onDeleteClicked()
            dismiss()
        }

        // Set click listener for cancel button, invoke callback, and dismiss the dialog
        binding.btnCancel.setOnClickListener {
            callback?.onCancelClicked()
            dismiss()
        }
    }

    override fun onStart() {
        super.onStart()
        // Set dialog width to match the parent width, and height to wrap content
        dialog?.window?.setLayout(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        dialog?.window?.setBackgroundDrawableResource(R.drawable.dialog_background)

    }
}
