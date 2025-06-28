package com.easyEats.ezeats.ui.main.restaurant.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.easyEats.ezeats.R
import com.easyEats.ezeats.databinding.MenuItemBinding
import com.easyEats.ezeats.databinding.SubMenuHeaderBinding
import com.easyEats.ezeats.domain.main.model.response.restaurant_menu.Menu
import com.easyEats.ezeats.domain.main.model.response.restaurant_menu.SubMenu

class VerticalMenuAdapter(
    val items: List<VerticalItem>,
    private val onMenuItemClicked: (SubMenu) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val TYPE_HEADER = 0
        private const val TYPE_SUB_ITEM = 1
    }

    override fun getItemViewType(position: Int): Int {
        return when (items[position]) {
            is VerticalItem.Header -> TYPE_HEADER
            is VerticalItem.SubItem -> TYPE_SUB_ITEM
        }
    }

    fun clear() {
        if (items.isNotEmpty()) {
            (items as? MutableList)?.clear()
            notifyDataSetChanged()
        }
    }

    override fun getItemCount(): Int = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_HEADER -> {
                val binding = DataBindingUtil.inflate<SubMenuHeaderBinding>(
                    LayoutInflater.from(parent.context),
                    R.layout.sub_menu_header,
                    parent,
                    false
                )
                HeaderViewHolder(binding)
            }

            TYPE_SUB_ITEM -> {
                val binding = DataBindingUtil.inflate<MenuItemBinding>(
                    LayoutInflater.from(parent.context),
                    R.layout.menu_item,
                    parent,
                    false
                )
                SubItemViewHolder(binding)
            }

            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = items[position]) {
            is VerticalItem.Header -> (holder as HeaderViewHolder).bind(item)
            is VerticalItem.SubItem -> {
                if (item.subMenu.isHidden == false)
                    (holder as SubItemViewHolder).bind(item.subMenu)
            }
        }
    }

    inner class HeaderViewHolder(private val binding: SubMenuHeaderBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: VerticalItem.Header) {

            binding.item = item
            binding.executePendingBindings() // Executes any pending bindings immediately
        }
    }

    inner class SubItemViewHolder(private val binding: MenuItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(subMenu: SubMenu) {
            binding.subMenu = subMenu
            binding.executePendingBindings()
            binding.root.setOnClickListener {
                onMenuItemClicked(subMenu)
            }
        }
    }
}
