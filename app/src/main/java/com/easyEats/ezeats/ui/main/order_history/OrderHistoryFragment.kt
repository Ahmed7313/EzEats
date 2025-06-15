package com.easyEats.ezeats.ui.main.order_history

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.easyEats.ezeats.base.BaseFragment
import com.easyEats.ezeats.databinding.FragmentOrderHistoryBinding
import com.easyEats.ezeats.ui.main.order_history.adapter.OrderHistoryAdapter
import dagger.hilt.android.AndroidEntryPoint
@AndroidEntryPoint
class OrderHistoryFragment : BaseFragment<FragmentOrderHistoryBinding>(),
    OrderHistoryAdapter.OnReorderButtonClicked {

    // Injecting the OrderHistoryViewModel using Hilt for dependency injection.
    private val orderHistoryViewModel: OrderHistoryViewModel by viewModels()

    // Creating an instance of the OrderHistoryAdapter to bind to the RecyclerView.
    private val orderHistoryAdapter = OrderHistoryAdapter(this)

    // Inflate the fragment's binding and return it for use in the fragment.
    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentOrderHistoryBinding.inflate(layoutInflater)

    // Set up views and their click listeners.
    override fun setUpViews() {
        // Observe the LiveData in the ViewModel to update UI elements accordingly.
        listenToObservers()

        // Bind the ViewModel to the layout via data binding.
        binding.viewModel = orderHistoryViewModel

        // Set up the back button to pop the fragment from the navigation stack.
        binding.ivBack.setOnClickListener {
            findNavController().popBackStack()
        }

        // Set the adapter for the RecyclerView to display the order history.
        binding.rvOrderHistory.adapter = orderHistoryAdapter

        // Add an infinite scroll listener to load more orders when reaching the bottom.
        binding.rvOrderHistory.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (!recyclerView.canScrollVertically(1)) {
                    // Load more orders when the bottom is reached.
                    orderHistoryViewModel.loadMoreOrders()
                }
            }
        })
    }

    // Observe changes in the ViewModel's LiveData and update the UI accordingly.
    override fun listenToObservers() {
        // Observe orders LiveData and submit the list to the adapter.
        orderHistoryViewModel.orders.observe(viewLifecycleOwner) { orders ->
            orderHistoryAdapter.submitList(orders)
        }

        // Observe the loading state and show/hide loading indicator.
        orderHistoryViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            if (isLoading) showMainLoading() else hideMainLoading()
        }
    }

    // Ensure to hide the loading indicator when the view is destroyed.
    override fun onDestroyView() {
        super.onDestroyView()
        hideMainLoading()
    }

    // Handle the Reorder button click by opening the restaurant page URL.
    override fun onReorderButtonClicked(restaurantId: String) {
        // Create an intent to open the restaurant's page in the browser.
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse("https://ezeats.app/$restaurantId-1")
        requireActivity().startActivity(intent)
    }
}

