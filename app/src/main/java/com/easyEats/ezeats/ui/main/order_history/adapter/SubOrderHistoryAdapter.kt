package com.easyEats.ezeats.ui.main.order_history.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.easyEats.ezeats.databinding.ItemSubOrderHistoryBinding
import com.easyEats.ezeats.domain.main.model.response.order_history.SubOrder

class SubOrderHistoryAdapter : ListAdapter<SubOrder, SubOrderHistoryAdapter.SubOrderHistoryViewHolder>(SubOrderHistoryDiffCallback()) {

    inner class SubOrderHistoryViewHolder(val binding: ItemSubOrderHistoryBinding) : RecyclerView.ViewHolder(binding.root) { fun bind(subOrder: SubOrder) { binding.subOrder = subOrder } }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubOrderHistoryViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemSubOrderHistoryBinding.inflate(inflater, parent, false)
        return SubOrderHistoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SubOrderHistoryViewHolder, position: Int) {
        val subOrder = getItem(position)
        holder.bind(subOrder)
    }
}

class SubOrderHistoryDiffCallback : DiffUtil.ItemCallback<SubOrder>() {
    override fun areItemsTheSame(oldItem: SubOrder, newItem: SubOrder): Boolean {
        return oldItem === newItem
    }

    override fun areContentsTheSame(oldItem: SubOrder, newItem: SubOrder): Boolean {
        return oldItem == newItem
    }
}