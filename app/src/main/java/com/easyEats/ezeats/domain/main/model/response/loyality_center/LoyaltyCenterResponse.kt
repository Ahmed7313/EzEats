package com.easyEats.ezeats.domain.main.model.response.loyality_center


import com.google.gson.annotations.SerializedName

data class LoyaltyCenterResponse(
    @SerializedName("EZPoints")
    val eZPoints: Int?,
    @SerializedName("points")
    val points: Points?,
    @SerializedName("rewards")
    val rewards: Rewards?,
    @SerializedName("spinWheelCodes")
    val spinWheelCodes: Int?,
    @SerializedName("unUsedVouchers")
    val unUsedVouchers: Int?,
    @SerializedName("activePromoCodes")
    val activePromoCodes: Int?,
    @SerializedName("activeSpinWheelVouchers")
    val activeSpinWheelVouchers: Int?
)