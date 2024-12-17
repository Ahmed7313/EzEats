package com.easyEats.ezeats.ui.main.home.acive_order.order_types.delivery.delivery_states

import com.easyEats.ezeats.MyApplication
import com.easyEats.ezeats.R
import com.easyEats.ezeats.databinding.FragmentHomeScreenBinding
import com.easyEats.ezeats.ui.main.home.acive_order.order_types.IOrderStatus

class PreparingDeliveryOrder : IOrderStatus {
    override fun updateUI(binding: FragmentHomeScreenBinding) {
        binding.tvDeliveryPending.setTextColor(MyApplication.baseApplication.getColor(R.color.dark_gray))
        binding.ivDeliveryPending.setBackgroundDrawable(MyApplication.baseApplication.getDrawable(R.drawable.dis_active_order_background))
        binding.ivDeliveryPending.setImageResource(R.drawable.pending_icon)
        binding.progressBar4.progress = 100
        binding.tvDeliveryPreparing.setTextColor(MyApplication.baseApplication.getColor(R.color.main_color))
        binding.ivDeliveryPreparing.setBackgroundDrawable(MyApplication.baseApplication.getDrawable(R.drawable.active_order_background))
        binding.ivDeliveryPreparing.setImageResource(R.drawable.active_preparing_icon)

    }
}