package com.easyEats.ezeats.ui.main.home.acive_order.active_order

import com.easyEats.ezeats.ui.main.home.acive_order.order_types.IOrderType

interface IActiveOrder {
    fun getOrderType(isPickup: Boolean?): IOrderType
}