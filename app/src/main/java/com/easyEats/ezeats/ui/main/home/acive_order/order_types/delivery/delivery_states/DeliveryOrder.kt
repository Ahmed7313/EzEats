package com.easyEats.ezeats.ui.main.home.acive_order.order_types.delivery.delivery_states

import com.easyEats.ezeats.MyApplication
import com.easyEats.ezeats.R
import com.easyEats.ezeats.databinding.FragmentHomeScreenBinding
import com.easyEats.ezeats.ui.main.home.acive_order.order_types.IOrderStatus

class DeliveryOrder : IOrderStatus {
    override fun updateUI(binding: FragmentHomeScreenBinding) {
        binding.progressBar6.progress = 100
        binding.tvDelivered.setTextColor(MyApplication.baseApplication.getColor(R.color.main_color))
        binding.ivDelivered.setBackgroundDrawable(MyApplication.baseApplication.getDrawable(R.drawable.active_order_background))
        binding.ivDelivered.setImageResource(R.drawable.active_delivered_icon)
    }
}