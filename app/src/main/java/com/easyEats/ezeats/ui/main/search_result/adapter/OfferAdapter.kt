package com.easyEats.ezeats.ui.main.search_result.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.easyEats.ezeats.databinding.ItemExtraOfferBinding
import com.easyEats.ezeats.domain.main.model.response.restaurant_reponse.Offer
import javax.inject.Inject

class OfferAdapter @Inject constructor() :
    ListAdapter<Offer, OfferAdapter.OfferViewHolder>(OfferDiffUtil()) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OfferViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = ItemExtraOfferBinding.inflate(
            inflater, parent, false
        )

        return OfferViewHolder(view)
    }

    override fun onBindViewHolder(holder: OfferViewHolder, position: Int) {
        holder.binding.offer = getItem(position)
    }

    inner class OfferViewHolder(val binding: ItemExtraOfferBinding) :
        RecyclerView.ViewHolder(binding.root)

    class OfferDiffUtil : DiffUtil.ItemCallback<Offer>() {
        override fun areItemsTheSame(oldItem: Offer, newItem: Offer): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Offer, newItem: Offer): Boolean {
            return oldItem == newItem
        }

    }
}