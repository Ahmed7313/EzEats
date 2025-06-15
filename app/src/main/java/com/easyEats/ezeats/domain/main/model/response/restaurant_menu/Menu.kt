package com.easyEats.ezeats.domain.main.model.response.restaurant_menu


import com.google.gson.annotations.SerializedName

data class Menu(
    @SerializedName("id")
    val id: String,
    @SerializedName("isHidden")
    val isHidden: Boolean?,
    @SerializedName("menu")
    val subMenus: List<SubMenu>?,
    @SerializedName("name")
    val name: String?
)