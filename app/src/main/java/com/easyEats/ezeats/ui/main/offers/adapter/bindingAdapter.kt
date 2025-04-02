package com.easyEats.ezeats.ui.main.offers.adapter

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import coil.load
import com.easyEats.ezeats.R

/**
 * A custom BindingAdapter for setting a list of category names to a TextView.
 * It formats the list by joining the first 3 categories with a comma and adds "etc"
 * if there are more than 3 categories.
 */
@BindingAdapter("setCategories")
fun setCategories(tv: TextView, categories: List<String?>?) {
    // If the categories list is null or empty, set the TextView text to an empty string
    if (categories.isNullOrEmpty()) {
        tv.text = ""
        return
    }

    // Create a new list excluding null values from the categories list
    val nonNullCategories = categories.filterNotNull()

    // Join the first 3 categories with a comma, and add "etc" if there are more than 3 items
    val formattedCategories = if (nonNullCategories.size > 3) {
        nonNullCategories.take(3).joinToString(", ") + ", etc"
    } else {
        nonNullCategories.joinToString(", ")
    }

    // Set the formatted categories string to the TextView
    tv.text = formattedCategories
}
