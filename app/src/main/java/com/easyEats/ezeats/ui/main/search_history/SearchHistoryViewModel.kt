package com.easyEats.ezeats.ui.main.search_history

import android.util.Log
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.easyEats.ezeats.MyApplication.Companion.TAG
import com.easyEats.ezeats.base.BaseResponse
import com.easyEats.ezeats.databinding.FragmentSearchHistoryBinding
import com.easyEats.ezeats.domain.main.model.response.search_response.SearchScreenResponse
import com.easyEats.ezeats.domain.main.use_case.MainUseCase
import com.easyEats.ezeats.ui.main.main.MainViewModel
import com.easyEats.ezeats.ui.main.search_history.adapter.CategoryAdapter
import com.easyEats.ezeats.ui.main.search_history.adapter.PopularSearchAdapter
import com.easyEats.ezeats.ui.main.search_history.adapter.RecentSearchAdapter
import com.easyEats.ezeats.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import javax.inject.Inject


/**
 * ViewModel class for managing the data and business logic related to search history
 * in the `SearchHistoryFragment`. It handles fetching search history, clearing recent
 * searches, and updating UI components with the data.
 *
 * @property mainUseCase The use case that provides access to the necessary business logic
 * for search history operations.
 *
 * Inherits from [MainViewModel] to leverage common ViewModel functionality.
 */
@HiltViewModel
class SearchHistoryViewModel @Inject constructor(private val mainUseCase: MainUseCase) :
    MainViewModel(mainUseCase) {

    // LiveData to show/hide shimmer (loading indicator) in the UI
    val showShimmer = MutableLiveData(true)

    // LiveData for search history result, observed by the UI to update the search history list
    private val _searchHistoryResultMutableLiveData =
        MutableLiveData<Resource<BaseResponse<SearchScreenResponse>>>()
    val searchHistoryResultLiveData: LiveData<Resource<BaseResponse<SearchScreenResponse>>> get() = _searchHistoryResultMutableLiveData

    // LiveData for clearing search history result, observed by the UI to handle the result
    private val _clearSearchHistoryResultMutableLiveData =
        MutableLiveData<Resource<BaseResponse<Any>>>()
    val clearSearchHistoryResultLiveData: LiveData<Resource<BaseResponse<Any>>> get() = _clearSearchHistoryResultMutableLiveData

    /**
     * Fetches the search history data and updates the [searchHistoryResultLiveData]
     * with the result.
     */
    fun getSearchHistory() {
        val flow = createSearchHistoryResultRequest()
        apply(
            flow = flow,
            maxRetries = 2,
            onError = { error ->
                _searchHistoryResultMutableLiveData.value =
                    Resource.Error("No Internet Connection")
            },
            onSuccess = { resource ->
                showShimmer.value = false
                _searchHistoryResultMutableLiveData.value = resource
            }
        )
    }

    /**
     * Creates a Flow to handle the search history request.
     * Sends the loading state initially, then fetches the search history data
     * and handles the response accordingly.
     */
    private fun createSearchHistoryResultRequest(): Flow<Resource<BaseResponse<SearchScreenResponse>>> =
        channelFlow {
            send(Resource.Loading())
            showShimmer.value = true
            val homeResult = mainUseCase.searchHistoryUseCase.searchHistory()
            homeResult.handleResponse(
                onSuccess = {
                    send(Resource.Success(homeResult))
                },
                onError = { message, error ->
                    send(Resource.Error(message, error))
                }, onUnauthenticated = {
                    send(Resource.UnAuthenticated())
                }
            )
        }

    /**
     * Clears the recent search history and updates the [clearSearchHistoryResultLiveData]
     * with the result.
     */
    fun clearRecentSearch() {
        val flow = createClearSearchHistoryResultRequest()
        apply(
            flow = flow,
            maxRetries = 2,
            onError = { error ->
                _clearSearchHistoryResultMutableLiveData.value =
                    Resource.Error("No Internet Connection")
                Log.d(TAG, "clearRecentSearch: ${"No Internet Connection"}")
            },
            onSuccess = { resource ->
                showShimmer.value = false
                _clearSearchHistoryResultMutableLiveData.value = resource
            }
        )
    }

    /**
     * Creates a Flow to handle the clear search history request.
     * Sends the loading state initially, then clears the search history and
     * handles the response accordingly.
     */
    private fun createClearSearchHistoryResultRequest(): Flow<Resource<BaseResponse<Any>>> =
        channelFlow {
            send(Resource.Loading())
            showShimmer.value = true
            val clearRecentSearchResult = mainUseCase.searchHistoryUseCase.clearRecentSearches()
            clearRecentSearchResult.handleResponse(
                onSuccess = {
                    send(Resource.Success(clearRecentSearchResult))
                },
                onError = { message, error ->
                    send(Resource.Error(message, error))
                }, onUnauthenticated = {
                    send(Resource.UnAuthenticated())
                }
            )
        }

    /**
     * Sets the data for the category, popular search, and recent search adapters, and
     * updates the UI components accordingly.
     *
     * @param categoryAdapter Adapter for the category RecyclerView
     * @param popularSearchAdapter Adapter for the popular search RecyclerView
     * @param recentSearchAdapter Adapter for the recent search RecyclerView
     * @param binding Binding object for the `SearchHistoryFragment`
     * @param resource Resource containing the search history data
     */
    fun setData(
        categoryAdapter: CategoryAdapter,
        popularSearchAdapter: PopularSearchAdapter,
        recentSearchAdapter: RecentSearchAdapter,
        binding: FragmentSearchHistoryBinding,
        resource: Resource.Success<BaseResponse<SearchScreenResponse>>
    ) {
        categoryAdapter.submitList(resource.data?.result?.data?.categories)
        popularSearchAdapter.submitList(resource.data?.result?.data?.popularSearches)
        recentSearchAdapter.submitList(resource.data?.result?.data?.recentSearches)
        if (resource.data?.result?.data?.recentSearches!!.isEmpty())
            binding.llRecentSearch.visibility = View.GONE
    }
}
