package com.easyEats.ezeats.ui.main.home.acive_order.order_types.delivery

import com.easyEats.ezeats.ui.main.home.acive_order.order_types.delivery.delivery_states.PendingDeliveryOrder
import com.easyEats.ezeats.ui.main.home.acive_order.order_types.IOrderStatus
import com.easyEats.ezeats.ui.main.home.acive_order.order_types.IOrderType
import com.easyEats.ezeats.ui.main.home.acive_order.order_types.delivery.delivery_states.DeliveryOrder
import com.easyEats.ezeats.ui.main.home.acive_order.order_types.delivery.delivery_states.InDeliveryOrder
import com.easyEats.ezeats.ui.main.home.acive_order.order_types.delivery.delivery_states.PreparingDeliveryOrder
import java.util.Locale


class DeliveryOrder : IOrderType {
    override fun getOrderState(state: String?): IOrderStatus {
        return when (state?.toLowerCase(Locale.getDefault())) {
            "pending" -> PendingDeliveryOrder()
            "preparing" -> PreparingDeliveryOrder()
            "progress" -> PreparingDeliveryOrder()
            "in-delivery" -> InDeliveryOrder()
            "out for delivery" -> InDeliveryOrder()
            "outfordelivery" -> InDeliveryOrder()
            "delivered" -> DeliveryOrder()
            "ready for pick up" -> DeliveryOrder()
            "ready for pickup" -> DeliveryOrder()
            else -> PendingDeliveryOrder()
        }
    }
}