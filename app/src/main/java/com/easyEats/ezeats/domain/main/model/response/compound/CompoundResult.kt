package com.easyEats.ezeats.domain.main.model.response.compound


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.Serializable

@OptIn(InternalSerializationApi::class)
@Serializable
@Parcelize
data class CompoundResult(
    @SerializedName("className")
    var className: String? = "",
    @SerializedName("coverPhoto")
    var coverPhoto: CoverPhoto? = CoverPhoto(),
    @SerializedName("createdAt")
    var createdAt: String? = "",
    @SerializedName("formattedName")
    var formattedName: String? = "",
    @SerializedName("isHidden")
    var isHidden: Boolean? = false,
    @SerializedName("name")
    var name: String? = "",
    @SerializedName("objectId")
    var objectId: String? = "",
//    @SerializedName("officesLocations")
//    var officesLocations: List<Any?>? = listOf(),
    @SerializedName("pickUpLocations")
    var pickUpLocations: List<String?>? = listOf(),
    @SerializedName("previewPhoto")
    var previewPhoto: PreviewPhoto? = PreviewPhoto(),
    @SerializedName("__type")
    var type: String? = "",
    @SerializedName("updatedAt")
    var updatedAt: String? = ""
) : Parcelable