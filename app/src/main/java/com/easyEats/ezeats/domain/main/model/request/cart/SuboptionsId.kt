package com.easyEats.ezeats.domain.main.model.request.cart

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.Serializable

@OptIn(InternalSerializationApi::class)
@Serializable
data class SuboptionsId(
    @SerializedName("choicesIds")
    var choicesIds: List<String?>? = listOf(),
    @SerializedName("suboptionId")
    var suboptionId: String? = ""
)