package com.easyEats.ezeats.ui.main.home.acive_order.order_types.pickup.pickup_states

import com.easyEats.ezeats.MyApplication
import com.easyEats.ezeats.R
import com.easyEats.ezeats.databinding.FragmentHomeScreenBinding
import com.easyEats.ezeats.ui.main.home.acive_order.order_types.IOrderStatus

class PreparingPickupOrder : IOrderStatus {
    override fun updateUI(binding: FragmentHomeScreenBinding) {
        binding.tvPendin.setTextColor(MyApplication.baseApplication.getColor(R.color.light_gray))
        binding.ivPending.setBackgroundDrawable(MyApplication.baseApplication.getDrawable(R.drawable.dis_active_order_background))
        binding.ivPending.setImageResource(R.drawable.pending_icon)
        binding.progressBar2.progress = 0
        binding.tvPreparing.setTextColor(MyApplication.baseApplication.getColor(R.color.main_color))
        binding.ivPreparing.setBackgroundDrawable(MyApplication.baseApplication.getDrawable(R.drawable.active_order_background))
        binding.ivPreparing.setImageResource(R.drawable.active_preparing_icon)

    }
}