package com.easyEats.ezeats.ui.main.order_history.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.easyEats.ezeats.databinding.ItemExtraBinding
import com.easyEats.ezeats.databinding.ItemExtrasParentBinding
import com.easyEats.ezeats.domain.main.model.response.order_history.Choices
import com.easyEats.ezeats.domain.main.model.response.order_history.Extras
/**
 * Adapter class for displaying a list of extras in a RecyclerView.
 * It uses `ListAdapter` with `ExtrasDiffUtil` to efficiently handle list updates.
 */
class ExtrasAdapter : ListAdapter<Extras, ExtrasAdapter.ExtrasViewHolder>(ExtrasDiffUtil()) {

    /**
     * ViewHolder for individual extras items.
     * Binds an `Extras` object to the corresponding UI elements in the item view.
     */
    inner class ExtrasViewHolder(val binding: ItemExtraBinding) :
        RecyclerView.ViewHolder(binding.root) {

        /**
         * Binds the `Extras` object to the UI elements in the ViewHolder.
         * Uses `executePendingBindings()` to ensure data binding is updated immediately.
         */
        fun bind(extras: Extras) {
            binding.extra = extras  // Bind the `Extras` data to the layout
            binding.executePendingBindings()  // Ensure any pending binding updates are executed
        }
    }

    /**
     * DiffUtil implementation for efficiently comparing the contents and references of `Extras` items.
     * Used to update the list in the adapter with minimal performance overhead.
     */
    class ExtrasDiffUtil : DiffUtil.ItemCallback<Extras>() {

        /**
         * Determines if two items represent the same object.
         * Compares by reference.
         */
        override fun areItemsTheSame(oldItem: Extras, newItem: Extras): Boolean {
            return oldItem === newItem
        }

        /**
         * Determines if the contents of two items are the same.
         * Compares by content equality.
         */
        override fun areContentsTheSame(oldItem: Extras, newItem: Extras): Boolean {
            return oldItem == newItem
        }
    }

    /**
     * Creates a new ViewHolder for the items in the list.
     * Inflates the `ItemExtraBinding` layout.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExtrasViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemExtraBinding.inflate(layoutInflater, parent, false)
        return ExtrasViewHolder(binding)
    }

    /**
     * Binds the `Extras` item at the specified position to the corresponding ViewHolder.
     */
    override fun onBindViewHolder(holder: ExtrasViewHolder, position: Int) {
        holder.bind(getItem(position))  // Bind the current item at the specified position
    }
}

