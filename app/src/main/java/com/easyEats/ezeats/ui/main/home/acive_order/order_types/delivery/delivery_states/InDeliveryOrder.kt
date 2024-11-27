package com.easyEats.ezeats.ui.main.home.acive_order.order_types.delivery.delivery_states

import com.easyEats.ezeats.MyApplication
import com.easyEats.ezeats.R
import com.easyEats.ezeats.databinding.FragmentHomeScreenBinding
import com.easyEats.ezeats.ui.main.home.acive_order.order_types.IOrderStatus

class InDeliveryOrder :IOrderStatus{
    override fun updateUI(binding: FragmentHomeScreenBinding) {
        binding.progressBar5.progress = 100
        binding.tvInDelivery.setTextColor(MyApplication.baseApplication.getColor(R.color.main_color))
        binding.ivInDelivery.setBackgroundDrawable(MyApplication.baseApplication.getDrawable(R.drawable.active_order_background))
        binding.ivInDelivery.setImageResource(R.drawable.active_in_delivery_icon)
    }
}