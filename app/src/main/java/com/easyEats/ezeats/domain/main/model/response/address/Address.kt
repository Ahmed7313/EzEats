package com.easyEats.ezeats.domain.main.model.response.address


import com.google.gson.annotations.SerializedName

data class Address(
    @SerializedName("addressLabel")
    var addressLabel: String? = "",
    @SerializedName("apartmentNumber")
    var apartmentNumber: String? = "",
    @SerializedName("buildingNumber")
    var buildingNumber: String? = "",
    @SerializedName("compound")
    var compound: String? = "",
    @SerializedName("floorNumber")
    var floorNumber: Int? = 0,
    @SerializedName("streetName")
    var streetName: String? = "",
    @SerializedName("userLocation")
    var userLocation: UserLocation? = UserLocation()
)