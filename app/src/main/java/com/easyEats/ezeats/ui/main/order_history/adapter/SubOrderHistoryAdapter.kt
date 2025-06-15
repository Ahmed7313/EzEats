package com.easyEats.ezeats.ui.main.order_history.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.easyEats.ezeats.databinding.ItemSubOrderHistoryBinding
import com.easyEats.ezeats.domain.main.model.response.order_history.Choices
import com.easyEats.ezeats.domain.main.model.response.order_history.SubOrder


/**
 * Adapter class for displaying a list of sub-orders in the Order History.
 * It uses `ListAdapter` with `SubOrderHistoryDiffCallback` to efficiently handle list updates.
 */
class SubOrderHistoryAdapter :
    ListAdapter<SubOrder, SubOrderHistoryAdapter.SubOrderHistoryViewHolder>(SubOrderHistoryDiffCallback()) {

    /**
     * ViewHolder for individual sub-orders in the Order History list.
     * Binds a `SubOrder` object to the corresponding UI elements in the item view.
     */
    inner class SubOrderHistoryViewHolder(val binding: ItemSubOrderHistoryBinding) :
        RecyclerView.ViewHolder(binding.root) {

        /**
         * Binds the `SubOrder` object to the UI elements in the ViewHolder.
         * Sets the sub-order details and handles the extras (choices) list.
         */
        fun bind(subOrder: SubOrder) {
            binding.subOrder = subOrder
            val choicesAdapter = ChoicesAdapter()  // Adapter for handling choices within a sub-order
            binding.rvExtras.adapter = choicesAdapter  // Set the adapter for extras RecyclerView
            choicesAdapter.submitList(subOrder.extras)  // Submit the list of extras (choices) to the adapter
        }
    }

    /**
     * Creates a new ViewHolder for the items in the list.
     * Inflates the `ItemSubOrderHistoryBinding` layout.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubOrderHistoryViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemSubOrderHistoryBinding.inflate(inflater, parent, false)
        return SubOrderHistoryViewHolder(binding)
    }

    /**
     * Binds the `SubOrder` item at the specified position to the corresponding ViewHolder.
     */
    override fun onBindViewHolder(holder: SubOrderHistoryViewHolder, position: Int) {
        val subOrder = getItem(position)
        holder.bind(subOrder)  // Bind the current sub-order to the ViewHolder
    }
}

/**
 * DiffUtil callback for efficiently comparing the `SubOrder` objects.
 * Used to update the list in the adapter with minimal performance overhead.
 */
class SubOrderHistoryDiffCallback : DiffUtil.ItemCallback<SubOrder>() {

    /**
     * Determines if two items represent the same object.
     * Compares reference equality for the `SubOrder` objects.
     */
    override fun areItemsTheSame(oldItem: SubOrder, newItem: SubOrder): Boolean {
        return oldItem === newItem
    }

    /**
     * Determines if the contents of two items are the same.
     * Compares all properties of the `SubOrder` objects for equality.
     */
    override fun areContentsTheSame(oldItem: SubOrder, newItem: SubOrder): Boolean {
        return oldItem == newItem
    }
}
