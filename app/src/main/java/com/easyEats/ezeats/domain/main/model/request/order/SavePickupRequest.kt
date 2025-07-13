package com.easyEats.ezeats.domain.main.model.request.order


import com.google.gson.annotations.SerializedName

data class SavePickupRequest(
    @SerializedName("compoundName")
    var compoundName: String? = "",
    @SerializedName("orderId")
    var orderId: String? = "",
    @SerializedName("pickUpStation")
    var pickUpStation: String? = "Restaurant"
)