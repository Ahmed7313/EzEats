package com.easyEats.ezeats.ui.main.home.acive_order.order_types.pickup.pickup_states

import com.easyEats.ezeats.MyApplication
import com.easyEats.ezeats.R
import com.easyEats.ezeats.databinding.FragmentHomeScreenBinding
import com.easyEats.ezeats.ui.main.home.acive_order.order_types.IOrderStatus

class PendingPickupOrder : IOrderStatus {
    override fun updateUI(binding: FragmentHomeScreenBinding) {
        binding.tvPendin.setTextColor(MyApplication.baseApplication.getColor(R.color.main_color))
        binding.ivPending.setBackgroundDrawable(MyApplication.baseApplication.getDrawable(R.drawable.active_order_background))
        binding.ivPending.setImageResource(R.drawable.active_pending_icon)
    }
}