package com.easyEats.ezeats.ui.main.home.acive_order.order_types

interface IOrderType {
    fun getOrderState(state: String?): IOrderStatus
}