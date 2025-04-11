package com.easyEats.ezeats.domain.main.model.response.restaurant_menu


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class CoverPhoto(
    @SerializedName("name")
    val name: String?,
    @SerializedName("__type")
    val type: String?,
    @SerializedName("url")
    val url: String?
) : Parcelable