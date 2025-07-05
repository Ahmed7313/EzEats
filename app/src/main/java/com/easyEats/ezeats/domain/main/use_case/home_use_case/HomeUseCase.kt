package com.easyEats.ezeats.domain.main.use_case.home_use_case

import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.easyEats.ezeats.domain.main.model.UserLocationAddress
import com.easyEats.ezeats.domain.main.model.request.cart.TipRequest
import com.easyEats.ezeats.domain.main.model.request.cart.UpdateCartRequest
import com.easyEats.ezeats.domain.main.model.request.firebase_token.UpdateTokeRequest
import com.easyEats.ezeats.domain.main.model.request.home.HomeRequest
import com.easyEats.ezeats.domain.main.model.request.restaurant.RestaurantRequest
import com.easyEats.ezeats.domain.main.repository.MainRepository
import com.easyEats.ezeats.utils.Constants.RATING_KEY
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class HomeUseCase @Inject constructor(private val mainRepository: MainRepository) {
    suspend fun getHomeDetails(request: HomeRequest) =
        mainRepository.getHomeDetails(request)


    suspend fun getAllCompoundData() = mainRepository.getAllCompoundData()




    suspend fun saveAddress(userLocationAddress: UserLocationAddress) =
        mainRepository.saveAddress(userLocationAddress)

    suspend fun getPartners() =
        mainRepository.getPartners()

    suspend fun updateDeviceToken(updateTokeRequest: UpdateTokeRequest) =
        mainRepository.updateDeviceToken(updateTokeRequest)

    suspend fun saveUserRating(isUserViewedRatingDialog: Boolean) {
        mainRepository.saveUserRating(isUserViewedRatingDialog)
    }

    fun checkIfUserViewedRatingDialog() =
        mainRepository.checkIfUserViewedRatingDialog()

    suspend fun getRestaurants(restaurantRequest: com.easyEats.ezeats.domain.main.model.request.RestaurantRequest) =
        mainRepository.getRestaurants(restaurantRequest)


    suspend fun updateCart(restaurantRequest: UpdateCartRequest) =
        mainRepository.updateCart(restaurantRequest)

    suspend fun saveTip(request: TipRequest) = mainRepository.saveTip(request)

}