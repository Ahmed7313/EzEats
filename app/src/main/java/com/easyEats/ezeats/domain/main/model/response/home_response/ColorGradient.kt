package com.easyEats.ezeats.domain.main.model.response.home_response


import com.google.gson.annotations.SerializedName
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.Serializable

@OptIn(InternalSerializationApi::class)
@Serializable
data class ColorGradient(
    val color1: String?,
    val color2: String?
)