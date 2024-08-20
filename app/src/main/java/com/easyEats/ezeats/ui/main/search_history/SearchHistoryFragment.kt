package com.easyEats.ezeats.ui.main.search_history

import android.view.LayoutInflater
import android.view.View.GONE
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.easyEats.ezeats.R
import com.easyEats.ezeats.base.BaseBottomSheetFragment
import com.easyEats.ezeats.base.BaseResponse
import com.easyEats.ezeats.databinding.FragmentSearchHistoryBinding
import com.easyEats.ezeats.domain.main.model.response.search_response.Category
import com.easyEats.ezeats.domain.main.model.response.search_response.PopularSearch
import com.easyEats.ezeats.domain.main.model.response.search_response.SearchScreenResponse
import com.easyEats.ezeats.ui.main.main.MainViewModel
import com.easyEats.ezeats.ui.main.search_history.adapter.CategoryAdapter
import com.easyEats.ezeats.ui.main.search_history.adapter.PopularSearchAdapter
import com.easyEats.ezeats.ui.main.search_history.adapter.RecentSearchAdapter
import com.easyEats.ezeats.ui.main.search_suggestion.SearchFragmentDirections
import com.easyEats.ezeats.utils.Resource
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class SearchHistoryFragment : BaseBottomSheetFragment<FragmentSearchHistoryBinding>(),
    CategoryAdapter.OnCategoryClickListener, PopularSearchAdapter.OnPopularSearchClickListener,
    RecentSearchAdapter.OnRecentSearchClickListener {

    private val mainViewModel: MainViewModel by activityViewModels()

    private val categoryAdapter by lazy {
        CategoryAdapter(this)
    }
    private val popularSearchAdapter by lazy {
        PopularSearchAdapter(
            this
        )
    }

    private val recentSearchAdapter by lazy {
        RecentSearchAdapter(
            this
        )
    }

    private val searchHistoryViewModel: SearchHistoryViewModel by viewModels()
    override fun setUpViews() {
        binding.searchHistoryViewModel = searchHistoryViewModel
        binding.mcvSearch.setOnClickListener {
            findNavController().navigate(R.id.searchFragment)
        }

        binding.tvLocation.setOnClickListener {
            findNavController().navigate(R.id.location_bottom_sheet)
        }
    }

    override fun onResume() {
        super.onResume()
        binding.rvFood.adapter = categoryAdapter
        binding.rvPopularSearch.adapter = popularSearchAdapter
        binding.rvRecentSearch.layoutManager = GridLayoutManager(requireContext(), 3)
        binding.rvRecentSearch.adapter = recentSearchAdapter
        binding.rvPopularSearch.layoutManager = GridLayoutManager(requireContext(), 3)
        searchHistoryViewModel.getSearchHistory()
    }

    override fun listenToObservers() {
        searchHistoryViewModel.searchHistoryResultLiveData.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Error -> {}
                is Resource.Loading -> {}
                is Resource.Success -> {
                    setData(resource)
                }

                is Resource.UnAuthenticated -> {}
            }
        }

        searchHistoryViewModel.clearSearchHistoryResultLiveData.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Error -> {}
                is Resource.Loading -> {}
                is Resource.Success -> {
                    searchHistoryViewModel.getSearchHistory()
                }

                is Resource.UnAuthenticated -> {}
            }
        }

        mainViewModel.userLocationAddressMutableLiveData.observe(viewLifecycleOwner) {
            it?.let {
                binding.tvLocation.text = it.address ?: getString(R.string.select_location)
            }
        }
    }

    private fun setData(resource: Resource.Success<BaseResponse<SearchScreenResponse>>) {
        categoryAdapter.submitList(resource.data?.result?.data?.categories)
        popularSearchAdapter.submitList(resource.data?.result?.data?.popularSearches)
        recentSearchAdapter.submitList(resource.data?.result?.data?.recentSearches)
        if (resource.data?.result?.data?.recentSearches!!.isEmpty())
            binding.llRecentSearch.visibility = GONE
    }

    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentSearchHistoryBinding.inflate(layoutInflater)

    override fun onCategoryClicked(category: Category) {
        findNavController().navigate(
            SearchFragmentDirections.searchResultFragment(category.categoryName ?: "")
        )
    }

    override fun onPopularSearchClicked(popularSearch: PopularSearch) {
        findNavController().navigate(
            SearchFragmentDirections.searchResultFragment(popularSearch.term ?: "")
        )
    }

    override fun onRecentSearchClickListener(recentSearch: String) {
        findNavController().navigate(
            SearchFragmentDirections.searchResultFragment(recentSearch ?: "")
        )
    }


}