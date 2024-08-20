package com.easyEats.ezeats.ui.main.search_suggestion

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.easyEats.ezeats.base.BaseBottomSheetFragment
import com.easyEats.ezeats.databinding.FragmentSearchBinding
import com.easyEats.ezeats.ui.main.search_suggestion.adapter.SuggestionsAdapter
import com.easyEats.ezeats.utils.Resource
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class SearchFragment : BaseBottomSheetFragment<FragmentSearchBinding>(),
    SuggestionsAdapter.OnSuggestionClickListener {
    private val viewModel: SearchViewModel by viewModels()
    private lateinit var suggestionsAdapter: SuggestionsAdapter


    override fun inflateBinding(
        inflater: LayoutInflater, container: ViewGroup?
    ) = FragmentSearchBinding.inflate(layoutInflater)

    override fun setUpViews() {
        binding.searchViewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        suggestionsAdapter = SuggestionsAdapter(viewModel, this)
        binding.rvSuggestionResult.adapter = suggestionsAdapter


    }

    override fun listenToObservers() {
        viewModel.searchKey.observe(viewLifecycleOwner) {
            viewModel.getSearchResult()
        }

        viewModel.searchSuggestionResultLiveData.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Error -> {}
                is Resource.Loading -> {}
                is Resource.Success -> {
                    suggestionsAdapter.notifyDataSetChanged()
                    suggestionsAdapter.submitList(resource.data?.result?.data)
                }

                is Resource.UnAuthenticated -> {}
            }
        }

    }


    override fun onClick(suggestion: String) {
        findNavController().navigate(SearchFragmentDirections.searchResultFragment(suggestion))
    }

}