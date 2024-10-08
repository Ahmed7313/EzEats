package com.easyEats.ezeats.domain.main.model.response.restaurant_reponse

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue


@Parcelize
data class Restaurant(
    val distance: String?,
    val id: String?,
    val locationName: String?,
    val logo: String?,
    val name: String?,
    val offers: @RawValue List<Offer?>?,
    val type: String?,
    val categories:@RawValue List<String>?,
    val rating: Double? = 0.0,
    val reviewsCount: Int? = 0

) : Parcelable