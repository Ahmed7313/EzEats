package com.easyEats.ezeats.ui.main.search_result.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.easyEats.ezeats.databinding.ItemExtraOfferBinding
import com.easyEats.ezeats.domain.main.model.response.restaurant_reponse.Offer
import javax.inject.Inject
/**
 * Adapter class for displaying a list of Offer items in a RecyclerView.
 * This adapter uses ListAdapter to handle item differences efficiently using DiffUtil.
 * It binds Offer data to the corresponding views using data binding.
 */
class OfferAdapter @Inject constructor() :
    ListAdapter<Offer, OfferAdapter.OfferViewHolder>(OfferDiffUtil()) {

    /**
     * Creates and returns a new OfferViewHolder.
     * Inflates the layout for each item in the RecyclerView.
     *
     * @param parent The parent ViewGroup that the ViewHolder will be attached to.
     * @param viewType The type of view. This is used when there are multiple view types.
     * @return An instance of OfferViewHolder.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OfferViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = ItemExtraOfferBinding.inflate(
            inflater, parent, false
        )
        return OfferViewHolder(view)
    }

    /**
     * Binds the Offer data to the views in the ViewHolder.
     *
     * @param holder The ViewHolder which should be updated to represent the contents of the item at the given position.
     * @param position The position of the item within the adapter's data set.
     */
    override fun onBindViewHolder(holder: OfferViewHolder, position: Int) {
        holder.binding.offer = getItem(position)
    }

    /**
     * ViewHolder class for holding the binding reference for each item.
     *
     * @property binding The binding object generated for the item layout.
     */
    inner class OfferViewHolder(val binding: ItemExtraOfferBinding) :
        RecyclerView.ViewHolder(binding.root)

    /**
     * DiffUtil class for calculating the difference between two lists of Offer items.
     * Helps ListAdapter to determine the changes in the list and update the RecyclerView efficiently.
     */
    class OfferDiffUtil : DiffUtil.ItemCallback<Offer>() {

        /**
         * Checks whether two items represent the same Offer.
         *
         * @param oldItem The old item in the list.
         * @param newItem The new item in the list.
         * @return True if the items are the same, false otherwise.
         */
        override fun areItemsTheSame(oldItem: Offer, newItem: Offer): Boolean {
            return oldItem == newItem
        }

        /**
         * Checks whether the contents of two Offer items are the same.
         *
         * @param oldItem The old item in the list.
         * @param newItem The new item in the list.
         * @return True if the contents are the same, false otherwise.
         */
        override fun areContentsTheSame(oldItem: Offer, newItem: Offer): Boolean {
            return oldItem == newItem
        }
    }
}
