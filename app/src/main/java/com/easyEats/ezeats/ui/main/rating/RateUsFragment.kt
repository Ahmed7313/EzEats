package com.easyEats.ezeats.ui.main.rating

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
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
import com.easyEats.ezeats.databinding.FragmentRateUsBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class RateUsFragment : DialogFragment() {

    private lateinit var binding: FragmentRateUsBinding

    interface DialogCallback {
        fun onRateUsClicked()
        fun onRateLaterClicked()
    }

    // Reference to the callback implementation
    private var callback: DialogCallback? = null


    fun setDialogCallback(callback: DialogCallback?) {
        this.callback = callback
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRateUsBinding.inflate(layoutInflater)
        setStyle(STYLE_NORMAL, R.style.RoundedDialog)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnRate.setOnClickListener {
            setDialogCallback(callback)
            callback?.onRateUsClicked()

        }

        binding.btnLater.setOnClickListener {
            callback?.onRateLaterClicked()
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