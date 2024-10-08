package com.easyEats.ezeats.ui.main.voucher.adapter

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.easyEats.ezeats.R
import com.easyEats.ezeats.databinding.FragmentVoucherBinding
import com.easyEats.ezeats.databinding.ItemVoucherBinding
import com.easyEats.ezeats.domain.main.model.response.voucher.VoucherResponseItem

class VoucherAdapter :
    ListAdapter<VoucherResponseItem, VoucherAdapter.VoucherViewHolder>(VoucherDiffUtil()) {

    class VoucherViewHolder(val binding: ItemVoucherBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(voucher: VoucherResponseItem) {
            binding.voucher = voucher
            copyVoucher(voucher)
        }

        private fun copyVoucher(voucher: VoucherResponseItem) {
            binding.tvName.setOnClickListener {
                val clip = ClipData.newPlainText("Copied Text", voucher.promoCode)
                (it.context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager).setPrimaryClip(clip)
                Toast.makeText(it.context, "voucher copied to clipboard", Toast.LENGTH_SHORT).show()
            }
        }
    }

    class VoucherDiffUtil : DiffUtil.ItemCallback<VoucherResponseItem>() {
        override fun areItemsTheSame(
            oldItem: VoucherResponseItem,
            newItem: VoucherResponseItem
        ): Boolean {
            return oldItem.promocodeId == newItem.promocodeId
        }

        override fun areContentsTheSame(
            oldItem: VoucherResponseItem,
            newItem: VoucherResponseItem
        ): Boolean {
            return oldItem == newItem
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VoucherViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemVoucherBinding.inflate(layoutInflater, parent, false)
        return VoucherViewHolder(binding)
    }

    override fun onBindViewHolder(holder: VoucherViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}