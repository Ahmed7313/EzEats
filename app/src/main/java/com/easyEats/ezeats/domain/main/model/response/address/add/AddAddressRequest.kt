package com.easyEats.ezeats.domain.main.model.response.address.add


import com.google.gson.annotations.SerializedName

data class AddAddressRequest(
    @SerializedName("addressLabel")
    var addressLabel: String? = "",
    @SerializedName("apartmentNumber")
    var apartmentNumber: String? = "",
    @SerializedName("buildingNumber")
    var buildingNumber: String? = "",
    @SerializedName("compound")
    var compound: String? = "",
    @SerializedName("floorNumber")
    var floorNumber: String? = "",
    @SerializedName("streetName")
    var streetName: String? = "",
    @SerializedName("userId")
    var userId: String? = "",
    @SerializedName("userLat")
    var userLat: String? = "",
    @SerializedName("userLon")
    var userLon: String? = ""
)