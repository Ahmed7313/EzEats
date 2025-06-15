package com.easyEats.ezeats.ui.main.restaurant.adapter

import com.easyEats.ezeats.domain.main.model.response.restaurant_menu.Menu

fun flattenMenu(menuList: List<Menu>?): List<VerticalItem> {
    val items = mutableListOf<VerticalItem>()
    menuList?.forEach { menu ->
        val menuId = menu.id ?: return@forEach
        val title = menu.name ?: return@forEach
        items.add(VerticalItem.Header(menuId, title))
        menu.subMenus?.forEach { subMenu ->
            items.add(VerticalItem.SubItem(menuId, subMenu))
        }
    }
    return items
}
