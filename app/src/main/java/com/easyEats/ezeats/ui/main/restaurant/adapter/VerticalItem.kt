package com.easyEats.ezeats.ui.main.restaurant.adapter

import com.easyEats.ezeats.domain.main.model.response.restaurant_menu.SubMenu

sealed class VerticalItem {
    data class Header(val id: String, val name: String) : VerticalItem()
    data class SubItem(val parentId: String, val subMenu: SubMenu) : VerticalItem()
}
