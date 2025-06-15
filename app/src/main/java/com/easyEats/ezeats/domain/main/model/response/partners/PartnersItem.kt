package com.easyEats.ezeats.domain.main.model.response.partners


import com.google.gson.annotations.SerializedName

data class PartnersItem(
    @SerializedName("logo")
    val logo: Logo?,
    @SerializedName("userName")
    val userName: String?
)