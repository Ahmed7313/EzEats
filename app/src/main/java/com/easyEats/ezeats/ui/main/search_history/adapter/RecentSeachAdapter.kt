package com.easyEats.ezeats.ui.main.search_history.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.easyEats.ezeats.databinding.ItemChipSearchBinding

class RecentSearchAdapter(
    private val clickListener: OnRecentSearchClickListener
) : ListAdapter<String, RecentSearchAdapter.RecentSearchViewHolder>(RecentSearchDiffCallback()) {

    interface OnRecentSearchClickListener {
        fun onRecentSearchClickListener(recentSearch: String)
    }

    inner class RecentSearchViewHolder(val binding: ItemChipSearchBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecentSearchViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemChipSearchBinding.inflate(inflater, parent, false)
        return RecentSearchViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecentSearchViewHolder, position: Int) {
        val recentSearch = getItem(position)
        holder.binding.recentSearch = recentSearch
        holder.binding.clickListener = clickListener
    }
}

class RecentSearchDiffCallback : DiffUtil.ItemCallback<String>() {
    override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
        return oldItem == newItem
    }

}