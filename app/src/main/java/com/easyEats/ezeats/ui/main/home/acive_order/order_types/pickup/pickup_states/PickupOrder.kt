package com.easyEats.ezeats.ui.main.home.acive_order.order_types.pickup.pickup_states

import com.easyEats.ezeats.MyApplication
import com.easyEats.ezeats.R
import com.easyEats.ezeats.databinding.FragmentHomeScreenBinding
import com.easyEats.ezeats.ui.main.home.acive_order.order_types.IOrderStatus

class PickupOrder : IOrderStatus {
    override fun updateUI(binding: FragmentHomeScreenBinding) {
        binding.progressBar3.progress = 100
        binding.tvPickup.setTextColor(MyApplication.baseApplication.getColor(R.color.main_color))
        binding.ivPickup.setBackgroundDrawable(MyApplication.baseApplication.getDrawable(R.drawable.active_order_background))
        binding.ivPickup.setImageResource(R.drawable.active_pickup_icon)
    }
}