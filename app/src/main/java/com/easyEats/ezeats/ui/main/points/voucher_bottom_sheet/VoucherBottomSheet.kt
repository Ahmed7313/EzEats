package com.easyEats.ezeats.ui.main.points.voucher_bottom_sheet


import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.easyEats.ezeats.base.BaseBottomSheetFragment
import com.easyEats.ezeats.databinding.VoucherBottomSheetBinding
import com.easyEats.ezeats.ui.main.points.PointsViewModel
import com.easyEats.ezeats.ui.main.points.adapter.PointsAdapter
import com.easyEats.ezeats.ui.main.points.adapter.SpinWheelAdapter
import com.easyEats.ezeats.utils.Resource
import kotlin.math.log

class VoucherBottomSheet : BaseBottomSheetFragment<VoucherBottomSheetBinding>() {

    private val pointsViewModel by activityViewModels<PointsViewModel>()
    private val args: VoucherBottomSheetArgs by navArgs()

    private val pointsAdapter by lazy(LazyThreadSafetyMode.NONE) { PointsAdapter() }

    private val spinWheelPointsAdapter by lazy(LazyThreadSafetyMode.NONE) { SpinWheelAdapter() }


    override fun setUpViews() {
    }

    override fun listenToObservers() {
        Log.d("TAG", "listenToObservers: ")
        pointsViewModel.pointsResultLiveData.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Error -> {
                    Log.d("TAG", "listenToObservers: Error ${resource.message}")
                }
                is Resource.Loading -> {
                    Log.d("TAG", "listenToObservers: loading")
                }
                is Resource.Success -> {
                    when (args.isSpinWheelPoints) {
                        true -> {
                            binding.tvTitle.text = "Spin the wheel codes"
                            binding.rvPoints.adapter = spinWheelPointsAdapter
                            spinWheelPointsAdapter.submitList(
                                resource.data?.result?.data?.rewards?.spinWheelVouchers
                            )
                            Log.d("TAG", "listenToObservers: Spin the wheel codes ${args.isSpinWheelPoints}")
                        }

                        false -> {
                            binding.tvTitle.text = "Points"
                            binding.rvPoints.adapter = pointsAdapter
                            pointsAdapter.submitList(
                                resource.data?.result?.data?.points?.loyaltyPoints
                            )
                            Log.d("TAG", "listenToObservers: ")
                            Log.d("TAG", "listenToObservers: Points ${args.isSpinWheelPoints}")
                        }
                    }
                }

                is Resource.UnAuthenticated -> {}
            }
        }
    }

    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = VoucherBottomSheetBinding.inflate(layoutInflater)

}