package com.easyEats.ezeats.ui.auth.on_boarding

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.easyEats.ezeats.MyApplication.Companion.baseApplication
import com.easyEats.ezeats.R
import com.easyEats.ezeats.domain.auth.use_case.AuthUseCase
import com.easyEats.ezeats.domain.main.model.UserLocationAddress
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


    fun setIntroSlides(slides: List<IntroSlide>) {
        _introSlides.value = slides
    }

    /**
     * Initializes the ViewModel and loads the introductory slides.
     * This is called when the ViewModel is created and sets up the initial state.
     */
    init {
    }

    /**
     * Loads the introductory slides with titles, descriptions, and icons.
     * This data is used to populate the onboarding screens.
     */



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
    fun saveAddress(userLocationAddress: UserLocationAddress) {
        viewModelScope.launch {
            authUseCase.onBoardingUseCase.saveAddress(userLocationAddress)
        }
    }
}


