package com.easyEats.ezeats.ui.main.home.acive_order.order_types.pickup

import com.easyEats.ezeats.ui.main.home.acive_order.order_types.IOrderStatus
import com.easyEats.ezeats.ui.main.home.acive_order.order_types.IOrderType
import com.easyEats.ezeats.ui.main.home.acive_order.order_types.pickup.pickup_states.PendingPickupOrder
import com.easyEats.ezeats.ui.main.home.acive_order.order_types.pickup.pickup_states.PickupOrder
import com.easyEats.ezeats.ui.main.home.acive_order.order_types.pickup.pickup_states.PreparingPickupOrder
import java.util.Locale

class PickupOrder : IOrderType {
    override fun getOrderState(state: String?): IOrderStatus {
        return when (state?.toLowerCase(Locale.getDefault())) {
            "pending" -> PendingPickupOrder()
            "preparing" -> PreparingPickupOrder()
            "progress" -> PreparingPickupOrder()
            "pickup" -> PickupOrder()
            "completed" -> PickupOrder()
            "ready for pick up" -> PickupOrder()
            else -> PendingPickupOrder()
        }
    }
}