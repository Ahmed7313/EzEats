package com.easyEats.ezeats.ui.main.redeem

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.easyEats.ezeats.MyApplication.Companion.TAG
import com.easyEats.ezeats.R
import com.easyEats.ezeats.base.BaseBottomSheetFragment
import com.easyEats.ezeats.databinding.FragmentRedeemBinding
import com.easyEats.ezeats.utils.Resource
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class RedeemFragment : BaseBottomSheetFragment<FragmentRedeemBinding>() {

    private val redeemViewModel: RedeemViewModel by viewModels()

    override fun setUpViews() {
    }

    override fun listenToObservers() {
        redeemViewModel.offerResultLiveData.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Error -> {}
                is Resource.Loading -> {
                    binding.loader.visibility = VISIBLE
                }

                is Resource.Success -> {
                    binding.offer = resource.data?.result?.data
                    binding.loader.visibility = GONE
                }

                is Resource.UnAuthenticated -> {}
            }

        }
    }

    override fun inflateBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentRedeemBinding.inflate(layoutInflater)


}