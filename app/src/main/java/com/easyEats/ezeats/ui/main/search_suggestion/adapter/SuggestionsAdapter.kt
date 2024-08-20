package com.easyEats.ezeats.ui.main.search_suggestion.adapter

import android.graphics.Color
import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.easyEats.ezeats.databinding.ItemSuggestionBinding
import com.easyEats.ezeats.ui.main.search_suggestion.SearchViewModel

class SuggestionsAdapter(
    private val viewModel: SearchViewModel,
    private val clickListener: OnSuggestionClickListener // Interface for click handling
) : ListAdapter<String, SuggestionsAdapter.SuggestionViewHolder>(SuggestionDiffCallback()) {

    interface OnSuggestionClickListener {
        fun onClick(suggestion: String)
    }

    inner class SuggestionViewHolder(val binding: ItemSuggestionBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SuggestionViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemSuggestionBinding.inflate(inflater, parent, false)
        return SuggestionViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SuggestionViewHolder, position: Int) {
        val suggestion = getItem(position)
        holder.binding.suggestion = suggestion
        holder.binding.clickListener = clickListener

        // Highlight search key in the suggestion
        holder.binding.tvSuggestion.text = highlightSearchKey(suggestion, viewModel.searchKey.value ?: "")
    }

    private fun highlightSearchKey(text: String, searchKey: String): SpannableString {
        val spannable = SpannableString(text)
        val startIndex = text.indexOf(searchKey, ignoreCase = true)

        if (startIndex >= 0) {
            spannable.setSpan(
                StyleSpan(Typeface.BOLD),
                startIndex,
                startIndex + searchKey.length,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            spannable.setSpan(
                ForegroundColorSpan(Color.BLACK),
                startIndex,
                startIndex + searchKey.length,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }

        return spannable
    }
}

class SuggestionDiffCallback : DiffUtil.ItemCallback<String>() {
    override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
        return oldItem == newItem
    }
}
