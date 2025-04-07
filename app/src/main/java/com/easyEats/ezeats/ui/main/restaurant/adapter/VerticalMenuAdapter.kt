package com.easyEats.ezeats.ui.main.restaurant.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.easyEats.ezeats.R
import com.easyEats.ezeats.domain.main.model.response.restaurant_menu.SubMenu

class VerticalMenuAdapter(
    val items: List<VerticalItem>
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

    override fun getItemCount(): Int = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_HEADER -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.sub_menu_header, parent, false)
                HeaderViewHolder(view)
            }
            TYPE_SUB_ITEM -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.menu_item, parent, false)
                SubItemViewHolder(view)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = items[position]) {
            is VerticalItem.Header -> (holder as HeaderViewHolder).bind(item)
            is VerticalItem.SubItem -> (holder as SubItemViewHolder).bind(item.subMenu)
        }
    }

    inner class HeaderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvHeader: TextView = itemView.findViewById(R.id.tv_sub_menu_header)

        fun bind(item: VerticalItem.Header) {
            tvHeader.text = item.name
        }
    }

    inner class SubItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val title: TextView = itemView.findViewById(R.id.tv_title_menu_item)
        private val description: TextView = itemView.findViewById(R.id.tv_description_menu_item)
        private val price: TextView = itemView.findViewById(R.id.tv_price_menu_item)
        private val image: ImageView = itemView.findViewById(R.id.iv_menu_item)

        fun bind(subMenu: SubMenu) {
            title.text = subMenu.name ?: ""
            description.text = subMenu.description ?: ""
            price.text = "EGP ${subMenu.price ?: "0"}"
            image.load(subMenu.coverPhoto?.url)
            // If using image loading library like Glide or Coil:
            // Glide.with(image.context).load(subMenu.imageUrl).into(image)
        }
    }
}
