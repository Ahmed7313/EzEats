package com.easyEats.ezeats.domain.main.model.response.address


import com.google.gson.annotations.SerializedName

data class UserLocation(
    @SerializedName("latitude")
    var latitude: Double? = 0.0,
    @SerializedName("longitude")
    var longitude: Double? = 0.0,
    @SerializedName("__type")
    var type: String? = ""
)