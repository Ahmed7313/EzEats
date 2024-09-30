package com.easyEats.ezeats.ui.main.order_history.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.easyEats.ezeats.databinding.ItemExtrasParentBinding
import com.easyEats.ezeats.domain.main.model.response.order_history.Choices

class ChoicesAdapter : ListAdapter<Choices, ChoicesAdapter.ChoicesViewHolder>(ChoicesDiffUtil()) {

    inner class ChoicesViewHolder(val binding: ItemExtrasParentBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(choice: Choices) {
            binding.choice = choice
            collapseExtras()
            val extrasAdapter = ExtrasAdapter()
            binding.rvExtras.adapter = extrasAdapter
            extrasAdapter.submitList(choice.choices)
        }

        private fun collapseExtras() {
            binding.ivCollapse.setOnClickListener {
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


    class ChoicesDiffUtil : DiffUtil.ItemCallback<Choices>() {
        override fun areItemsTheSame(oldItem: Choices, newItem: Choices): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: Choices, newItem: Choices): Boolean {
            return oldItem == newItem
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChoicesViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemExtrasParentBinding.inflate(layoutInflater, parent, false)
        return ChoicesViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ChoicesViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}
