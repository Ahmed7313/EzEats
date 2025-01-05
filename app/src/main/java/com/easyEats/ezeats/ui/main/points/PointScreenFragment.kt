package com.easyEats.ezeats.ui.main.points


import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.easyEats.ezeats.MyApplication.Companion.TAG
import com.easyEats.ezeats.R
import com.easyEats.ezeats.base.BaseFragment
import com.easyEats.ezeats.databinding.FragmentPointScreenBinding
import com.easyEats.ezeats.ui.main.points.adapter.PointsAdapter
import com.easyEats.ezeats.ui.main.points.adapter.SpinWheelAdapter
import com.easyEats.ezeats.ui.main.points.adapter.VoucherAdapter
import com.easyEats.ezeats.utils.Resource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch


@AndroidEntryPoint
class PointScreenFragment : BaseFragment<FragmentPointScreenBinding>() {
    private val pointsViewModel: PointsViewModel by viewModels()
    private val pointsAdapter by lazy(LazyThreadSafetyMode.NONE) {
        PointsAdapter()
    }

    private val spinWheelPointsAdapter by lazy(LazyThreadSafetyMode.NONE) {
        SpinWheelAdapter()
    }

    private val vouchersAdapter by lazy(LazyThreadSafetyMode.NONE) {
        VoucherAdapter()
    }

    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentPointScreenBinding.inflate(layoutInflater)

    override fun setUpViews() {
        listenToObserver()
        binding.btnSpinWheelPromoCodes.setOnClickListener {
            findNavController().navigate(PointScreenFragmentDirections.goToVoucherBottomSheet(true))
        }

        binding.tvSpinWheelPoints.setOnClickListener {
            findNavController().navigate(PointScreenFragmentDirections.goToVoucherBottomSheet(true))
        }

        binding.tvPoints.setOnClickListener {
            findNavController().navigate(PointScreenFragmentDirections.goToVoucherBottomSheet(false))
        }

        binding.tvAllPoints.setOnClickListener {
            findNavController().navigate(PointScreenFragmentDirections.goToVoucherBottomSheet(false))
        }
    }

    private fun listenToObserver() {
        lifecycleScope.launchWhenResumed {
            val token = pointsViewModel.getUserToken().first()
            if (token.isEmpty()) {
                findNavController().navigate(R.id.goToUserGuestScreen)
            }
        }

        pointsViewModel.pointsResultLiveData.observe(viewLifecycleOwner) { resource ->
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
                    binding.response = resource.data?.result?.data
                    binding.rvPoints.adapter = pointsAdapter
                    pointsAdapter.submitList(
                        resource.data?.result?.data?.points?.loyaltyPoints?.take(
                            3
                        )
                    )

                    binding.rvSpinWheelPromoCodes.adapter = spinWheelPointsAdapter
                    spinWheelPointsAdapter.submitList(
                        resource.data?.result?.data?.rewards?.spinWheelVouchers?.take(
                            3
                        )
                    )

                    binding.rvVouchers.adapter = vouchersAdapter
                    vouchersAdapter.submitList(resource.data?.result?.data?.rewards?.promocodes)
                }

                is Resource.UnAuthenticated -> {
                    hideMainLoading()

                }
            }

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        hideMainLoading()
    }

    override fun onPause() {
        super.onPause()
        hideMainLoading()
    }
}