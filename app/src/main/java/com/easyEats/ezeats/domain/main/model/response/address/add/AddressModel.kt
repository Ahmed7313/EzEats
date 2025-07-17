package com.easyEats.ezeats.domain.main.model.response.address.add


import com.easyEats.ezeats.domain.main.model.response.address.list.AddressUser
import com.google.gson.annotations.SerializedName

//isam
data class AddressModel(
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
    @SerializedName("objectId")
    var objectId: String? = "",
    @SerializedName("userLocation")
    var userLocation: UserLocation? = UserLocation(),
    @SerializedName("user")
    var user: AddressUser? = AddressUser(),
)