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

/**
 * Adapter for displaying a list of hot deals in a RecyclerView.
 *
 * @property HotDeal The data model representing a hot deal item.
 */
class HotDealsAdapter (
    val onHotDealClicked: OnHotDealClicked
):
    ListAdapter<HotDeal, HotDealsAdapter.HotDealsViewHolder>(HotDealsAdapterDiffCallback()) {

    /**
     * Creates a new ViewHolder for the hot deal item.
     *
     * @param parent The parent view that will contain the new ViewHolder.
     * @param viewType The type of view to create. This is not used in this implementation.
     * @return A new instance of [HotDealsViewHolder].
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HotDealsViewHolder {
        val binding =
            HotDealsItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HotDealsViewHolder(binding)
    }

    interface OnHotDealClicked {
        fun onHotDealClickListener(offer: HotDeal)
    }

    /**
     * Binds data to the ViewHolder at the specified position.
     *
     * @param holder The ViewHolder to bind data to.
     * @param position The position of the item within the adapter's data set.
     */
    override fun onBindViewHolder(holder: HotDealsViewHolder, position: Int) {
        val hotDealOffer = getItem(position)
        holder.bind(hotDealOffer)
    }

    /**
     * ViewHolder for hot deal items.
     *
     * @param binding The binding for the hot deal item layout.
     */
    inner class HotDealsViewHolder(private val binding: HotDealsItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        /**
         * Binds the hot deal data to the item view.
         *
         * @param hotDeal The hot deal data to bind.
         */
        fun bind(hotDeal: HotDeal) {
            binding.hotDeal = hotDeal
            binding.executePendingBindings()
            binding.root.setOnClickListener {
                onHotDealClicked.onHotDealClickListener(hotDeal)
            }
        }
    }
}

/**
 * DiffUtil callback for calculating the difference between two lists of [HotDeal] items.
 */
class HotDealsAdapterDiffCallback : DiffUtil.ItemCallback<HotDeal>() {

    /**
     * Checks if two items represent the same item.
     *
     * @param oldItem The old item.
     * @param newItem The new item.
     * @return True if the items represent the same object.
     */
    override fun areItemsTheSame(oldItem: HotDeal, newItem: HotDeal): Boolean {
        return oldItem.id == newItem.id
    }

    /**
     * Checks if the contents of two items are the same.
     *
     * @param oldItem The old item.
     * @param newItem The new item.
     * @return True if the contents of the items are the same.
     */
    override fun areContentsTheSame(oldItem: HotDeal, newItem: HotDeal): Boolean {
        return oldItem == newItem
    }
}
