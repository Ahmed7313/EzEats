package com.easyEats.ezeats.ui.main.search_history.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.easyEats.ezeats.databinding.ItemChipSearchBinding

/**
 * Adapter used to display a list of recent search terms in a RecyclerView.
 * It binds each recent search term to a chip-style view and handles click events.
 *
 * @param clickListener A callback interface for handling clicks on recent search items.
 */
class RecentSearchAdapter(
    private val clickListener: OnRecentSearchClickListener
) : ListAdapter<String, RecentSearchAdapter.RecentSearchViewHolder>(RecentSearchDiffCallback()) {

    /**
     * Interface to handle click events for recent search items.
     * The implementing class should define the `onRecentSearchClickListener` method.
     */
    interface OnRecentSearchClickListener {
        /**
         * This method is called when a recent search item is clicked.
         *
         * @param recentSearch The clicked recent search term.
         */
        fun onRecentSearchClickListener(recentSearch: String)
    }

    /**
     * ViewHolder class that binds the recent search data to the view.
     *
     * @param binding The binding object that connects the recent search term to the view.
     */
    inner class RecentSearchViewHolder(val binding: ItemChipSearchBinding) :
        RecyclerView.ViewHolder(binding.root)

    /**
     * Creates a new ViewHolder to represent a recent search item in the RecyclerView.
     *
     * @param parent The parent view where the item will be placed.
     * @param viewType The type of the view to be created (not used here).
     * @return A new instance of RecentSearchViewHolder.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecentSearchViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemChipSearchBinding.inflate(inflater, parent, false)
        return RecentSearchViewHolder(binding)
    }

    /**
     * Binds the recent search data to the ViewHolder.
     * This method also sets up the click listener for the recent search item.
     *
     * @param holder The ViewHolder to bind the data to.
     * @param position The position of the item in the list.
     */
    override fun onBindViewHolder(holder: RecentSearchViewHolder, position: Int) {
        val recentSearch = getItem(position)
        holder.binding.recentSearch = recentSearch
        holder.binding.clickListener = clickListener
    }
}

/**
 * DiffUtil callback used to optimize RecyclerView updates when the list of recent searches changes.
 * It checks whether two recent search items are the same and if their contents are the same.
 */
class RecentSearchDiffCallback : DiffUtil.ItemCallback<String>() {

    /**
     * Determines whether two recent search items represent the same object.
     *
     * @param oldItem The old recent search term.
     * @param newItem The new recent search term.
     * @return True if the two items are the same, false otherwise.
     */
    override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
        return oldItem == newItem
    }

    /**
     * Determines whether the contents of two recent search items are the same.
     *
     * @param oldItem The old recent search term.
     * @param newItem The new recent search term.
     * @return True if the contents are the same, false otherwise.
     */
    override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
        return oldItem == newItem
    }
}
