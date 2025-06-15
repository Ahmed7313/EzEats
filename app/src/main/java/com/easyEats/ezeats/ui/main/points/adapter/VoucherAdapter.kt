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
/**
 * Adapter for displaying a list of vouchers (Promocode items) in a RecyclerView.
 * This adapter uses ListAdapter to efficiently handle updates with DiffUtil.
 */
class VoucherAdapter :
    ListAdapter<Promocode, VoucherAdapter.VoucherViewHolder>(VoucherDiffUtil()) {

    /**
     * ViewHolder class for managing individual voucher items in the RecyclerView.
     *
     * @property binding The binding object for accessing views in the item layout.
     */
    class VoucherViewHolder(val binding: ItemVoucherLoyaltyPointsBinding) :
        RecyclerView.ViewHolder(binding.root) {

        /**
         * Binds a Promocode object to the item layout.
         *
         * @param promoCode The Promocode object to bind.
         */
        fun bind(promoCode: Promocode) {
            binding.voucher = promoCode
        }

        /**
         * Sets up a click listener on the voucher name to copy the promo code to the clipboard.
         *
         * @param voucher The VoucherResponseItem containing the promo code to copy.
         */
        private fun copyVoucher(voucher: VoucherResponseItem) {
            binding.tvName.setOnClickListener {
                // Create a clip with the promo code text
                val clip = ClipData.newPlainText("Copied Text", voucher.promoCode)
                // Copy the text to the clipboard
                (it.context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager).setPrimaryClip(clip)
                // Show a toast message confirming the copy
                Toast.makeText(it.context, "Voucher copied to clipboard", Toast.LENGTH_SHORT).show()
            }
        }
    }

    /**
     * DiffUtil implementation for comparing Promocode items.
     */
    class VoucherDiffUtil : DiffUtil.ItemCallback<Promocode>() {
        /**
         * Checks if two Promocode items are the same based on their references.
         *
         * @param oldItem The old Promocode item.
         * @param newItem The new Promocode item.
         * @return True if the two items are the same, false otherwise.
         */
        override fun areItemsTheSame(
            oldItem: Promocode,
            newItem: Promocode
        ): Boolean {
            return oldItem === newItem
        }

        /**
         * Checks if the contents of two Promocode items are the same.
         *
         * @param oldItem The old Promocode item.
         * @param newItem The new Promocode item.
         * @return True if the contents of the two items are the same, false otherwise.
         */
        override fun areContentsTheSame(
            oldItem: Promocode,
            newItem: Promocode
        ): Boolean {
            return oldItem == newItem
        }
    }

    /**
     * Creates a new ViewHolder instance for a voucher item.
     *
     * @param parent The parent ViewGroup into which the new view will be added.
     * @param viewType The view type of the new view.
     * @return A new instance of VoucherViewHolder.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VoucherViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemVoucherLoyaltyPointsBinding.inflate(layoutInflater, parent, false)
        return VoucherViewHolder(binding)
    }

    /**
     * Binds the data of a Promocode item to a ViewHolder.
     *
     * @param holder The ViewHolder to bind the data to.
     * @param position The position of the Promocode item in the list.
     */
    override fun onBindViewHolder(holder: VoucherViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}
