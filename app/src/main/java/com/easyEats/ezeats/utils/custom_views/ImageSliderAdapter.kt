package com.easyEats.ezeats.utils.custom_views

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.easyEats.ezeats.databinding.ItemImageBinding
import com.easyEats.ezeats.domain.main.model.response.home_response.FeaturedOffer

class ImageSliderAdapter(private val images: List<FeaturedOffer>) :
    RecyclerView.Adapter<ImageSliderAdapter.ImageSliderViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageSliderViewHolder {
        val binding = ItemImageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ImageSliderViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ImageSliderViewHolder, position: Int) {
        val imageUrl = images[position]
        holder.binding.imageView.load(imageUrl.isFeaturedLogo)
    }

    override fun getItemCount(): Int = images.size

    inner class ImageSliderViewHolder(val binding: ItemImageBinding) :
        RecyclerView.ViewHolder(binding.root)
}


