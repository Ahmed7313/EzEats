package com.easyEats.ezeats.ui.main.search_history.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.easyEats.ezeats.databinding.ItemCategoryBinding
import com.easyEats.ezeats.domain.main.model.response.search_response.Category


class CategoryAdapter(
    private val clickListener: OnCategoryClickListener
) : ListAdapter<Category, CategoryAdapter.SearchHistoryViewHolder>(SearchHistoryDiffCallback()) {

    interface OnCategoryClickListener {
        fun onCategoryClicked(category: Category)
    }

    inner class SearchHistoryViewHolder(val binding: ItemCategoryBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchHistoryViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemCategoryBinding.inflate(inflater, parent, false)
        return SearchHistoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SearchHistoryViewHolder, position: Int) {
        val category = getItem(position)
        holder.binding.category = category
        holder.binding.clickListener = clickListener
    }
}

class SearchHistoryDiffCallback : DiffUtil.ItemCallback<Category>() {
    override fun areItemsTheSame(oldItem: Category, newItem: Category): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Category, newItem: Category): Boolean {
        return oldItem == newItem
    }
}