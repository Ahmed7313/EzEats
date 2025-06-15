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
import com.easyEats.ezeats.databinding.SpinWheelVoucherItemBinding
import com.easyEats.ezeats.domain.main.model.response.loyality_center.SpinWheelVoucher
import com.easyEats.ezeats.domain.main.model.response.voucher.VoucherResponseItem


class SpinWheelAdapter :
    ListAdapter<SpinWheelVoucher, SpinWheelAdapter.VoucherViewHolder>(VoucherDiffUtil()) {

    class VoucherViewHolder(val binding: SpinWheelVoucherItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(voucher: SpinWheelVoucher) {
            binding.voucher = voucher
        }

        private fun copyVoucher(voucher: VoucherResponseItem) {
            binding.tvName.setOnClickListener {
                val clip = ClipData.newPlainText("Copied Text", voucher.promoCode)
                (it.context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager).setPrimaryClip(
                    clip
                )
                Toast.makeText(it.context, "voucher copied to clipboard", Toast.LENGTH_SHORT).show()
            }
        }
    }

    class VoucherDiffUtil : DiffUtil.ItemCallback<SpinWheelVoucher>() {
        override fun areItemsTheSame(
            oldItem: SpinWheelVoucher,
            newItem: SpinWheelVoucher
        ): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(
            oldItem: SpinWheelVoucher,
            newItem: SpinWheelVoucher
        ): Boolean {
            return oldItem == newItem
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VoucherViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = SpinWheelVoucherItemBinding.inflate(layoutInflater, parent, false)
        return VoucherViewHolder(binding)
    }

    override fun onBindViewHolder(holder: VoucherViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}