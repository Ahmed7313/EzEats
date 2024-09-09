package com.easyEats.ezeats.ui.main.delete_account

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.DialogFragment
import com.easyEats.ezeats.R
import com.easyEats.ezeats.databinding.DialogDeleteAccountBinding


class DeleteAccountDialogFragment : DialogFragment() {

    private lateinit var binding: DialogDeleteAccountBinding

    // Define callback interface
    interface DialogCallback {
        fun onDeleteClicked()
        fun onCancelClicked()
    }

    private var callback: DialogCallback? = null

    // Attach the callback
    fun setDialogCallback(callback: DialogCallback) {
        this.callback = callback
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogDeleteAccountBinding.inflate(layoutInflater)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnDelete.setOnClickListener {
            callback?.onDeleteClicked()
            dismiss()
        }

        binding.btnCancel.setOnClickListener {
            callback?.onCancelClicked()
            dismiss()
        }
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }
}
