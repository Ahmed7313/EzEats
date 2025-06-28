package com.easyEats.ezeats.domain.main.model.response.restaurant_menu.check_table_availability


import com.google.gson.annotations.SerializedName

data class Result(
    @SerializedName("currentOrder")
    var currentOrder: CurrentOrder? = CurrentOrder(),
    @SerializedName("success")
    var success: Boolean? = false,
    @SerializedName("tableOrder")
    var tableOrder: TableOrder? = TableOrder()
)