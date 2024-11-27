package com.easyEats.ezeats.ui.main.home.adapter.partners

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.easyEats.ezeats.databinding.ItemPartnerBinding
import com.easyEats.ezeats.domain.main.model.response.partners.PartnersItem

class PartnersAdapter :
    ListAdapter<PartnersItem, PartnersAdapter.PartnersViewHolder>(PartnersDiffCallback()) {

    inner class PartnersViewHolder(val binding: ItemPartnerBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(partner: PartnersItem) {
            binding.partner = partner
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PartnersViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemPartnerBinding.inflate(layoutInflater, parent, false)
        return PartnersViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PartnersViewHolder, position: Int) {
        val partner = getItem(position)
        holder.bind(partner)
    }
}

class PartnersDiffCallback : DiffUtil.ItemCallback<PartnersItem>() {

    override fun areItemsTheSame(oldItem: PartnersItem, newItem: PartnersItem): Boolean {
        return oldItem === newItem
    }

    override fun areContentsTheSame(oldItem: PartnersItem, newItem: PartnersItem): Boolean {
        return oldItem == newItem
    }
}



