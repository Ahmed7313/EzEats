package com.easyEats.ezeats.ui.main.offers.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.easyEats.ezeats.MyApplication.Companion.baseApplication
import com.easyEats.ezeats.R
import com.easyEats.ezeats.databinding.ItemOffersCategoryBinding
import com.easyEats.ezeats.domain.main.model.response.search_response.Category

/**
 * Adapter for displaying a list of categories in a RecyclerView.
 * It allows for click interactions on categories and highlights the selected category.
 */
class OffersCategoryAdapter(
    private val clickListener: OnCategoryClickListener
) : ListAdapter<Category, OffersCategoryAdapter.SearchHistoryViewHolder>(SearchHistoryDiffCallback()) {

    /**
     * Interface for handling category item click events.
     */
    interface OnCategoryClickListener {
        fun onCategoryClicked(category: Category)
    }

    private var selectedPosition = 0 // Keeps track of the selected category position.

    /**
     * ViewHolder that binds category data to the view and handles category click events.
     */
    inner class SearchHistoryViewHolder(val binding: ItemOffersCategoryBinding) :
        RecyclerView.ViewHolder(binding.root) {

        /**
         * Binds the category data to the view and handles click interactions.
         * It also changes the text color based on the selected state of the category.
         */
        fun bind(category: Category, position: Int) {
            binding.category = category
            binding.clickListener = clickListener

            // Change text color based on selection
            binding.tvTitle.setTextColor(
                if (selectedPosition == position) baseApplication.getColor(R.color.main_color) else Color.BLACK
            )

            binding.root.setOnClickListener {
                selectedPosition = position
                notifyDataSetChanged()
                // Trigger the click listener
                clickListener.onCategoryClicked(category)
            }
        }
    }

    /**
     * Creates a new ViewHolder and inflates the category item layout.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchHistoryViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemOffersCategoryBinding.inflate(inflater, parent, false)
        return SearchHistoryViewHolder(binding)
    }

    /**
     * Binds category data to the ViewHolder.
     */
    override fun onBindViewHolder(holder: SearchHistoryViewHolder, position: Int) {
        val category = getItem(position)
        holder.bind(category, position)
    }
}

/**
 * DiffUtil callback for comparing category items.
 * Used to efficiently update the RecyclerView when the list of categories changes.
 */
class SearchHistoryDiffCallback : DiffUtil.ItemCallback<Category>() {

    /**
     * Compares the unique identifiers of the categories to determine if they represent the same item.
     */
    override fun areItemsTheSame(oldItem: Category, newItem: Category): Boolean {
        return oldItem.categoryId == newItem.categoryId // Compare by categoryId, assuming it’s unique.
    }

    /**
     * Compares the contents of the category items to check if they are the same.
     */
    override fun areContentsTheSame(oldItem: Category, newItem: Category): Boolean {
        return oldItem == newItem
    }
}

