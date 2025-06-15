package com.easyEats.ezeats.utils.custom_views

import android.content.Context
import android.os.Handler
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.viewpager2.widget.ViewPager2
import com.easyEats.ezeats.R
import com.easyEats.ezeats.databinding.CustomImageSliderBinding
import com.easyEats.ezeats.domain.main.model.response.home_response.FeaturedOffer

class CustomImageSlider @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val pageChangeCallback = object : ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            super.onPageSelected(position)
            setCurrentIndicator(position)
        }
    }

    private val binding: CustomImageSliderBinding
    private var currentImagePosition = 0
    private val handler = Handler()


    init {
        val inflater = LayoutInflater.from(context)
        binding = CustomImageSliderBinding.inflate(inflater, this, true)
    }

    private val slideRunnable = object : Runnable {
        override fun run() {
            val totalImages = binding.viewPager.adapter?.itemCount ?: 0
            currentImagePosition = (currentImagePosition + 1) % totalImages
            binding.viewPager.setCurrentItem(currentImagePosition, true)
            handler.postDelayed(this, 3000) // 3-second delay
        }
    }

    fun setImages(images: List<FeaturedOffer>) {
        // Stop any previous sliding Runnable
        handler.removeCallbacks(slideRunnable)

        // Set up the ViewPager adapter
        val adapter = ImageSliderAdapter(images)
        binding.viewPager.adapter = adapter

        // Set up the indicators
        setupIndicators(images.size)

        // Unregister and register the page change callback to avoid multiple registrations
        binding.viewPager.unregisterOnPageChangeCallback(pageChangeCallback)
        binding.viewPager.registerOnPageChangeCallback(pageChangeCallback)

        // Reset the current position
        currentImagePosition = 0

        // Start the sliding effect with a delay
        handler.postDelayed(slideRunnable, 3000)
    }


    private fun setupIndicators(count: Int) {
        // Clear existing indicators to avoid duplication
        binding.indicatorContainer.removeAllViews()

        for (i in 0 until count) {
            val indicator = ImageView(context)
            val params = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )

            val margin = resources.getDimensionPixelSize(R.dimen.indicator_margin)
            params.setMargins(margin, 0, margin, 0)

            indicator.layoutParams = params
            indicator.setImageDrawable(
                ContextCompat.getDrawable(
                    context,
                    R.drawable.inactive_indicator
                )
            )
            binding.indicatorContainer.addView(indicator)
        }

        setCurrentIndicator(0)
    }


    private fun setCurrentIndicator(position: Int) {
        val childCount = binding.indicatorContainer.childCount

        for (i in 0 until childCount) {
            val imageView = binding.indicatorContainer.getChildAt(i) as ImageView
            if (i == position) {
                imageView.setImageDrawable(
                    ContextCompat.getDrawable(context, R.drawable.active_indicator)
                )
            } else {
                imageView.setImageDrawable(
                    ContextCompat.getDrawable(context, R.drawable.inactive_indicator)
                )
            }
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        handler.removeCallbacks(slideRunnable) // Stop the sliding effect
        binding.viewPager.unregisterOnPageChangeCallback(pageChangeCallback)
    }
}
