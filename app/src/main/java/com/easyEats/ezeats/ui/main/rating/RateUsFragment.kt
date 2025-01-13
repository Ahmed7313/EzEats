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

@AndroidEntryPoint
class RateUsFragment : DialogFragment() {
    private lateinit var binding: FragmentRateUsBinding

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
        binding= FragmentRateUsBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.btnRateUs.setOnClickListener {
            callback?.onRateUsClicked()
        }

        binding.btnLater.setOnClickListener {
            dismiss()
        }
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setBackgroundDrawableResource(R.drawable.rounded_dialog_background)


    }

}
