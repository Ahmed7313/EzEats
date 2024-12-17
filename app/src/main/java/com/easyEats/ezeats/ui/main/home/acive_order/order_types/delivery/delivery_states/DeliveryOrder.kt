package com.easyEats.ezeats.ui.main.home.acive_order.order_types.delivery.delivery_states

import androidx.core.content.ContextCompat
import com.easyEats.ezeats.MyApplication
import com.easyEats.ezeats.R
import com.easyEats.ezeats.databinding.FragmentHomeScreenBinding
import com.easyEats.ezeats.ui.main.home.acive_order.order_types.IOrderStatus

class DeliveryOrder : IOrderStatus {
    override fun updateUI(binding: FragmentHomeScreenBinding) {
        binding.tvDeliveryPending.setTextColor(MyApplication.baseApplication.getColor(R.color.dark_gray))
        binding.ivDeliveryPending.setBackgroundDrawable(MyApplication.baseApplication.getDrawable(R.drawable.dis_active_order_background))
        binding.ivDeliveryPending.setImageResource(R.drawable.pending_icon)
        binding.progressBar4.progress = 0
        binding.tvDeliveryPreparing.setTextColor(MyApplication.baseApplication.getColor(R.color.dark_gray))
        binding.ivDeliveryPreparing.setBackgroundDrawable(MyApplication.baseApplication.getDrawable(R.drawable.dis_active_order_background))
        binding.ivDeliveryPreparing.setImageResource(R.drawable.preparing_icon)
        binding.progressBar5.progress = 0
        binding.tvInDelivery.setTextColor(MyApplication.baseApplication.getColor(R.color.dark_gray))
        binding.ivInDelivery.setBackgroundDrawable(MyApplication.baseApplication.getDrawable(R.drawable.dis_active_order_background))
        binding.ivInDelivery.setImageResource(R.drawable.dis_active_in_delivery_icon)
        binding.progressBar6.progress = 100
        binding.tvDelivered.setTextColor(MyApplication.baseApplication.getColor(R.color.main_color))
        binding.ivDelivered.setBackgroundDrawable(MyApplication.baseApplication.getDrawable(R.drawable.active_order_background))
        binding.ivDelivered.setImageResource(R.drawable.active_delivered_icon)
    }
}