package com.easyEats.ezeats.ui.main.search_history

import android.content.DialogInterface
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.easyEats.ezeats.MyApplication.Companion.TAG
import com.easyEats.ezeats.R
import com.easyEats.ezeats.base.BaseBottomSheetFragment
import com.easyEats.ezeats.base.BaseFragment
import com.easyEats.ezeats.databinding.FragmentSearchHistoryBinding
import com.easyEats.ezeats.domain.main.model.response.search_response.Category
import com.easyEats.ezeats.domain.main.model.response.search_response.PopularSearch
import com.easyEats.ezeats.ui.main.main.MainViewModel
import com.easyEats.ezeats.ui.main.search_history.adapter.CategoryAdapter
import com.easyEats.ezeats.ui.main.search_history.adapter.PopularSearchAdapter
import com.easyEats.ezeats.ui.main.search_history.adapter.RecentSearchAdapter
import com.easyEats.ezeats.ui.main.search_suggestion.SearchFragmentDirections
import com.easyEats.ezeats.utils.Resource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * Fragment class responsible for displaying the search history, including categories,
 * popular searches, and recent searches. The fragment allows the user to interact with
 * the search history items and navigate to search results or other fragments.
 *
 * Implements:
 * - [CategoryAdapter.OnCategoryClickListener] to handle clicks on categories.
 * - [PopularSearchAdapter.OnPopularSearchClickListener] to handle clicks on popular searches.
 * - [RecentSearchAdapter.OnRecentSearchClickListener] to handle clicks on recent searches.
 *
 * Dependencies:
 * - [MainViewModel] is shared across multiple fragments and activities.
 * - [SearchHistoryViewModel] manages the data and business logic related to search history.
 *
 * Annotations:
 * - [AndroidEntryPoint] is used to inject dependencies via Hilt.
 */
@AndroidEntryPoint
class SearchHistoryFragment : BaseFragment<FragmentSearchHistoryBinding>(),
    CategoryAdapter.OnCategoryClickListener, PopularSearchAdapter.OnPopularSearchClickListener,
    RecentSearchAdapter.OnRecentSearchClickListener {

    private val viewModel: MainViewModel by activityViewModels()

    // Shared ViewModel between multiple fragments and activities
    private val mainViewModel: MainViewModel by activityViewModels()

    // Adapter for displaying categories in the search history
    private val categoryAdapter by lazy {
        CategoryAdapter(this)
    }

    // Adapter for displaying popular searches
    private val popularSearchAdapter by lazy {
        PopularSearchAdapter(this)
    }

    // Adapter for displaying recent searches
    private val recentSearchAdapter by lazy {
        RecentSearchAdapter(this)
    }

    // ViewModel specific to this fragment, managing search history
    private val searchHistoryViewModel: SearchHistoryViewModel by viewModels()

    /**
     * Sets up the views in the fragment, including click listeners and adapters.
     */
    override fun setUpViews() {
        binding.searchHistoryViewModel = searchHistoryViewModel
        binding.mcvSearch.setOnClickListener {
            findNavController().navigate(R.id.goToSearchFragment)
        }

        binding.tvLocation.setOnClickListener {
            findNavController().navigate(R.id.location_bottom_sheet)
        }
    }

    /**
     * Called when the fragment resumes. Sets up the RecyclerView adapters and fetches search history data.
     */
    override fun onResume() {
        super.onResume()
        binding.rvFood.adapter = categoryAdapter
        binding.rvPopularSearch.adapter = popularSearchAdapter
        binding.rvRecentSearch.layoutManager = GridLayoutManager(requireContext(), 3)
        binding.rvRecentSearch.adapter = recentSearchAdapter
        binding.rvPopularSearch.layoutManager = GridLayoutManager(requireContext(), 3)
        searchHistoryViewModel.getSearchHistory()

    }


    /**
     * Observes LiveData objects in the ViewModel to update the UI based on changes in data.
     */
    override fun listenToObservers() {
        searchHistoryViewModel.searchHistoryResultLiveData.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Error -> {}
                is Resource.Loading -> {}
                is Resource.Success -> {
                    searchHistoryViewModel.setData(
                        categoryAdapter,
                        popularSearchAdapter,
                        recentSearchAdapter,
                        binding,
                        resource
                    )
                }

                is Resource.UnAuthenticated -> {}
                else ->{}

            }
        }

        searchHistoryViewModel.clearSearchHistoryResultLiveData.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Error -> {}
                is Resource.Loading -> {
                    searchHistoryViewModel.getSearchHistory()
                }

                is Resource.Success -> {
                    searchHistoryViewModel.getSearchHistory()
                }

                is Resource.UnAuthenticated -> {}
                else ->{}

            }
        }

        lifecycleScope.launch {
            mainViewModel.userLocationAddressMutableLiveData.collect {
                binding.tvLocation.text = if (it?.address != null) {
                    "You're at ${it?.address}"
                } else {
                    getString(R.string.select_location)
                }

            }
        }
    }

    /**
     * Inflates the fragment's view binding.
     */
    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentSearchHistoryBinding.inflate(layoutInflater)

    /**
     * Handles clicks on category items.
     */
    override fun onCategoryClicked(category: Category) {
        val navController = findNavController()
        navController.navigate(
            SearchFragmentDirections.goToSearchResultFragment(category.categoryName ?: "")
        )
    }

    /**
     * Handles clicks on popular search items.
     */
    override fun onPopularSearchClicked(popularSearch: PopularSearch) {
        findNavController().navigate(
            SearchFragmentDirections.goToSearchResultFragment(popularSearch.term ?: "")
        )
    }

    /**
     * Handles clicks on recent search items.
     */
    override fun onRecentSearchClickListener(recentSearch: String) {
        findNavController().navigate(
            SearchFragmentDirections.goToSearchResultFragment(recentSearch ?: "")
        )
    }


}
