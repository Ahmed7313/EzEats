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


/**
 * Adapter class for displaying a list of Restaurant items in a RecyclerView.
 * This adapter uses ListAdapter to handle item differences efficiently using DiffUtil.
 * It also manages a nested RecyclerView for displaying offers within each Restaurant item.
 */
class RestaurantAdapter @Inject constructor(
    val onRestaurantClicked: OnRestaurantClicked, val onOfferClicked: OfferAdapter.OnOfferClicked
) :
    ListAdapter<Restaurant, RestaurantAdapter.RestaurantViewHolder>(RestaurantDiffUtil()) {

    private val sharedViewPool = RecyclerView.RecycledViewPool()

    interface OnRestaurantClicked {
        fun onRestaurantClickListener(restaurant: Restaurant)
    }

    // Adapter for handling offers within a restaurant
    private lateinit var offerAdapter: OfferAdapter

    // Context to access resources and application-specific assets
    lateinit var context: Context

    /**
     * DiffUtil class for calculating the difference between two lists of Restaurant items.
     * Helps ListAdapter to determine the changes in the list and update the RecyclerView efficiently.
     */
    class RestaurantDiffUtil : DiffUtil.ItemCallback<Restaurant>() {

        /**
         * Checks whether two items represent the same Restaurant.
         *
         * @param oldItem The old item in the list.
         * @param newItem The new item in the list.
         * @return True if the items are the same, false otherwise.
         */
        override fun areItemsTheSame(oldItem: Restaurant, newItem: Restaurant) = oldItem == newItem

        /**
         * Checks whether the contents of two Restaurant items are the same.
         *
         * @param oldItem The old item in the list.
         * @param newItem The new item in the list.
         * @return True if the contents are the same, false otherwise.
         */
        override fun areContentsTheSame(oldItem: Restaurant, newItem: Restaurant) =
            oldItem == newItem
    }

    /**
     * Creates and returns a new RestaurantViewHolder.
     * Inflates the layout for each item in the RecyclerView.
     *
     * @param parent The parent ViewGroup that the ViewHolder will be attached to.
     * @param viewType The type of view. This is used when there are multiple view types.
     * @return An instance of RestaurantViewHolder.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RestaurantViewHolder {
        context = parent.context
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = ItemOfferBinding.inflate(layoutInflater, parent, false)
        return RestaurantViewHolder(view)
    }

    /**
     * Binds the Restaurant data to the views in the ViewHolder.
     * Also handles the nested RecyclerView for displaying offers within the restaurant.
     *
     * @param holder The ViewHolder which should be updated to represent the contents of the item at the given position.
     * @param position The position of the item within the adapter's data set.
     */
    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: RestaurantViewHolder, position: Int) {
        val restaurant = getItem(position)
        holder.binding.restaurant = restaurant
        holder.binding.rvExtraOffers.setRecycledViewPool(sharedViewPool)
        offerAdapter = OfferAdapter(onOfferClicked)

        handleEmptyData(restaurant, holder)
        holder.binding.clRestaurantDetails.setOnClickListener {
            onRestaurantClicked.onRestaurantClickListener(restaurant)
        }

    }


    private fun handleEmptyData(
        restaurant: Restaurant,
        holder: RestaurantViewHolder
    ) {
        if (restaurant.offers?.isEmpty() == true) {
            holder.binding.tvRestaurantHasNoOffers.text =
                restaurant.name + " " + context.getString(R.string.has_no_offers_right_now)
//            holder.binding.llEmptyOffers.visibility = View.VISIBLE
            holder.binding.rvExtraOffers.visibility = View.GONE
        } else {
//            holder.binding.llEmptyOffers.visibility = View.GONE
            holder.binding.rvExtraOffers.visibility = View.VISIBLE
            holder.binding.rvExtraOffers.adapter = offerAdapter
            offerAdapter.submitList(restaurant.offers)
        }
    }

    /**
     * ViewHolder class for holding the binding reference for each Restaurant item.
     *
     * @property binding The binding object generated for the item layout.
     */
    inner class RestaurantViewHolder(val binding: ItemOfferBinding) :
        RecyclerView.ViewHolder(binding.root)
}
