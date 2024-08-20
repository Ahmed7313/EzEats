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

@HiltViewModel
class SearchViewModel @Inject constructor(private val mainUseCase: MainUseCase) :
    MainViewModel(mainUseCase.mainUseCase) {

    val searchKey = MutableLiveData("")

    private val _searchSuggestionResultMutableLiveData =
        MutableLiveData<Resource<BaseResponse<List<String>>>>()
    val searchSuggestionResultLiveData: LiveData<Resource<BaseResponse<List<String>>>> get() = _searchSuggestionResultMutableLiveData


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