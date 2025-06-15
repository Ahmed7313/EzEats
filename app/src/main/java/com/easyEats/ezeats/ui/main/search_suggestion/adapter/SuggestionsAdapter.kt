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


/**
 * Adapter class for displaying search suggestions in a RecyclerView.
 * This adapter highlights the search key within the suggestions and handles click events.
 *
 * @param viewModel The SearchViewModel instance, used for accessing the search key.
 * @param clickListener The listener interface to handle click events on suggestions.
 */
class SuggestionsAdapter(
    private val viewModel: SearchViewModel,
    private val clickListener: OnSuggestionClickListener // Interface for click handling
) : ListAdapter<String, SuggestionsAdapter.SuggestionViewHolder>(SuggestionDiffCallback()) {

    /**
     * Interface definition for a callback to be invoked when a suggestion is clicked.
     */
    interface OnSuggestionClickListener {
        fun onClick(suggestion: String)
    }

    /**
     * ViewHolder class for holding and binding views related to a search suggestion item.
     *
     * @param binding The data binding object for the item layout.
     */
    inner class SuggestionViewHolder(val binding: ItemSuggestionBinding) :
        RecyclerView.ViewHolder(binding.root)

    /**
     * Creates and returns a new ViewHolder object, inflating the item layout.
     *
     * @param parent The parent view that the ViewHolder's view will be attached to.
     * @param viewType The view type of the new view.
     * @return A new SuggestionViewHolder that holds a view for a search suggestion item.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SuggestionViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemSuggestionBinding.inflate(inflater, parent, false)
        return SuggestionViewHolder(binding)
    }

    /**
     * Binds data to the ViewHolder's view, including the suggestion text and click listener.
     * The search key within the suggestion is highlighted for better user visibility.
     *
     * @param holder The ViewHolder which should be updated to represent the contents of the item.
     * @param position The position of the item within the adapter's data set.
     */
    override fun onBindViewHolder(holder: SuggestionViewHolder, position: Int) {
        val suggestion = getItem(position)
        holder.binding.suggestion = suggestion
        holder.binding.clickListener = clickListener

        // Highlight search key in the suggestion text
        holder.binding.tvSuggestion.text = highlightSearchKey(suggestion, viewModel.searchKey.value ?: "")
    }

    /**
     * Highlights the search key within the suggestion text by making it bold and changing its color to black.
     *
     * @param text The full suggestion text.
     * @param searchKey The search key that should be highlighted within the suggestion text.
     * @return A SpannableString with the search key highlighted.
     */
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

/**
 * Callback class for calculating the difference between two search suggestion items.
 * Used to efficiently update the RecyclerView when the data set changes.
 */
class SuggestionDiffCallback : DiffUtil.ItemCallback<String>() {
    override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
        return oldItem == newItem
    }
}

