package com.easyEats.ezeats.ui.main.order_history.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.easyEats.ezeats.databinding.ItemExtrasParentBinding
import com.easyEats.ezeats.domain.main.model.response.order_history.Choices
/**
 * Adapter class for displaying a list of choices and their corresponding extras.
 * It uses a `ListAdapter` with a `ChoicesDiffUtil` to efficiently update the list.
 */
class ChoicesAdapter : ListAdapter<Choices, ChoicesAdapter.ChoicesViewHolder>(ChoicesDiffUtil()) {

    /**
     * ViewHolder for the individual choice items.
     * It binds the `Choices` object to the corresponding layout.
     */
    inner class ChoicesViewHolder(val binding: ItemExtrasParentBinding) :
        RecyclerView.ViewHolder(binding.root) {

        /**
         * Binds the `Choices` object to the UI elements in the ViewHolder.
         * Also sets up an `ExtrasAdapter` to handle the extras for each choice.
         */
        fun bind(choice: Choices) {
            binding.choice = choice
            collapseExtras()  // Collapsible extras functionality
            val extrasAdapter = ExtrasAdapter()  // Adapter for the extras within each choice
            binding.rvExtras.adapter = extrasAdapter
            extrasAdapter.submitList(choice.choices)  // Submit the list of extras for the current choice
        }

        /**
         * Set up a click listener to toggle visibility of the extras.
         * When clicked, the `rvExtras` (RecyclerView) is either collapsed or expanded.
         */
        private fun collapseExtras() {
            binding.ivCollapse.setOnClickListener {
                // Toggle the visibility and rotation of the collapse icon
                if (binding.rvExtras.isVisible) {
                    binding.ivCollapse.rotation = 0f
                    binding.rvExtras.isVisible = false
                } else {
                    binding.ivCollapse.rotation = 180f
                    binding.rvExtras.isVisible = true
                }
            }
        }
    }

    /**
     * DiffUtil implementation to efficiently manage updates to the list.
     * It compares the items based on their reference and content equality.
     */
    class ChoicesDiffUtil : DiffUtil.ItemCallback<Choices>() {

        /**
         * Determines if two items represent the same object.
         * It compares by reference.
         */
        override fun areItemsTheSame(oldItem: Choices, newItem: Choices): Boolean {
            return oldItem === newItem
        }

        /**
         * Determines if the contents of two items are the same.
         * It compares based on content equality.
         */
        override fun areContentsTheSame(oldItem: Choices, newItem: Choices): Boolean {
            return oldItem == newItem
        }
    }

    /**
     * Creates a new ViewHolder for each item in the list.
     * Inflates the `ItemExtrasParentBinding` layout.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChoicesViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemExtrasParentBinding.inflate(layoutInflater, parent, false)
        return ChoicesViewHolder(binding)
    }

    /**
     * Binds the data from the `Choices` list to the corresponding ViewHolder.
     */
    override fun onBindViewHolder(holder: ChoicesViewHolder, position: Int) {
        holder.bind(getItem(position))  // Bind the current item at the specified position
    }
}

