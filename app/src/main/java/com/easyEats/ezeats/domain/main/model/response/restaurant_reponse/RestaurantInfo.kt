package com.easyEats.ezeats.domain.main.model.response.restaurant_reponse

import android.os.Parcelable
import com.easyEats.ezeats.domain.main.model.response.home_response.HotDeal
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
data class RestaurantInfo(
    val restaurantName: String?,
    val restaurantLogo: String?,
    val restaurantCover: String?,
    val distance: String?,
    val reviewsCount: Int?,
    val rating: Double?,
    val offers: List<HotDeal?>? = arrayListOf(),
) : Parcelable