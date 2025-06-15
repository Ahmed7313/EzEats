package com.easyEats.ezeats.ui.main.home.adapter.featured

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.easyEats.ezeats.databinding.FeaturedItemBinding
import com.easyEats.ezeats.domain.main.model.response.home_response.FeaturedOffer

/**
 * Adapter for displaying a list of featured offers in a RecyclerView.
 *
 * @property FeaturedOffer The data model representing a featured offer item.
 */

class FeaturedOfferAdapter(
    val onFeaturedClicked: OnFeaturedClicked
) :
    ListAdapter<FeaturedOffer, FeaturedOfferAdapter.FeaturedOfferViewHolder>(
        FeaturedOfferDiffCallback()
    ) {

    interface OnFeaturedClicked {
        fun onFeatureClicked(offerId: String)
    }

    /**
     * Creates a new ViewHolder for the featured offer item.
     *
     * @param parent The parent view that will contain the new ViewHolder.
     * @param viewType The type of view to create. This is not used in this implementation.
     * @return A new instance of [FeaturedOfferViewHolder].
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeaturedOfferViewHolder {
        val binding =
            FeaturedItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FeaturedOfferViewHolder(binding)
    }

    /**
     * Binds data to the ViewHolder at the specified position.
     *
     * @param holder The ViewHolder to bind data to.
     * @param position The position of the item within the adapter's data set.
     */
    override fun onBindViewHolder(holder: FeaturedOfferViewHolder, position: Int) {
        val featuredOffer = getItem(position)
        holder.bind(featuredOffer)
    }

    /**
     * ViewHolder for featured offer items.
     *
     * @param binding The binding for the featured offer item layout.
     */
    inner class FeaturedOfferViewHolder(private val binding: FeaturedItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        /**
         * Binds the featured offer data to the item view.
         *
         * @param featuredOffer The featured offer data to bind.
         */
        fun bind(featuredOffer: FeaturedOffer) {
            binding.featuredOffer = featuredOffer
            binding.executePendingBindings()
            binding.root.setOnClickListener {
                onFeaturedClicked.onFeatureClicked(offerId = featuredOffer.id)
            }
            binding.btnRedeem.setOnClickListener {
                onFeaturedClicked.onFeatureClicked(offerId = featuredOffer.id)
            }
        }
    }
}

/**
 * DiffUtil callback for calculating the difference between two lists of [FeaturedOffer] items.
 */
class FeaturedOfferDiffCallback : DiffUtil.ItemCallback<FeaturedOffer>() {

    /**
     * Checks if two items represent the same item.
     *
     * @param oldItem The old item.
     * @param newItem The new item.
     * @return True if the items represent the same object.
     */
    override fun areItemsTheSame(oldItem: FeaturedOffer, newItem: FeaturedOffer): Boolean {
        return oldItem.id == newItem.id
    }

    /**
     * Checks if the contents of two items are the same.
     *
     * @param oldItem The old item.
     * @param newItem The new item.
     * @return True if the contents of the items are the same.
     */
    override fun areContentsTheSame(oldItem: FeaturedOffer, newItem: FeaturedOffer): Boolean {
        return oldItem == newItem
    }
}

