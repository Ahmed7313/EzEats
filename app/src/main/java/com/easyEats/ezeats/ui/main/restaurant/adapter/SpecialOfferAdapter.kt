package com.easyEats.ezeats.ui.main.restaurant.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.easyEats.ezeats.databinding.ItemRestaurantOfferBinding
import com.easyEats.ezeats.domain.main.model.response.home_response.HotDeal
/**
 * Adapter used to display special offers for a restaurant in a RecyclerView.
 * It binds a list of HotDeal items to the RecyclerView and listens for click events on each item.
 *
 * @param onSpecialOfferClicked A callback interface to handle special offer clicks.
 */
class SpecialOfferAdapter(
    val onSpecialOfferClicked: OnSpecialOfferClicked
) : ListAdapter<HotDeal, SpecialOfferAdapter.SpecialHotDealsViewHolder>(
    SpecialHotDealsAdapterDiffCallback()
) {

    /**
     * Interface for handling clicks on special offer items.
     * The implementing class should define the `onSpecialOfferClickListener` method.
     */
    interface OnSpecialOfferClicked {
        /**
         * Called when a special offer item is clicked.
         *
         * @param offerId The ID of the clicked offer.
         */
        fun onSpecialOfferClickListener(offerId: String)
    }

    /**
     * Creates a new ViewHolder to represent a special offer item.
     *
     * @param parent The parent view group where the item will be placed.
     * @param viewType The type of the view to create.
     * @return The created ViewHolder for the special offer item.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SpecialHotDealsViewHolder {
        val binding =
            ItemRestaurantOfferBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SpecialHotDealsViewHolder(binding)
    }

    /**
     * Binds the special offer data to the ViewHolder.
     *
     * @param holder The ViewHolder to bind the data to.
     * @param position The position of the item in the list.
     */
    override fun onBindViewHolder(holder: SpecialHotDealsViewHolder, position: Int) {
        val hotDealOffer = getItem(position)
        holder.bind(hotDealOffer)
    }

    /**
     * ViewHolder class for binding the special offer data to the view.
     *
     * @param binding The binding object for the special offer item view.
     */
    inner class SpecialHotDealsViewHolder(private val binding: ItemRestaurantOfferBinding) :
        RecyclerView.ViewHolder(binding.root) {

        /**
         * Binds the HotDeal data to the UI elements in the item view.
         *
         * @param hotDeal The special offer data to bind to the view.
         */
        fun bind(hotDeal: HotDeal) {
            binding.offer = hotDeal
            binding.executePendingBindings() // Ensures the bindings are applied immediately
            // Sets up a click listener to notify when a special offer is clicked
            binding.root.setOnClickListener {
                onSpecialOfferClicked.onSpecialOfferClickListener(hotDeal.id.toString())
            }
        }
    }
}

/**
 * DiffUtil callback used to determine if two HotDeal items are the same or have the same contents.
 * It helps to optimize RecyclerView's performance when updating the list.
 */
class SpecialHotDealsAdapterDiffCallback : DiffUtil.ItemCallback<HotDeal>() {

    /**
     * Compares if two items represent the same object (based on their IDs).
     *
     * @param oldItem The old HotDeal item.
     * @param newItem The new HotDeal item.
     * @return True if the items are the same, false otherwise.
     */
    override fun areItemsTheSame(oldItem: HotDeal, newItem: HotDeal): Boolean {
        return oldItem.id == newItem.id
    }

    /**
     * Compares if the contents of two HotDeal items are the same.
     *
     * @param oldItem The old HotDeal item.
     * @param newItem The new HotDeal item.
     * @return True if the contents are the same, false otherwise.
     */
    override fun areContentsTheSame(oldItem: HotDeal, newItem: HotDeal): Boolean {
        return oldItem == newItem
    }
}
