package com.easyEats.ezeats.ui.main.location.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.easyEats.ezeats.databinding.ItemLocationBinding
import com.easyEats.ezeats.domain.main.model.response.location_response.Location
import javax.inject.Inject

class LocationAdapter @Inject constructor(
    val locationClickListener: OnLocationListener
) :
    ListAdapter<Location, LocationAdapter.LocationViewHolder>(LocationDiffCallback()) {

    interface OnLocationListener {
        fun onLocationClicked(location: Location)
    }

    inner class LocationViewHolder(val binding: ItemLocationBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LocationViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemLocationBinding.inflate(inflater, parent, false)
        return LocationViewHolder(binding)
    }

    override fun onBindViewHolder(holder: LocationViewHolder, position: Int) {
        val location = getItem(position)
        holder.binding.location = location
        holder.binding.locationClicked = locationClickListener
    }

}

class LocationDiffCallback : DiffUtil.ItemCallback<Location>() {
    override fun areItemsTheSame(oldItem: Location, newItem: Location): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Location, newItem: Location): Boolean {
        return oldItem == newItem
    }
}