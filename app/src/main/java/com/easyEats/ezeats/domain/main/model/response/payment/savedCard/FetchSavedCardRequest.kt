package com.easyEats.ezeats.domain.main.model.response.payment.savedCard

import com.google.gson.annotations.SerializedName

data class FetchSavedCardRequest(
    @SerializedName("restaurantId")
    var restaurantId: String? = "",
    @SerializedName("userId")
    var userId: String? = ""
)