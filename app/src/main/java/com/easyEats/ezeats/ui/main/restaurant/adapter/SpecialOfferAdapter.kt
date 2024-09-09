package com.easyEats.ezeats.ui.main.restaurant.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.easyEats.ezeats.databinding.ItemRestaurantOfferBinding
import com.easyEats.ezeats.domain.main.model.response.home_response.HotDeal


class SpecialOfferAdapter(
    val onSpecialOfferClicked: OnSpecialOfferClicked
) :
    ListAdapter<HotDeal, SpecialOfferAdapter.SpecialHotDealsViewHolder>(
        SpecialHotDealsAdapterDiffCallback()
    ) {

    interface OnSpecialOfferClicked {
        fun onSpecialOfferClickListener(offerId: String)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SpecialHotDealsViewHolder {
        val binding =
            ItemRestaurantOfferBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SpecialHotDealsViewHolder(binding)
    }


    override fun onBindViewHolder(holder: SpecialHotDealsViewHolder, position: Int) {
        val hotDealOffer = getItem(position)
        holder.bind(hotDealOffer)
    }


    inner class SpecialHotDealsViewHolder(private val binding: ItemRestaurantOfferBinding) :
        RecyclerView.ViewHolder(binding.root) {


        fun bind(hotDeal: HotDeal) {
            binding.offer = hotDeal
            binding.executePendingBindings()
            binding.root.setOnClickListener {
                onSpecialOfferClicked.onSpecialOfferClickListener(hotDeal.id.toString())
            }
        }
    }
}


class SpecialHotDealsAdapterDiffCallback : DiffUtil.ItemCallback<HotDeal>() {


    override fun areItemsTheSame(oldItem: HotDeal, newItem: HotDeal): Boolean {
        return oldItem.id == newItem.id
    }


    override fun areContentsTheSame(oldItem: HotDeal, newItem: HotDeal): Boolean {
        return oldItem == newItem
    }
}