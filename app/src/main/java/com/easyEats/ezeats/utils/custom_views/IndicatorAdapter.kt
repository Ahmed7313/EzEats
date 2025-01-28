package com.easyEats.ezeats.utils.custom_views

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.easyEats.ezeats.R

class IndicatorAdapter(private val size: Int, private var selectedPosition: Int = 0) :
    RecyclerView.Adapter<IndicatorAdapter.IndicatorViewHolder>() {

    inner class IndicatorViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        fun bind(isActive: Boolean) {
            view.setBackgroundResource(
                if (isActive) R.drawable.active_indicator else R.drawable.inactive_indicator
            )
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IndicatorViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_indicator, parent, false)
        return IndicatorViewHolder(view)
    }

    override fun onBindViewHolder(holder: IndicatorViewHolder, position: Int) {
        holder.bind(position == selectedPosition)
    }

    override fun getItemCount(): Int = size

    fun updateSelectedPosition(newPosition: Int) {
        val oldPosition = selectedPosition
        selectedPosition = newPosition
        notifyItemChanged(oldPosition)
        notifyItemChanged(newPosition)
    }
}
