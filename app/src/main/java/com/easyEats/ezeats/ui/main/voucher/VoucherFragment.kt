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

/**
 * VoucherFragment handles the display and interaction of vouchers in the app.
 * It observes data from the [VoucherViewModel] and updates the UI based on the received resource states.
 */
@AndroidEntryPoint
class VoucherFragment : BaseFragment<FragmentVoucherBinding>() {

    // ViewModel instance for interacting with voucher data
    private val viewModel: VoucherViewModel by viewModels()

    // Adapter to display voucher data in a RecyclerView
    private val adapter = VoucherAdapter()

    /**
     * Inflates the view binding for the fragment.
     *
     * @param inflater The LayoutInflater object used to inflate the view.
     * @param container The parent container into which the view will be attached.
     * @return The binding object for the fragment's layout.
     */
    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentVoucherBinding.inflate(layoutInflater)

    /**
     * Sets up views and interactions in the fragment.
     * Includes setting a click listener on the back button and observing the ViewModel.
     */
    override fun setUpViews() {
        // Set up the back button click listener to pop the fragment from the back stack
        binding.ivBack.setOnClickListener {
            findNavController().popBackStack()
        }

        // Start observing the ViewModel's LiveData for voucher results
        listenToObserver()
    }

    /**
     * Listens for changes to the [voucherResultLiveData] and updates the UI accordingly.
     * Observes various resource states (Error, Loading, Success, UnAuthenticated).
     */
    private fun listenToObserver() {
        viewModel.voucherResultLiveData.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                // Handle error state
                is Resource.Error -> {
                    hideMainLoading() // Hide loading indicator
                    Log.d(TAG, "listenToObserver: ${resource.message}") // Log the error message
                }

                // Handle loading state
                is Resource.Loading -> {
                    showMainLoading() // Show loading indicator
                }

                // Handle success state
                is Resource.Success -> {
                    hideMainLoading() // Hide loading indicator
                    binding.rvVouchers.adapter = adapter // Set the adapter for the RecyclerView
                    adapter.submitList(resource.data?.result?.data) // Submit the data to the adapter
                    // Show or hide empty state based on the presence of vouchers
                    if (resource.data?.result?.data?.size != 0)
                        binding.llEmptyOrderHistory.visibility = GONE
                    else
                        binding.llEmptyOrderHistory.visibility = VISIBLE
                }

                // Handle unauthenticated state
                is Resource.UnAuthenticated -> {
                    hideMainLoading() // Hide loading indicator
                }
                else ->{}

            }
        }
    }
}
