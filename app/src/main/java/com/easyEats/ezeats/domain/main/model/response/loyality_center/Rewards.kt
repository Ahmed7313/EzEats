package com.easyEats.ezeats.domain.main.model.response.loyality_center


import com.google.gson.annotations.SerializedName

data class Rewards(
    @SerializedName("promocodes")
    val promocodes: List<Promocode>?,
    @SerializedName("spinWheelVouchers")
    val spinWheelVouchers: List<SpinWheelVoucher>?
)