package com.easyEats.ezeats.domain.main.model.request.cart.response


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.Serializable

@OptIn(InternalSerializationApi::class)
@Serializable
@Parcelize
data class TableOrder(
    @SerializedName("objectId")
    var objectId: String? = ""
) : Parcelable