package com.easyEats.ezeats.ui.main.points


import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
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
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

@AndroidEntryPoint // Annotation for Hilt to inject dependencies into this Fragment
class PointScreenFragment : BaseFragment<FragmentPointScreenBinding>() {

    // ViewModel shared between activities or fragments using the same Activity scope
    private val pointsViewModel: PointsViewModel by activityViewModels()

    // Lazy initialization of PointsAdapter to avoid creating it multiple times
    private val pointsAdapter by lazy(LazyThreadSafetyMode.NONE) {
        PointsAdapter()
    }

    // Lazy initialization for spin wheel promo codes adapter
    private val spinWheelPointsAdapter by lazy(LazyThreadSafetyMode.NONE) {
        SpinWheelAdapter()
    }

    // Lazy initialization for vouchers adapter
    private val vouchersAdapter by lazy(LazyThreadSafetyMode.NONE) {
        VoucherAdapter()
    }

    // Inflate the data binding layout for the fragment
    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentPointScreenBinding.inflate(layoutInflater)

    // Set up views and UI interactions
    override fun setUpViews() {
        // Request data for points from the ViewModel
        pointsViewModel.getPointsRestaurantResult()
        listenToObserver() // Start observing data changes

        // Handle spin wheel promo codes button click
        binding.btnSpinWheelPromoCodes.setOnClickListener {
            lifecycleScope.launch {
                binding.btnSpinWheelPromoCodes.isClickable = false // Disable button to prevent multiple clicks
                findNavController().navigate(PointScreenFragmentDirections.goToVoucherBottomSheet(true))
                delay(1000) // Add delay to prevent rapid navigation
                binding.btnSpinWheelPromoCodes.isClickable = true // Re-enable button
            }
        }

        // Handle spin wheel points text click
        binding.tvSpinWheelPoints.setOnClickListener {
            lifecycleScope.launch {
                binding.tvSpinWheelPoints.isClickable = false
                delay(1000)
                findNavController().navigate(PointScreenFragmentDirections.goToVoucherBottomSheet(true))
                binding.tvSpinWheelPoints.isClickable = true
            }
        }

        // Handle points text click
        binding.tvPoints.setOnClickListener {
            lifecycleScope.launch {
                binding.tvPoints.isClickable = false
                delay(1000)
                findNavController().navigate(PointScreenFragmentDirections.goToVoucherBottomSheet(false))
                binding.tvPoints.isClickable = true
            }
        }

        // Handle all points text click
        binding.tvAllPoints.setOnClickListener {
            lifecycleScope.launch {
                binding.tvAllPoints.isClickable = false
                delay(1000)
                findNavController().navigate(PointScreenFragmentDirections.goToVoucherBottomSheet(false))
                binding.tvAllPoints.isClickable = true
            }
        }
    }

    // Observe LiveData and handle responses from the ViewModel
    private fun listenToObserver() {
        lifecycleScope.launchWhenResumed {
            val token = pointsViewModel.getUserToken().first() // Retrieve the user token
            if (token.isEmpty()) {
                // Navigate to guest screen if token is not available
                findNavController().navigate(R.id.goToUserGuestScreen)
            }
        }

        // Observe points result LiveData for changes
        pointsViewModel.pointsResultLiveData.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Error -> {
                    hideMainLoading() // Hide loading indicator
                    Log.d(TAG, "listenToObserver: ${resource.message}") // Log the error message
                }

                is Resource.Loading -> {
                    showMainLoading() // Show loading indicator
                }

                is Resource.Success -> {
                    hideMainLoading() // Hide loading indicator
                    binding.response = resource.data?.result?.data // Bind the response data to the UI

                    // Set up the points RecyclerView
                    binding.rvPoints.adapter = pointsAdapter
                    pointsAdapter.submitList(
                        resource.data?.result?.data?.points?.loyaltyPoints?.take(3) // Display up to 3 items
                    )

                    // Set up the spin wheel promo codes RecyclerView
                    binding.rvSpinWheelPromoCodes.adapter = spinWheelPointsAdapter
                    spinWheelPointsAdapter.submitList(
                        resource.data?.result?.data?.rewards?.spinWheelVouchers?.take(3) // Display up to 3 items
                    )

                    // Set up the vouchers RecyclerView
                    binding.rvVouchers.adapter = vouchersAdapter
                    vouchersAdapter.submitList(resource.data?.result?.data?.rewards?.promocodes)
                }

                is Resource.UnAuthenticated -> {
                    hideMainLoading() // Hide loading indicator
                    // Handle unauthenticated state (e.g., navigate to login)
                }
            }
        }
    }

    // Clean up loading indicator when the view is destroyed
    override fun onDestroyView() {
        super.onDestroyView()
        hideMainLoading()
    }

    // Hide the loading indicator when the fragment is paused
    override fun onPause() {
        super.onPause()
        hideMainLoading()
    }
}
