package com.easyEats.ezeats.ui.main.home.adapter.featured

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.easyEats.ezeats.databinding.FeaturedItemBinding
import com.easyEats.ezeats.domain.main.model.response.home_response.FeaturedOffer

class FeaturedOfferAdapter :
    ListAdapter<FeaturedOffer, FeaturedOfferAdapter.FeaturedOfferViewHolder>(
        FeaturedOfferDiffCallback()
    ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeaturedOfferViewHolder {
        val binding =
            FeaturedItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FeaturedOfferViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FeaturedOfferViewHolder, position: Int) {
        val featuredOffer = getItem(position)
        holder.bind(featuredOffer)
    }

    inner class FeaturedOfferViewHolder(private val binding: FeaturedItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(featuredOffer: FeaturedOffer) {
            binding.featuredOffer = featuredOffer
            binding.executePendingBindings()


        }
    }
}

class FeaturedOfferDiffCallback : DiffUtil.ItemCallback<FeaturedOffer>() {
    override fun areItemsTheSame(oldItem: FeaturedOffer, newItem: FeaturedOffer): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: FeaturedOffer, newItem: FeaturedOffer): Boolean {
        return oldItem == newItem
    }
}
