package com.easyEats.ezeats.domain.main.model.response.restaurant_menu


import com.google.gson.annotations.SerializedName

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
)