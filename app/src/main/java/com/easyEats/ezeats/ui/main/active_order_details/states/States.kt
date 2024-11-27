package com.easyEats.ezeats.ui.main.active_order_details.states

import com.easyEats.ezeats.R
import java.util.Locale

enum class States(val imageResId: Int) {
    PLACED(R.drawable.active_order_placed),
    BEING_PREPARED(R.drawable.active_being_prepared),
    OUT_FOR_DELIVERY(R.drawable.active_out_for_delivery),
    DELIVERED(R.drawable.active_delivered),
    READY_FOR_PICKUP(R.drawable.active_delivered);

    companion object {
        /**
         * Maps a given state string to a corresponding States enum value.
         * Defaults to PLACED if the state is null or unrecognized.
         *
         * @param state The string representation of the state (e.g., "placed", "being prepared").
         * @return The matching States enum constant, or PLACED if no match is found.
         */
        fun fromState(state: String?): States {
            return when (state?.lowercase(Locale.getDefault())) {
                "placed" -> PLACED
                "being prepared" -> BEING_PREPARED
                "out for delivery" -> OUT_FOR_DELIVERY
                "delivered" -> DELIVERED
                "ready for pick up" -> READY_FOR_PICKUP
                else -> PLACED
            }
        }
    }
}
