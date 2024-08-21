package com.easyEats.ezeats.ui.main.search_suggestion

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.easyEats.ezeats.base.BaseResponse
import com.easyEats.ezeats.domain.main.model.request.search.SearchSuggestionsRequest
import com.easyEats.ezeats.domain.main.model.response.search_response.SearchScreenResponse
import com.easyEats.ezeats.domain.main.use_case.MainUseCase
import com.easyEats.ezeats.ui.main.main.MainViewModel
import com.easyEats.ezeats.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import javax.inject.Inject

/**
 * ViewModel class responsible for managing the search functionality in the app.
 * This ViewModel handles search input, fetching search suggestions, and providing data
 * to the UI components in the corresponding fragment.
 *
 * The ViewModel interacts with a `MainUseCase` to perform use case-specific operations
 * and manage the business logic.
 */
@HiltViewModel
class SearchViewModel @Inject constructor(private val mainUseCase: MainUseCase) :
    MainViewModel(mainUseCase.mainUseCase) {

    // LiveData for holding the user's search input key
    val searchKey = MutableLiveData("")

    // LiveData for observing the search suggestions result
    private val _searchSuggestionResultMutableLiveData =
        MutableLiveData<Resource<BaseResponse<List<String>>>>()
    val searchSuggestionResultLiveData: LiveData<Resource<BaseResponse<List<String>>>> get() = _searchSuggestionResultMutableLiveData

    /**
     * Triggers the search operation based on the current search key.
     * The search results are fetched and observed through `searchSuggestionResultLiveData`.
     */
    fun getSearchResult() {
        val flow = createSearchResultRequest(SearchSuggestionsRequest(searchKey.value.toString()))
        apply(
            flow = flow,
            maxRetries = 2,
            onError = { error ->
                _searchSuggestionResultMutableLiveData.value =
                    Resource.Error(error.localizedMessage ?: "Unknown error")
            },
            onSuccess = { resource ->
                _searchSuggestionResultMutableLiveData.value = resource
            }
        )
    }

    /**
     * Creates a flow to handle the search result request.
     * This flow sends a loading state, performs the search suggestion request,
     * and handles the response by updating the corresponding LiveData.
     *
     * @param searchSuggestionsRequest The request object containing the search key.
     * @return A Flow that emits the resource containing the search suggestions.
     */
    private fun createSearchResultRequest(searchSuggestionsRequest: SearchSuggestionsRequest): Flow<Resource<BaseResponse<List<String>>>> =
        channelFlow {
            send(Resource.Loading())
            val homeResult =
                mainUseCase.searchSuggestionsUseCase.searchSuggestions(searchSuggestionsRequest)
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
}
