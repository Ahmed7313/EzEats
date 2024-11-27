package com.easyEats.ezeats.ui.main.home.acive_order.order_types.delivery.delivery_states

import android.annotation.SuppressLint
import com.easyEats.ezeats.MyApplication
import com.easyEats.ezeats.R
import com.easyEats.ezeats.databinding.FragmentHomeScreenBinding
import com.easyEats.ezeats.ui.main.home.acive_order.order_types.IOrderStatus

class PendingDeliveryOrder : IOrderStatus {
    @SuppressLint("UseCompatLoadingForDrawables")
    override fun updateUI(binding: FragmentHomeScreenBinding) {
        binding.tvDeliveryPending.setTextColor(MyApplication.baseApplication.getColor(R.color.main_color))
        binding.ivDeliveryPending.setBackgroundDrawable(MyApplication.baseApplication.getDrawable(R.drawable.active_order_background))
        binding.ivDeliveryPending.setImageResource(R.drawable.active_pending_icon)
    }
}