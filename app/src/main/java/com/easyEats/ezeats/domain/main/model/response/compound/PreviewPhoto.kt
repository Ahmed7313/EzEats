package com.easyEats.ezeats.domain.main.model.response.compound


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.Serializable

@OptIn(InternalSerializationApi::class)
@Serializable
@Parcelize
data class PreviewPhoto(
    @SerializedName("name")
    var name: String? = "",
    @SerializedName("__type")
    var type: String? = "",
    @SerializedName("url")
    var url: String? = ""
) : Parcelable