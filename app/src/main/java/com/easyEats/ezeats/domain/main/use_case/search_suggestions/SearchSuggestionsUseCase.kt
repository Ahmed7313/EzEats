package com.easyEats.ezeats.domain.main.use_case.search_suggestions

import com.easyEats.ezeats.domain.main.model.request.search.SearchSuggestionsRequest
import com.easyEats.ezeats.domain.main.repository.MainRepository
import javax.inject.Inject

class SearchSuggestionsUseCase @Inject constructor(private val mainRepository: MainRepository) {
    suspend fun searchSuggestions(request: SearchSuggestionsRequest) =
        mainRepository.searchSuggestions(request)

}
