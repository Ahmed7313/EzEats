package com.easyEats.ezeats.di.main_module

import com.easyEats.ezeats.domain.main.use_case.MainUseCase
import com.easyEats.ezeats.domain.main.use_case.main_use_case.MainActivityUseCase
import com.easyEats.ezeats.domain.main.use_case.home_use_case.HomeUseCase
import com.easyEats.ezeats.domain.main.use_case.location_use_case.LocationUseCase
import com.easyEats.ezeats.domain.main.use_case.offers_use_case.OffersUserCase
import com.easyEats.ezeats.domain.main.use_case.search_history.SearchHistoryUseCase
import com.easyEats.ezeats.domain.main.use_case.search_suggestions.SearchSuggestionsUseCase
import com.easyEats.ezeats.domain.main.use_case.search_use_case.SearchUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class MainUseCaseModule {

    @Provides
    @Singleton
    fun provideMainUseCase(
        mainUseCase: MainActivityUseCase,
        homeUseCase: HomeUseCase,
        searchSuggestionsUseCase: SearchSuggestionsUseCase,
        searchHistoryUseCase: SearchHistoryUseCase,
        locationUseCase: LocationUseCase,
        searchUseCase: SearchUseCase,
        offersUserCase: OffersUserCase
    ): MainUseCase {
        return MainUseCase(
            mainUseCase,
            homeUseCase,
            searchSuggestionsUseCase,
            searchHistoryUseCase,
            locationUseCase,
            searchUseCase,
            offersUserCase
        )
    }
}