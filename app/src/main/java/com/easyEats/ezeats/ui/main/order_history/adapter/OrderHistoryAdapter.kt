package com.easyEats.ezeats.ui.main.order_history.adapter


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.easyEats.ezeats.databinding.ItemOrderHistoryBinding
import com.easyEats.ezeats.domain.main.model.response.order_history.Order
/**
 * Adapter class for displaying a list of orders in the Order History.
 * It uses `ListAdapter` with `OrderHistoryDiffCallback` to efficiently handle list updates.
 */
class OrderHistoryAdapter(
    private val onReorderButtonClicked: OnReorderButtonClicked  // Interface for handling reorder button clicks
) : ListAdapter<Order, OrderHistoryAdapter.OrderHistoryViewHolder>(OrderHistoryDiffCallback()) {

    private lateinit var subOrderAdapter: SubOrderHistoryAdapter  // Adapter for sub-orders
    private val sharedViewPool = RecyclerView.RecycledViewPool()  // Shared ViewPool for performance

    /**
     * Interface to be implemented by the parent activity or fragment to handle reorder button clicks.
     */
    interface OnReorderButtonClicked {
        fun onReorderButtonClicked(restaurantId: String)
    }

    /**
     * ViewHolder for individual orders in the Order History list.
     * Binds an `Order` object to the corresponding UI elements in the item view.
     */
    inner class OrderHistoryViewHolder(val binding: ItemOrderHistoryBinding) :
        RecyclerView.ViewHolder(binding.root) {

        /**
         * Binds the `Order` object to the UI elements in the ViewHolder.
         * Sets the order details, handles reorder button clicks, and collapses/expands sub-orders.
         */
        fun bind(order: Order) {
            if (order.totalPrice != 0.0)  // Only bind if the order has a valid total price
                binding.order = order
            subOrderAdapter.submitList(order.subOrders)  // Submit the list of sub-orders
            binding.btnReOrder.setOnClickListener {
                onReorderButtonClicked.onReorderButtonClicked(order.restaurantId)  // Trigger reorder action
            }
            binding.ivCollapse.setOnClickListener {
                if (binding.rvSubOrder.isVisible) {
                    binding.ivCollapse.rotation = 0f  // Rotate collapse icon to 0 degrees
                    binding.rvSubOrder.isVisible = false  // Hide sub-order list
                } else {
                    binding.ivCollapse.rotation = 180f  // Rotate collapse icon to 180 degrees
                    binding.rvSubOrder.isVisible = true  // Show sub-order list
                }
            }
        }
    }

    /**
     * Creates a new ViewHolder for the items in the list.
     * Inflates the `ItemOrderHistoryBinding` layout.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderHistoryViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemOrderHistoryBinding.inflate(inflater, parent, false)
        return OrderHistoryViewHolder(binding)
    }

    /**
     * Binds the `Order` item at the specified position to the corresponding ViewHolder.
     * Initializes the sub-order adapter and sets it to the sub-order RecyclerView.
     */
    override fun onBindViewHolder(holder: OrderHistoryViewHolder, position: Int) {
        val order = getItem(position)
        subOrderAdapter = SubOrderHistoryAdapter()  // Create a new sub-order adapter for each order
        holder.binding.rvSubOrder.setRecycledViewPool(sharedViewPool)  // Set the shared ViewPool for sub-orders
        holder.binding.rvSubOrder.adapter = subOrderAdapter  // Set the sub-order adapter
        holder.bind(order)  // Bind the current order to the ViewHolder
    }
}

/**
 * DiffUtil callback for efficiently comparing the `Order` objects.
 * Used to update the list in the adapter with minimal performance overhead.
 */
class OrderHistoryDiffCallback : DiffUtil.ItemCallback<Order>() {

    /**
     * Determines if two items represent the same object.
     * Compares `orderId`, assuming it is unique for each order.
     */
    override fun areItemsTheSame(oldItem: Order, newItem: Order): Boolean {
        return oldItem.orderId == newItem.orderId
    }

    /**
     * Determines if the contents of two items are the same.
     * Compares all properties of the `Order` objects for equality.
     */
    override fun areContentsTheSame(oldItem: Order, newItem: Order): Boolean {
        return oldItem == newItem
    }
}
