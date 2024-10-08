package com.easyEats.ezeats.ui.main.order_history.adapter


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.easyEats.ezeats.databinding.ItemOrderHistoryBinding
import com.easyEats.ezeats.domain.main.model.response.order_history.Order

class OrderHistoryAdapter(
    private val onReorderButtonClicked: OnReorderButtonClicked
) : ListAdapter<Order, OrderHistoryAdapter.OrderHistoryViewHolder>(OrderHistoryDiffCallback()) {
    private lateinit var subOrderAdapter: SubOrderHistoryAdapter
    private val sharedViewPool = RecyclerView.RecycledViewPool()


    interface OnReorderButtonClicked {
        fun onReorderButtonClicked(restaurantId: String)
    }

    inner class OrderHistoryViewHolder(val binding: ItemOrderHistoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(order: Order) {
            if (order.totalPrice != 0.0)
                binding.order = order
            subOrderAdapter.submitList(order.subOrders)
            binding.btnReOrder.setOnClickListener {
                onReorderButtonClicked.onReorderButtonClicked(order.restaurantId)
            }
            binding.ivCollapse.setOnClickListener {
                if (binding.rvSubOrder.isVisible) {
                    binding.ivCollapse.rotation = 0f
                    binding.rvSubOrder.isVisible = false
                } else {
                    binding.ivCollapse.rotation = 180f
                    binding.rvSubOrder.isVisible = true
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderHistoryViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemOrderHistoryBinding.inflate(inflater, parent, false)
        return OrderHistoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: OrderHistoryViewHolder, position: Int) {
        val order = getItem(position)
        subOrderAdapter = SubOrderHistoryAdapter()
        holder.binding.rvSubOrder.setRecycledViewPool(sharedViewPool)
        holder.binding.rvSubOrder.adapter = subOrderAdapter
        holder.bind(order)
    }

}

class OrderHistoryDiffCallback : DiffUtil.ItemCallback<Order>() {
    override fun areItemsTheSame(oldItem: Order, newItem: Order): Boolean {
        return oldItem.orderId == newItem.orderId // Make sure you compare unique identifiers
    }

    override fun areContentsTheSame(oldItem: Order, newItem: Order): Boolean {
        return oldItem == newItem
    }
}