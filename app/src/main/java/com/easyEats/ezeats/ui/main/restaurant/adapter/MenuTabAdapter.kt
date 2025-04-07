package com.easyEats.ezeats.ui.main.restaurant.adapter

import android.view.LayoutInflater
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.easyEats.ezeats.MyApplication.Companion.baseApplication
import com.easyEats.ezeats.databinding.MenuHeaderBinding
import com.easyEats.ezeats.domain.main.model.response.restaurant_menu.Menu

class MenuTabAdapter(
    private val menus: List<Menu>?,
    private var selectedId: String?,
    private val onClick: (String) -> Unit
) : RecyclerView.Adapter<MenuTabAdapter.MenuTabViewHolder>() {

    inner class MenuTabViewHolder(val binding: MenuHeaderBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        MenuTabViewHolder(
            MenuHeaderBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun getItemCount(): Int = menus?.size!!

    override fun onBindViewHolder(holder: MenuTabViewHolder, position: Int) {
        val menu = menus?.get(position)
        with(holder.binding) {
            menuTitle.text = menu?.name
            if (menu?.id == selectedId) {
                menuTitle.setTextColor(baseApplication.getColor(com.easyEats.ezeats.R.color.main_color))
                underline.visibility = VISIBLE
            } else {
                menuTitle.setTextColor(baseApplication.getColor(com.easyEats.ezeats.R.color.black))
                underline.visibility = GONE
            }

            root.setOnClickListener {
                selectedId = menu?.id
                notifyDataSetChanged()
                onClick(menu!!.id)
            }
        }
    }

    fun updateSelection(id: String?) {
        selectedId = id
        notifyDataSetChanged()
    }
}
