package com.easyEats.ezeats.ui.main.percent.adapter

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.easyEats.ezeats.MyApplication.Companion.TAG
import com.easyEats.ezeats.MyApplication.Companion.baseApplication
import com.easyEats.ezeats.R
import com.easyEats.ezeats.databinding.ItemOffersCategoryBinding
import com.easyEats.ezeats.domain.main.model.response.search_response.Category


class OffersCategoryAdapter(
    private val clickListener: OnCategoryClickListener
) : ListAdapter<Category, OffersCategoryAdapter.SearchHistoryViewHolder>(SearchHistoryDiffCallback()) {

    interface OnCategoryClickListener {
        fun onCategoryClicked(category: Category)
    }

    private var selectedPosition = 0

    inner class SearchHistoryViewHolder(val binding: ItemOffersCategoryBinding) :
        RecyclerView.ViewHolder(binding.root) {

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
                // Trigger click listener
                clickListener.onCategoryClicked(category)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchHistoryViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemOffersCategoryBinding.inflate(inflater, parent, false)
        return SearchHistoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SearchHistoryViewHolder, position: Int) {
        val category = getItem(position)
        holder.bind(category, position)
    }
}

class SearchHistoryDiffCallback : DiffUtil.ItemCallback<Category>() {
    override fun areItemsTheSame(oldItem: Category, newItem: Category): Boolean {
        return oldItem.categoryId == newItem.categoryId // Make sure you compare unique identifiers
    }

    override fun areContentsTheSame(oldItem: Category, newItem: Category): Boolean {
        return oldItem == newItem
    }
}
