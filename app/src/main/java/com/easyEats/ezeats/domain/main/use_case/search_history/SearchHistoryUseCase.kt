package com.easyEats.ezeats.domain.main.use_case.search_history

import com.easyEats.ezeats.domain.main.repository.MainRepository
import javax.inject.Inject

class SearchHistoryUseCase @Inject constructor(private val mainRepository: MainRepository) {
    suspend fun searchHistory() = mainRepository.searchScreen()

    suspend fun clearRecentSearches() = mainRepository.clearRecentSearches()

}
