package com.easyEats.ezeats.ui.main.home.adapter.hot_deals

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.easyEats.ezeats.databinding.FeaturedItemBinding
import com.easyEats.ezeats.databinding.HotDealsItemBinding
import com.easyEats.ezeats.domain.main.model.response.home_response.FeaturedOffer
import com.easyEats.ezeats.domain.main.model.response.home_response.HotDeal

class HotDealsAdapter :
    ListAdapter<HotDeal, HotDealsAdapter.HotDealsViewHolder>(HotDealsAdapterDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HotDealsViewHolder {
        val binding =
            HotDealsItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HotDealsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HotDealsViewHolder, position: Int) {
        val hotDealOffer = getItem(position)
        holder.bind(hotDealOffer)
        Log.d("TAG", "onBindViewHolder: $hotDealOffer ")
    }

    inner class HotDealsViewHolder(private val binding: HotDealsItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(hotDeal: HotDeal) {
            binding.hotDeal = hotDeal
            binding.executePendingBindings()
        }
    }
}

class HotDealsAdapterDiffCallback : DiffUtil.ItemCallback<HotDeal>() {
    override fun areItemsTheSame(oldItem: HotDeal, newItem: HotDeal): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: HotDeal, newItem: HotDeal): Boolean {
        return oldItem == newItem
    }
}