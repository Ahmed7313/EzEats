package com.easyEats.ezeats.domain.main.model.response.loyality_center


import com.easyEats.ezeats.domain.main.model.response.partners.Logo
import com.google.gson.annotations.SerializedName

data class EntityData(
    @SerializedName("logo")
    val logo: Logo?,
    @SerializedName("userName")
    val userName: String?
)