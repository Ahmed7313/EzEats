package com.easyEats.ezeats.ui.main.search_history.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.easyEats.ezeats.databinding.ItemCategoryBinding
import com.easyEats.ezeats.domain.main.model.response.search_response.Category

/**
 * Adapter used to display a list of categories in a RecyclerView.
 * It binds a list of Category items to the RecyclerView and listens for click events on each item.
 *
 * @param clickListener A callback interface to handle category item clicks.
 */
class CategoryAdapter(
    private val clickListener: OnCategoryClickListener
) : ListAdapter<Category, CategoryAdapter.SearchHistoryViewHolder>(SearchHistoryDiffCallback()) {

    /**
     * Interface for handling clicks on category items.
     * The implementing class should define the `onCategoryClicked` method.
     */
    interface OnCategoryClickListener {
        /**
         * Called when a category item is clicked.
         *
         * @param category The clicked category item.
         */
        fun onCategoryClicked(category: Category)
    }

    /**
     * ViewHolder class for binding the category data to the view.
     *
     * @param binding The binding object for the category item view.
     */
    inner class SearchHistoryViewHolder(val binding: ItemCategoryBinding) :
        RecyclerView.ViewHolder(binding.root)

    /**
     * Creates a new ViewHolder to represent a category item.
     *
     * @param parent The parent view group where the item will be placed.
     * @param viewType The type of the view to create.
     * @return The created ViewHolder for the category item.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchHistoryViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemCategoryBinding.inflate(inflater, parent, false)
        return SearchHistoryViewHolder(binding)
    }

    /**
     * Binds the category data to the ViewHolder.
     *
     * @param holder The ViewHolder to bind the data to.
     * @param position The position of the item in the list.
     */
    override fun onBindViewHolder(holder: SearchHistoryViewHolder, position: Int) {
        val category = getItem(position)
        holder.binding.category = category
        holder.binding.clickListener = clickListener
    }
}

/**
 * DiffUtil callback used to determine if two Category items are the same or have the same contents.
 * It helps to optimize RecyclerView's performance when updating the list.
 */
class SearchHistoryDiffCallback : DiffUtil.ItemCallback<Category>() {

    /**
     * Compares if two items represent the same object.
     *
     * @param oldItem The old Category item.
     * @param newItem The new Category item.
     * @return True if the items are the same, false otherwise.
     */
    override fun areItemsTheSame(oldItem: Category, newItem: Category): Boolean {
        return oldItem == newItem
    }

    /**
     * Compares if the contents of two Category items are the same.
     *
     * @param oldItem The old Category item.
     * @param newItem The new Category item.
     * @return True if the contents are the same, false otherwise.
     */
    override fun areContentsTheSame(oldItem: Category, newItem: Category): Boolean {
        return oldItem == newItem
    }
}
