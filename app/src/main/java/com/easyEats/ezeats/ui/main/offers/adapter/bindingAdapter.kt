package com.easyEats.ezeats.ui.main.offers.adapter

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import coil.load
import com.easyEats.ezeats.R


@BindingAdapter("setCategories")
fun setCategories(tv: TextView, categories: List<String?>?) {
    if (categories.isNullOrEmpty()) {
        tv.text = ""
        return
    }
    // Create a new list excluding null values
    val nonNullCategories = categories.filterNotNull()

    // Join the first 3 categories with a comma, and add "etc" if there are more than 3 items
    val formattedCategories = if (nonNullCategories.size > 3) {
        nonNullCategories.take(3).joinToString(", ") + ", etc"
    } else {
        nonNullCategories.joinToString(", ")
    }

    // Set the formatted text
    tv.text = formattedCategories
}
