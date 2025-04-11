package com.easyEats.ezeats.ui.main.profile.logout


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.easyEats.ezeats.R
import com.google.android.material.button.MaterialButton
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LogOutFragment : DialogFragment() {

    interface DialogCallback {
        fun onLogOutClicked()
        fun onCancelClicked()
    }

    private var callback: DialogCallback? = null

    fun setDialogCallback(callback: DialogCallback?) {
        this.callback = callback
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setStyle(STYLE_NORMAL, R.style.RoundedDialog)
        return inflater.inflate(R.layout.fragment_log_out, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val btnLogOut: MaterialButton = view.findViewById(R.id.btn_log_out)
        val btnCancel: MaterialButton = view.findViewById(R.id.btn_cancel)

        btnLogOut.setOnClickListener {
            callback?.onLogOutClicked()
            dismiss()
        }

        btnCancel.setOnClickListener {
            callback?.onCancelClicked()
            dismiss()
        }
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        dialog?.window?.setBackgroundDrawableResource(android.R.color.transparent)
    }

}
