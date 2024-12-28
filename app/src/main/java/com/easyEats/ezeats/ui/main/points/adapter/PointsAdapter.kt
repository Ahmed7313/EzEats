package com.easyEats.ezeats.ui.main.points.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.easyEats.ezeats.databinding.PointsItemBinding
import com.easyEats.ezeats.domain.main.model.response.loyality_center.LoyaltyPoint
import javax.inject.Inject

class PointsAdapter @Inject constructor() :
    ListAdapter<LoyaltyPoint, PointsAdapter.PointsViewHolder>(PointDiffUtil()) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PointsViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = PointsItemBinding.inflate(inflater, parent, false)
        return PointsViewHolder(view)
    }

    override fun onBindViewHolder(holder: PointsViewHolder, position: Int) {
        val point = getItem(position)
        holder.binding.restaurantPoint = point
        holder.binding.executePendingBindings()
    }

    inner class PointsViewHolder(val binding: PointsItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    class PointDiffUtil : DiffUtil.ItemCallback<LoyaltyPoint>() {
        override fun areItemsTheSame(oldItem: LoyaltyPoint, newItem: LoyaltyPoint): Boolean {
            return oldItem.resturantId == newItem.resturantId
        }

        override fun areContentsTheSame(
            oldItem: LoyaltyPoint,
            newItem: LoyaltyPoint
        ): Boolean {
            return oldItem == newItem
        }

    }

}