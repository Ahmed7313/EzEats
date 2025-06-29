package com.easyEats.ezeats.domain.main.model.response.compound


import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class PreviewPhoto(
    @SerializedName("name")
    var name: String? = "",
    @SerializedName("__type")
    var type: String? = "",
    @SerializedName("url")
    var url: String? = ""
)