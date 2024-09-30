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

class ExtrasAdapter : ListAdapter<Extras, ExtrasAdapter.ExtrasViewHolder>(ExtrasDiffUtil()) {

    inner class ExtrasViewHolder(val binding: ItemExtraBinding) :
        RecyclerView.ViewHolder(binding.root){
        fun bind(extras: Extras){
            binding.extra=extras
            binding.executePendingBindings()
        }

    }


    class ExtrasDiffUtil : DiffUtil.ItemCallback<Extras>() {
        override fun areItemsTheSame(oldItem: Extras, newItem: Extras): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: Extras, newItem: Extras): Boolean {
            return oldItem == newItem
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExtrasViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemExtraBinding.inflate(layoutInflater, parent, false)
        return ExtrasViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ExtrasViewHolder, position: Int) {
        holder.bind(getItem(position))
    }


}
