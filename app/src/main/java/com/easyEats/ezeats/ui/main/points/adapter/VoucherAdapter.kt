package com.easyEats.ezeats.ui.main.points.adapter

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.easyEats.ezeats.databinding.ItemVoucherBinding
import com.easyEats.ezeats.databinding.ItemVoucherLoyaltyPointsBinding
import com.easyEats.ezeats.domain.main.model.response.loyality_center.Promocode
import com.easyEats.ezeats.domain.main.model.response.voucher.VoucherResponseItem


class VoucherAdapter :
    ListAdapter<Promocode, VoucherAdapter.VoucherViewHolder>(VoucherDiffUtil()) {

    class VoucherViewHolder(val binding: ItemVoucherLoyaltyPointsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(promoCode: Promocode) {
            binding.voucher = promoCode
        }

        private fun copyVoucher(voucher: VoucherResponseItem) {
            binding.tvName.setOnClickListener {
                val clip = ClipData.newPlainText("Copied Text", voucher.promoCode)
                (it.context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager).setPrimaryClip(clip)
                Toast.makeText(it.context, "voucher copied to clipboard", Toast.LENGTH_SHORT).show()
            }
        }
    }

    class VoucherDiffUtil : DiffUtil.ItemCallback<Promocode>() {
        override fun areItemsTheSame(
            oldItem: Promocode,
            newItem: Promocode
        ): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(
            oldItem: Promocode,
            newItem: Promocode
        ): Boolean {
            return oldItem == newItem
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VoucherViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemVoucherLoyaltyPointsBinding.inflate(layoutInflater, parent, false)
        return VoucherViewHolder(binding)
    }

    override fun onBindViewHolder(holder: VoucherViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}