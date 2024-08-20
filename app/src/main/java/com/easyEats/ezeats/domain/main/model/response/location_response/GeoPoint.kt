package com.easyEats.ezeats.domain.main.model.response.location_response

import com.google.gson.annotations.SerializedName

data class GeoPoint(
    @SerializedName("__type")
    val type: String?,
    val latitude: Double?,
    val longitude: Double?
)
