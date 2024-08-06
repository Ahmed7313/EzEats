package com.easyEats.ezeats.ui.auth.on_boarding

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.easyEats.ezeats.MyApplication.Companion.baseApplication
import com.easyEats.ezeats.R
import com.easyEats.ezeats.domain.auth.use_case.AuthUseCase
import com.easyEats.ezeats.ui.auth.on_boarding.pager.IntroSlide
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * OnBoardingViewModel manages the data and logic for the onboarding process.
 * It provides a list of introductory slides to be displayed during the onboarding flow.
 *
 * This ViewModel uses the `@HiltViewModel` annotation to enable dependency injection with Hilt.
 *
 * @property authUseCase The use case that handles authentication and onboarding operations.
 */
@HiltViewModel
class OnBoardingViewModel @Inject constructor(
    private val authUseCase: AuthUseCase
) : ViewModel() {

    // LiveData to expose the list of introductory slides
    private val _introSlides = MutableLiveData<List<IntroSlide>>()
    val introSlides: LiveData<List<IntroSlide>> = _introSlides

    /**
     * Initializes the ViewModel and loads the introductory slides.
     * This is called when the ViewModel is created and sets up the initial state.
     */
    init {
        loadIntroSlides()
    }

    /**
     * Loads the introductory slides with titles, descriptions, and icons.
     * This data is used to populate the onboarding screens.
     */
    private fun loadIntroSlides() {
        _introSlides.value = listOf(
            IntroSlide(
                baseApplication.getString(R.string.welcome_to_ezeats),
                baseApplication.getString(R.string.ezeats_is_your_destination_to_saving_on_your_favorite_restaurants),
                R.drawable.welcome_icon
            ),
            IntroSlide(
                baseApplication.getString(R.string.save_money_earn_rewards),
                baseApplication.getString(R.string.with_ezeats_you_can_save_money_get_special_offers_get_points_that_you_can_redeem_and_much_more),
                R.drawable.gift_icon
            ),
            IntroSlide(
                baseApplication.getString(R.string.location_access),
                baseApplication.getString(R.string.we_would_like_to_get_access_to_your_location_so_we_can_always_notify_you_about_the_best_near_deals),
                R.drawable.location_icon
            )
        )
    }


    /**
     * Updates the status of whether the user has watched the onboarding screens.
     * This status is used to determine if the onboarding process needs to be shown again.
     *
     * @param isWatched A boolean indicating if the user has watched the onboarding screens.
     */
    fun userWatchOnBoarding(isWatched: Boolean) {
        viewModelScope.launch {
            authUseCase.onBoardingUseCase.userWatchOnBoardingScreen(isWatched)
        }
    }


    /**
     * Saves the user's address.
     * This function is called to persist the address entered by the user during the onboarding process.
     *
     * @param address The address string to be saved.
     */
    fun saveAddress(address: String) {
        viewModelScope.launch {
            authUseCase.onBoardingUseCase.saveAddress(address)
        }
    }
}


