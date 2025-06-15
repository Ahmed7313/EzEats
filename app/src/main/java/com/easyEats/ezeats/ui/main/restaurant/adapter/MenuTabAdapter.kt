package com.easyEats.ezeats.ui.main.restaurant.adapter

import android.view.LayoutInflater
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.easyEats.ezeats.R
import com.easyEats.ezeats.databinding.MenuHeaderBinding
import com.easyEats.ezeats.domain.main.model.response.restaurant_menu.Menu

/**
 * Adapter for the menu tab list. This adapter binds the list of menus to the RecyclerView.
 * It also handles the click event to select a menu tab.
 */
class MenuTabAdapter(
    val menus: List<Menu>?,
    var selectedId: String?,
    private val onClick: (String) -> Unit
) : RecyclerView.Adapter<MenuTabAdapter.MenuTabViewHolder>() {

    // Expose the items list to allow access by the Fragment
    val items: List<Menu>
        get() = menus ?: emptyList()

    /**
     * ViewHolder to hold the menu tab's view elements
     */
    inner class MenuTabViewHolder(val binding: MenuHeaderBinding) :
        RecyclerView.ViewHolder(binding.root)

    /**
     * Inflates the menu header layout for each menu tab
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        MenuTabViewHolder(
            MenuHeaderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )

    /**
     * Returns the number of items in the list
     */
    override fun getItemCount(): Int = menus?.size ?: 0

    /**
     * Binds data to the menu tab and updates the view with selection state
     */
    override fun onBindViewHolder(holder: MenuTabViewHolder, position: Int) {
        val menu = menus?.get(position) ?: return

        with(holder.binding) {
            // Set the menu title
            menuTitle.text = menu.name

            // Highlight selected menu and show/hide underline
            if (menu.id == selectedId) {
                menuTitle.setTextColor(ContextCompat.getColor(holder.binding.root.context, R.color.main_color))
                underline.visibility = VISIBLE
            } else {
                menuTitle.setTextColor(ContextCompat.getColor(holder.binding.root.context, R.color.black))
                underline.visibility = GONE
            }

            // Handle click event for menu tab selection
            holder.binding.root.setOnClickListener {
                // Only update if the selected menu is different
                if (menu.id != selectedId) {
                    selectedId = menu.id
                    onClick(menu.id)
                    notifyDataSetChanged()
                }
            }
        }
    }

    /**
     * Updates the selected menu tab's state and refreshes the view
     */
    fun updateSelection(id: String?) {
        if (id != selectedId) {
            selectedId = id
            notifyDataSetChanged()
        }
    }
}
