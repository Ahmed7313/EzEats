package com.easyEats.ezeats.ui.main.search_history

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.easyEats.ezeats.base.BaseResponse
import com.easyEats.ezeats.domain.main.model.response.search_response.SearchScreenResponse
import com.easyEats.ezeats.domain.main.use_case.MainUseCase
import com.easyEats.ezeats.ui.main.main.MainViewModel
import com.easyEats.ezeats.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import javax.inject.Inject

@HiltViewModel
class SearchHistoryViewModel @Inject constructor(private val mainUseCase: MainUseCase) :
    MainViewModel(mainUseCase.mainUseCase) {

    val showShimmer = MutableLiveData(true)
    private val _searchHistoryResultMutableLiveData =
        MutableLiveData<Resource<BaseResponse<SearchScreenResponse>>>()
    val searchHistoryResultLiveData: LiveData<Resource<BaseResponse<SearchScreenResponse>>> get() = _searchHistoryResultMutableLiveData


    private val _clearSearchHistoryResultMutableLiveData =
        MutableLiveData<Resource<BaseResponse<Any>>>()
    val clearSearchHistoryResultLiveData: LiveData<Resource<BaseResponse<Any>>> get() = _clearSearchHistoryResultMutableLiveData


    fun getSearchHistory() {
        val flow = createSearchHistoryResultRequest()
        apply(
            flow = flow,
            maxRetries = 2,
            onError = { error ->
                _searchHistoryResultMutableLiveData.value =
                    Resource.Error(error.localizedMessage ?: "Unknown error")
            },
            onSuccess = { resource ->
                showShimmer.value = false
                _searchHistoryResultMutableLiveData.value = resource
            }
        )
    }

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


    fun clearRecentSearch() {
        val flow = createClearSearchHistoryResultRequest()
        apply(
            flow = flow,
            maxRetries = 2,
            onError = { error ->
                _clearSearchHistoryResultMutableLiveData.value =
                    Resource.Error(error.localizedMessage ?: "Unknown error")
            },
            onSuccess = { resource ->
                showShimmer.value = false
                _clearSearchHistoryResultMutableLiveData.value = resource
            }
        )
    }

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
}
