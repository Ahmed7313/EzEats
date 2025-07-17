package com.easyEats.ezeats.ui.main.profile.logout


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.easyEats.ezeats.R
import com.easyEats.ezeats.ui.main.profile.ProfileViewModel
import com.easyEats.ezeats.ui.main.restaurant.CartViewModel
import com.google.android.material.button.MaterialButton
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kotlin.getValue

/**
 * A DialogFragment that displays a confirmation dialog for logging out.
 * It contains two buttons: Log Out and Cancel.
 * The fragment uses a callback interface to notify the parent fragment or activity about the user's action.
 */
@AndroidEntryPoint
class LogOutFragment : DialogFragment() {

    private val cartViewModel: CartViewModel by viewModels()

    // Interface for communicating the log out action to the parent
    interface DialogCallback {
        /**
         * Called when the user clicks the "Log Out" button.
         */
        fun onLogOutClicked()

        /**
         * Called when the user clicks the "Cancel" button.
         */
        fun onCancelClicked()
    }

    // The callback instance that will handle the actions in the parent
    private var callback: DialogCallback? = null

    /**
     * Sets the callback to handle the log out and cancel actions.
     *
     * @param callback The callback implementation to handle the actions.
     */
    fun setDialogCallback(callback: DialogCallback?) {
        this.callback = callback
    }

    /**
     * Inflates the layout for the dialog fragment and applies a custom style.
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Set the custom style for the dialog
        setStyle(STYLE_NORMAL, R.style.RoundedDialog)
        return inflater.inflate(R.layout.fragment_log_out, container, false)
    }

    /**
     * Sets up the click listeners for the buttons in the dialog.
     * The "Log Out" button triggers the log out action, and the "Cancel" button dismisses the dialog.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Find the buttons by their IDs
        val btnLogOut: MaterialButton = view.findViewById(R.id.btn_log_out)
        val btnCancel: MaterialButton = view.findViewById(R.id.btn_cancel)

        // Set the click listener for the "Log Out" button
        btnLogOut.setOnClickListener {
            // Notify the parent about the log out action
            callback?.onLogOutClicked()
            //dismissCart
            viewLifecycleOwner.lifecycleScope.launch {
                cartViewModel.clearShop()
            }
            dismiss()  // Dismiss the dialog
        }

        // Set the click listener for the "Cancel" button
        btnCancel.setOnClickListener {
            // Notify the parent about the cancel action
            callback?.onCancelClicked()
            dismiss()  // Dismiss the dialog
        }
    }

    /**
     * Called when the dialog is starting. Sets the dialog window size and background.
     */
    override fun onStart() {
        super.onStart()
        // Set the dialog's width and height to wrap content
        dialog?.window?.setLayout(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        // Set the background of the dialog to transparent
        dialog?.window?.setBackgroundDrawableResource(android.R.color.transparent)
    }

}

