package com.easyEats.ezeats.domain.main.model.response.restaurant_menu


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

@Parcelize
data class SubMenu(
    @SerializedName("available")
    val available: Boolean?,
    @SerializedName("coverPhoto")
    val coverPhoto: CoverPhoto?,
    @SerializedName("days")
    val days: @RawValue List<Any>?,
    @SerializedName("defaultService")
    val defaultService: Int?,
    @SerializedName("description")
    val description: String?,
    @SerializedName("endTime")
    val endTime: String?,
    @SerializedName("id")
    val id: String?,
    @SerializedName("isHidden")
    val isHidden: Boolean?,
    @SerializedName("isNew")
    val isNew: Boolean?,
    @SerializedName("name")
    val name: String?,
    @SerializedName("prepTime")
    val prepTime: Int?,
    @SerializedName("price")
    val price: Int?,
    @SerializedName("startTime")
    val startTime: String?,
    @SerializedName("subOptions")
    val subOptions: List<SubOption?>?,
    @SerializedName("type")
    val type: String?
) : Parcelable