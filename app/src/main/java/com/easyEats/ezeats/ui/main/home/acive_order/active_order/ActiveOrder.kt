package com.easyEats.ezeats.ui.main.home.acive_order.active_order

import com.easyEats.ezeats.ui.main.home.acive_order.order_types.delivery.DeliveryOrder
import com.easyEats.ezeats.ui.main.home.acive_order.order_types.IOrderType
import com.easyEats.ezeats.ui.main.home.acive_order.order_types.pickup.PickupOrder

class ActiveOrder : IActiveOrder {
    override fun getOrderType(isPickup: Boolean?) =
        if (isPickup == true) PickupOrder() else DeliveryOrder()

}