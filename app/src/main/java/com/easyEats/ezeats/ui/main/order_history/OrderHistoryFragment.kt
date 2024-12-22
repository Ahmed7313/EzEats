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

    private val orderHistoryViewModel: OrderHistoryViewModel by viewModels()

    private val orderHistoryAdapter = OrderHistoryAdapter(this)
    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentOrderHistoryBinding.inflate(layoutInflater)

    override fun setUpViews() {
        listenToObservers()
        binding.viewModel = orderHistoryViewModel
        binding.ivBack.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.rvOrderHistory.adapter = orderHistoryAdapter
        binding.rvOrderHistory.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (!recyclerView.canScrollVertically(1)) {
                    orderHistoryViewModel.loadMoreOrders()
                }
            }
        })
    }

    private fun listenToObservers() {
        orderHistoryViewModel.orders.observe(viewLifecycleOwner) { orders ->
            orderHistoryAdapter.submitList(orders)
        }

        orderHistoryViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            if (isLoading) showMainLoading() else hideMainLoading()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        hideMainLoading()
    }

    override fun onReorderButtonClicked(restaurantId: String) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse("https://ezeats.app/$restaurantId-1")
        requireActivity().startActivity(intent)
    }
}
