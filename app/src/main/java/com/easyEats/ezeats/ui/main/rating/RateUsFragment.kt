package com.easyEats.ezeats.ui.main.rating

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.easyEats.ezeats.R
import com.google.android.material.button.MaterialButton
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RateUsFragment : DialogFragment() {

    interface DialogCallback {
        fun onRateUsClicked()
        fun onRateLaterClicked()
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
        return inflater.inflate(R.layout.fragment_rate_us, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val btnRate: MaterialButton = view.findViewById(R.id.btn_rate_us)
        val btnLater: MaterialButton = view.findViewById(R.id.btn_later)

        btnRate.setOnClickListener {
            callback?.onRateUsClicked()
        }

        btnLater.setOnClickListener {
            callback?.onRateLaterClicked()
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
