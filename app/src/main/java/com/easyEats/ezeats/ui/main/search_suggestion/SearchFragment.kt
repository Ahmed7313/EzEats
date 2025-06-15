package com.easyEats.ezeats.ui.main.search_suggestion

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.easyEats.ezeats.MyApplication.Companion.TAG
import com.easyEats.ezeats.base.BaseBottomSheetFragment
import com.easyEats.ezeats.base.BaseFragment
import com.easyEats.ezeats.databinding.FragmentSearchBinding
import com.easyEats.ezeats.ui.main.search_suggestion.adapter.SuggestionsAdapter
import com.easyEats.ezeats.utils.Resource
import dagger.hilt.android.AndroidEntryPoint

/**
 * Fragment class for handling the search functionality in the app.
 * It displays search suggestions based on the user's input and navigates to
 * search results when a suggestion is clicked.
 *
 * This fragment uses a `SearchViewModel` to manage the search data and logic.
 * The search results are displayed in a RecyclerView using a `SuggestionsAdapter`.
 */
@AndroidEntryPoint
class SearchFragment : BaseFragment<FragmentSearchBinding>(),
    SuggestionsAdapter.OnSuggestionClickListener {

    // ViewModel for managing search-related data and operations
    private val viewModel: SearchViewModel by viewModels()

    // Adapter for displaying search suggestions in a RecyclerView
    private lateinit var suggestionsAdapter: SuggestionsAdapter

    /**
     * Inflates the view binding for this fragment.
     *
     * @param inflater The LayoutInflater object that can be used to inflate any views in the fragment.
     * @param container If non-null, this is the parent view that the fragment's UI should be attached to.
     * @return The inflated view binding object.
     */
    override fun inflateBinding(
        inflater: LayoutInflater, container: ViewGroup?
    ) = FragmentSearchBinding.inflate(layoutInflater)

    /**
     * Sets up the views and binds the ViewModel to the UI components.
     * Initializes the suggestions adapter and sets it to the RecyclerView.
     */
    override fun setUpViews() {
        binding.searchViewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        suggestionsAdapter = SuggestionsAdapter(viewModel, this)
        binding.rvSuggestionResult.adapter = suggestionsAdapter
        binding.etRealSearch.requestFocus()
        showKeyboard(binding.etRealSearch)

    }

    /**
     * Observes LiveData from the ViewModel and updates the UI components accordingly.
     * It listens to changes in the search key and triggers search results when the key changes.
     */
    override fun listenToObservers() {
        viewModel.searchKey.observe(viewLifecycleOwner) {
            viewModel.getSearchResult()
            viewModel.isEmptySearch()
        }

        viewModel.searchSuggestionResultLiveData.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Error -> {
                    // Handle error state
                }

                is Resource.Loading -> {
                    // Handle loading state
                }

                is Resource.Success -> {
                    // Update the suggestions adapter with the new data
                    suggestionsAdapter.notifyDataSetChanged()
                    suggestionsAdapter.submitList(resource.data?.result?.data)
                }

                is Resource.UnAuthenticated -> {
                    // Handle unauthenticated state
                }
            }
        }
    }

    /**
     * Handles the click event on a search suggestion.
     * Navigates to the search results screen based on the selected suggestion.
     *
     * @param suggestion The selected suggestion string.
     */
    override fun onClick(suggestion: String) {
        findNavController().navigate(SearchFragmentDirections.goToSearchResultFragment(suggestion))
    }

    private fun showKeyboard(editText: EditText) {
        val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT)
    }

}
