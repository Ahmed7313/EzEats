package com.easyEats.ezeats.domain.main.model.response.restaurant_menu


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Choice(
    @SerializedName("id")
    val id: String?,
    @SerializedName("isHidden")
    val isHidden: Boolean?,
    @SerializedName("name")
    val name: String?,
    @SerializedName("price")
    val price: Double?
) : Parcelable