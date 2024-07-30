package com.easyEats.ezeats.ui.auth.on_boarding.pager

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.easyEats.ezeats.databinding.SlideItemLayoutBinding
/**
 * IntroSliderAdapter is a RecyclerView adapter that handles the display of introductory slides.
 * It binds the data from a list of [IntroSlide] objects to the views in the RecyclerView.
 *
 * @property introSlides The list of introductory slides to display.
 */
class IntroSliderAdapter(private var introSlides: List<IntroSlide>)
    : RecyclerView.Adapter<IntroSliderAdapter.IntroSlideViewHolder>() {

    // Lambda function to pass the TextView for the title
    var onTextPassed: ((textView: TextView) -> Unit)? = null

    /**
     * Creates and returns a ViewHolder object, inflating the view from the provided parent context.
     *
     * @param parent The ViewGroup into which the new view will be added after it is bound to an adapter position.
     * @param viewType The view type of the new view.
     * @return A new IntroSlideViewHolder that holds a View of the given view type.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IntroSlideViewHolder {
        return IntroSlideViewHolder(
            SlideItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    /**
     * Updates the list of slides and notifies the adapter to refresh the views.
     *
     * @param newSlides The new list of introductory slides.
     */
    fun updateSlides(newSlides: List<IntroSlide>) {
        introSlides = newSlides
        notifyDataSetChanged()
    }

    /**
     * Returns the total number of slides.
     *
     * @return The size of the introSlides list.
     */
    override fun getItemCount(): Int {
        return introSlides.size
    }

    /**
     * Binds the data from an [IntroSlide] object to the corresponding view holder.
     *
     * @param holder The ViewHolder which should be updated to represent the contents of the item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    override fun onBindViewHolder(holder: IntroSlideViewHolder, position: Int) {
        holder.bind(introSlides[position])
    }

    /**
     * ViewHolder class that binds the data to the views for each slide item.
     *
     * @property binding The view binding object for the slide item layout.
     */
    inner class IntroSlideViewHolder(private val binding: SlideItemLayoutBinding) : RecyclerView.ViewHolder(binding.root) {

        /**
         * Binds the data from an [IntroSlide] object to the views.
         *
         * @param introSlide The IntroSlide object containing the data to bind.
         */
        fun bind(introSlide: IntroSlide) {
            binding.textTitle.text = introSlide.title
            binding.textDescription.text = introSlide.description
            binding.imageSlideIcon.setImageResource(introSlide.icon)
            onTextPassed?.invoke(binding.textTitle)
        }
    }
}
