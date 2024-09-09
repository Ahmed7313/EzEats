package com.easyEats.ezeats.ui.main.order_history


import android.view.LayoutInflater
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.easyEats.ezeats.base.BaseFragment
import com.easyEats.ezeats.databinding.FragmentOrderHistoryBinding
import com.easyEats.ezeats.ui.main.order_history.adapter.OrderHistoryAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OrderHistoryFragment : BaseFragment<FragmentOrderHistoryBinding>() {

    private val orderHistoryViewModel: OrderHistoryViewModel by viewModels()

    private val orderHistoryAdapter = OrderHistoryAdapter()
    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentOrderHistoryBinding.inflate(layoutInflater)

    override fun setUpViews() {
        listenToObservers()
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
            orders.isEmpty().let { binding.llEmptyOrderHistory.visibility= VISIBLE }
            orderHistoryAdapter.submitList(orders)
        }
    }


}
