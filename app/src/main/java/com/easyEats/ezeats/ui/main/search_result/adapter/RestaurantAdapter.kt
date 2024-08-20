package com.easyEats.ezeats.ui.main.search_result.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.easyEats.ezeats.R
import com.easyEats.ezeats.databinding.ItemOfferBinding
import com.easyEats.ezeats.domain.main.model.response.restaurant_reponse.Restaurant
import javax.inject.Inject

class RestaurantAdapter @Inject constructor() :
    ListAdapter<Restaurant, RestaurantAdapter.RestaurantViewHolder>(RestaurantDiffUtil()) {
    private lateinit var offerAdapter: OfferAdapter

    lateinit var context: Context

    class RestaurantDiffUtil : DiffUtil.ItemCallback<Restaurant>() {
        override fun areItemsTheSame(oldItem: Restaurant, newItem: Restaurant) = oldItem == newItem


        override fun areContentsTheSame(oldItem: Restaurant, newItem: Restaurant) =
            oldItem == newItem

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RestaurantViewHolder {
        context = parent.context
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = ItemOfferBinding.inflate(layoutInflater, parent, false)
        return RestaurantViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: RestaurantViewHolder, position: Int) {
        val restaurant = getItem(position)
        holder.binding.restaurant = restaurant
        offerAdapter = OfferAdapter()
        if (restaurant.offers?.isEmpty() == true) {
            holder.binding.tvRestaurantHasNoOffers.text =
                restaurant.name + " " + context.getString(R.string.has_no_offers_right_now)
            holder.binding.llEmptyOffers.visibility = View.VISIBLE
            holder.binding.rvExtraOffers.visibility = View.GONE
        } else {
            holder.binding.llEmptyOffers.visibility = View.GONE
            holder.binding.rvExtraOffers.visibility = View.VISIBLE
            holder.binding.rvExtraOffers.adapter = offerAdapter
            offerAdapter.submitList(restaurant.offers)
        }


    }

    inner class RestaurantViewHolder(val binding: ItemOfferBinding) :
        RecyclerView.ViewHolder(binding.root)

}