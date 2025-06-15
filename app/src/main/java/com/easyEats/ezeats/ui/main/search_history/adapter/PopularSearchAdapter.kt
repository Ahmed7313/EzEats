package com.easyEats.ezeats.ui.main.search_history.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.easyEats.ezeats.databinding.ItemPopularChipSearchBinding
import com.easyEats.ezeats.domain.main.model.response.search_response.PopularSearch
/**
 * Adapter used to display a list of PopularSearch items in a RecyclerView.
 * It binds each PopularSearch item to the corresponding item view and handles click events.
 *
 * @param clickListener A callback interface for handling clicks on PopularSearch items.
 */
class PopularSearchAdapter(
    private val clickListener: OnPopularSearchClickListener
) : ListAdapter<PopularSearch, PopularSearchAdapter.PopularSearchViewHolder>(
    PopularSearchDiffCallback()
) {

    /**
     * Interface to handle click events for PopularSearch items.
     * The implementing class should define the `onPopularSearchClicked` method.
     */
    interface OnPopularSearchClickListener {
        /**
         * This method is called when a PopularSearch item is clicked.
         *
         * @param popularSearch The clicked PopularSearch item.
         */
        fun onPopularSearchClicked(popularSearch: PopularSearch)
    }

    /**
     * ViewHolder class that binds the PopularSearch data to the view.
     *
     * @param binding The binding object that connects the PopularSearch item to the view.
     */
    inner class PopularSearchViewHolder(val binding: ItemPopularChipSearchBinding) :
        RecyclerView.ViewHolder(binding.root)

    /**
     * Creates a new ViewHolder to represent a PopularSearch item in the RecyclerView.
     *
     * @param parent The parent view where the item will be placed.
     * @param viewType The type of the view to be created (not used here).
     * @return A new instance of PopularSearchViewHolder.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PopularSearchViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemPopularChipSearchBinding.inflate(inflater, parent, false)
        return PopularSearchViewHolder(binding)
    }

    /**
     * Binds the PopularSearch data to the ViewHolder.
     * This method also sets up the click listener for PopularSearch items.
     *
     * @param holder The ViewHolder to bind the data to.
     * @param position The position of the item in the list.
     */
    override fun onBindViewHolder(holder: PopularSearchViewHolder, position: Int) {
        val popularSearch = getItem(position)
        holder.binding.popularSearch = popularSearch
        holder.binding.clickListener = clickListener
    }
}

/**
 * DiffUtil callback used to optimize RecyclerView updates when the list of PopularSearch changes.
 * It checks whether two PopularSearch items are the same and if their contents are the same.
 */
class PopularSearchDiffCallback : DiffUtil.ItemCallback<PopularSearch>() {

    /**
     * Determines whether two PopularSearch items represent the same object.
     *
     * @param oldItem The old PopularSearch item.
     * @param newItem The new PopularSearch item.
     * @return True if the two items are the same, false otherwise.
     */
    override fun areItemsTheSame(oldItem: PopularSearch, newItem: PopularSearch): Boolean {
        return oldItem == newItem
    }

    /**
     * Determines whether the contents of two PopularSearch items are the same.
     *
     * @param oldItem The old PopularSearch item.
     * @param newItem The new PopularSearch item.
     * @return True if the contents are the same, false otherwise.
     */
    override fun areContentsTheSame(oldItem: PopularSearch, newItem: PopularSearch): Boolean {
        return oldItem == newItem
    }
}
