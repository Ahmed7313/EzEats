package com.easyEats.ezeats.ui.main.points.adapter

import android.graphics.Color
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.easyEats.ezeats.MyApplication
import com.easyEats.ezeats.R

@BindingAdapter("setPoints")
fun setPoints(tv: TextView, points: Int?) {
    val pointsValue = points ?: 0 // Handle null points with a default value
    val fullText = "You have $pointsValue unused promo codes,\nstart using them now!"

    val spannable = SpannableString(fullText)

    // Set the color for the entire text to black
    spannable.setSpan(
        MyApplication.baseApplication.getColor(R.color.main_color),
        0,
        fullText.length,
        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
    )

    // Highlight "$pointsValue" in red
    val pointsStartIndex = fullText.indexOf(pointsValue.toString())
    if (pointsStartIndex != -1) {
        spannable.setSpan(
            ForegroundColorSpan(Color.RED),
            pointsStartIndex,
            pointsStartIndex + pointsValue.toString().length,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
    }

    // Highlight "unused" in red
    val unusedStartIndex = fullText.indexOf("unused")
    if (unusedStartIndex != -1) {
        spannable.setSpan(
            ForegroundColorSpan(Color.RED),
            unusedStartIndex,
            unusedStartIndex + "unused".length,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
    }

    tv.text = spannable
}


@BindingAdapter("setVoucherText")
fun setVoucherText(tv: TextView, vouchers: Int?) {
    val voucherCount = vouchers ?: 0 // Handle null vouchers with a default value
    val fullText = "You have\n$voucherCount Unused Vouchers"

    val spannable = SpannableString(fullText)

    // Set the entire text color to black
    spannable.setSpan(
        ForegroundColorSpan(Color.BLACK),
        0,
        fullText.length,
        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
    )

    // Highlight the voucher count (e.g., "10") in red (or your custom color)
    val countStartIndex = fullText.indexOf(voucherCount.toString())
    if (countStartIndex != -1) {
        val color = MyApplication.baseApplication.getColor(R.color.main_color) // Get color resource
        spannable.setSpan(
            ForegroundColorSpan(color), // Use ForegroundColorSpan with the resolved color
            countStartIndex,
            countStartIndex + voucherCount.toString().length,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
    }

    tv.text = spannable
}


@BindingAdapter("setPointsText")
fun setPointsText(tv: TextView, vouchers: Int?) {
    val voucherCount = vouchers ?: 0 // Handle null vouchers with a default value
    val fullText = "You have\n$voucherCount EZeats Points"

    val spannable = SpannableString(fullText)

    // Set the entire text color to black
    spannable.setSpan(
        ForegroundColorSpan(Color.BLACK),
        0,
        fullText.length,
        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
    )

    // Highlight the voucher count (e.g., "10") in red (or your custom color)
    val countStartIndex = fullText.indexOf(voucherCount.toString())
    if (countStartIndex != -1) {
        val color = MyApplication.baseApplication.getColor(R.color.main_color) // Get color resource
        spannable.setSpan(
            ForegroundColorSpan(color), // Use ForegroundColorSpan with the resolved color
            countStartIndex,
            countStartIndex + voucherCount.toString().length,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
    }

    tv.text = spannable
}

