package com.easyEats.ezeats.ui.main.voucher

import android.util.Log
import android.view.LayoutInflater
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.easyEats.ezeats.MyApplication.Companion.TAG
import com.easyEats.ezeats.base.BaseFragment
import com.easyEats.ezeats.databinding.FragmentVoucherBinding
import com.easyEats.ezeats.ui.main.voucher.adapter.VoucherAdapter
import com.easyEats.ezeats.utils.Resource
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class VoucherFragment : BaseFragment<FragmentVoucherBinding>() {
    private val viewModel: VoucherViewModel by viewModels()
    private val adapter = VoucherAdapter()

    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentVoucherBinding.inflate(layoutInflater)

    override fun setUpViews() {
        binding.ivBack.setOnClickListener {
            findNavController().popBackStack()
        }
        listenToObserver()
    }


    private fun listenToObserver() {
        viewModel.voucherResultLiveData.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Error -> {
                    hideMainLoading()
                    Log.d(TAG, "listenToObserver: ${resource.message}")
                }

                is Resource.Loading -> {
                    showMainLoading()
                }

                is Resource.Success -> {
                    hideMainLoading()
                    binding.rvVouchers.adapter = adapter
                    adapter.submitList(resource.data?.result?.data)
                    if (resource.data?.result?.data?.size != 0)
                        binding.llEmptyOrderHistory.visibility=GONE
                    else
                        binding.llEmptyOrderHistory.visibility=VISIBLE
                }

                is Resource.UnAuthenticated -> {
                    hideMainLoading()
                }
            }
        }
    }

}