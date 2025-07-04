package com.easyEats.ezeats.domain.main.model.request.cart

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.Serializable

@OptIn(InternalSerializationApi::class)
@Serializable
data class ItemCheckoutModel(
    @SerializedName("itemId")
    var itemId: String? = "",
    @SerializedName("notes")
    var notes: String? = "",
    @SerializedName("quantity")
    var quantity: Int? = 0,
    @SerializedName("suboptionsIds")
    var suboptionsIds: List<SuboptionsId?>? = listOf()
)