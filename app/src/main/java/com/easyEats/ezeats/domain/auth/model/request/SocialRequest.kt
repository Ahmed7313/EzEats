package com.easyEats.ezeats.domain.auth.model.request

import com.google.gson.annotations.SerializedName

data class SocialRequest(
    @SerializedName("token")
    val token: String,
    @SerializedName("googleId")
    val googleId: String,
    @SerializedName("email")
    val email: String
)