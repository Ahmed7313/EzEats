package com.easyEats.ezeats.domain.main.model.response.home_response

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.Serializable

@OptIn(InternalSerializationApi::class)
@Serializable
@Parcelize
data class HotDeal(
    val available: Boolean?,
    val colorGradient: @RawValue ColorGradient?,
    val description: String?,
    val distance: String?,
    val dateForm: @RawValue DateForm?,
    val id: String?,
    val isFeature: @RawValue String?,
    val isFeaturedLogo: String?,
    val isHot: Boolean?,
    val isNew: Boolean?,
    val name: String?,
    val offerLocation: @RawValue String? = null,
    val offerLogo: String?,
    val points: @RawValue String?,
    val pointsType: String?,
    val restaurantLogo: String?,
    val restaurantName: String?,
    val timeZone: String?,
    val to: @RawValue DateForm?
) : Parcelable