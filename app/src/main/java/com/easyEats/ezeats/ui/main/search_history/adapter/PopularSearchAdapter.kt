package com.easyEats.ezeats.ui.main.search_history.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.easyEats.ezeats.databinding.ItemPopularChipSearchBinding
import com.easyEats.ezeats.domain.main.model.response.search_response.PopularSearch

class PopularSearchAdapter(
    private val clickListener: OnPopularSearchClickListener
) : ListAdapter<PopularSearch, PopularSearchAdapter.PopularSearchViewHolder>(
    PopularSearchDiffCallback()
) {

    interface OnPopularSearchClickListener {
        fun onPopularSearchClicked(popularSearch: PopularSearch)
    }

    inner class PopularSearchViewHolder(val binding: ItemPopularChipSearchBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PopularSearchViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemPopularChipSearchBinding.inflate(inflater, parent, false)
        return PopularSearchViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PopularSearchViewHolder, position: Int) {
        val category = getItem(position)
        holder.binding.popularSearch = category
        holder.binding.clickListener = clickListener
    }
}

class PopularSearchDiffCallback : DiffUtil.ItemCallback<PopularSearch>() {
    override fun areItemsTheSame(oldItem: PopularSearch, newItem: PopularSearch): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: PopularSearch, newItem: PopularSearch): Boolean {
        return oldItem == newItem
    }

}