package com.easyEats.ezeats.domain.main.model.response.restaurant_menu


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class SubOption(
    @SerializedName("choices")
    val choices: List<Choice?>?,
    @SerializedName("description")
    val description: String?,
    @SerializedName("id")
    val id: String?,
    @SerializedName("max")
    val max: String?,
    @SerializedName("min")
    val min: Int?,
    @SerializedName("name")
    val name: String?,
    @SerializedName("required")
    val required: Boolean?,
    @SerializedName("type")
    val type: String?
): Parcelable