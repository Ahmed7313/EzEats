package com.easyEats.ezeats.domain.main.model.response.restaurant_menu.check_table_availability


import com.google.gson.annotations.SerializedName

data class CurrentOrder(
    @SerializedName("objectId")
    var currentOrderId: String? = "",
    @SerializedName("userId")
    var userId: String? = ""
)