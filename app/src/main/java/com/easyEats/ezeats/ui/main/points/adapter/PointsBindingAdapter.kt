package com.easyEats.ezeats.ui.main.points.adapter

import android.graphics.Color
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.view.View
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.easyEats.ezeats.MyApplication
import com.easyEats.ezeats.R

/**
 * BindingAdapter to set points text with dynamic styling.
 *
 * @param tv The TextView to display the styled text.
 * @param points The number of points to be displayed. Defaults to 0 if null.
 */
@BindingAdapter("setPoints")
fun setPoints(tv: TextView, points: Int?) {
    val pointsValue = points ?: 0 // Default to 0 if points is null
    val fullText = "You have $pointsValue unused promo codes,\nstart using them now!"

    val spannable = SpannableString(fullText)

    // Set the entire text color to main color
    val mainColor = MyApplication.baseApplication.getColor(R.color.main_color)
    spannable.setSpan(
        ForegroundColorSpan(mainColor),
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

    // Highlight the word "unused" in red
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

@BindingAdapter("visibleIf")
fun View.visibleIf(isVisible: Boolean?) {
    visibility = if (isVisible == true) View.VISIBLE else View.GONE
}

/**
 * BindingAdapter to set voucher count text with dynamic styling.
 *
 * @param tv The TextView to display the styled text.
 * @param vouchers The number of vouchers to be displayed. Defaults to 0 if null.
 */
@BindingAdapter("setVoucherText")
fun setVoucherText(tv: TextView, vouchers: Int?) {
    val voucherCount = vouchers ?: 0 // Default to 0 if vouchers is null
    val fullText = "You have\n$voucherCount Unused Vouchers"

    val spannable = SpannableString(fullText)

    // Set the entire text color to black
    spannable.setSpan(
        ForegroundColorSpan(Color.BLACK),
        0,
        fullText.length,
        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
    )

    // Highlight the voucher count in the main color
    val countStartIndex = fullText.indexOf(voucherCount.toString())
    if (countStartIndex != -1) {
        val mainColor = MyApplication.baseApplication.getColor(R.color.main_color)
        spannable.setSpan(
            ForegroundColorSpan(mainColor),
            countStartIndex,
            countStartIndex + voucherCount.toString().length,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
    }

    tv.text = spannable
}

/**
 * BindingAdapter to set points text for EZeats with dynamic styling.
 *
 * @param tv The TextView to display the styled text.
 * @param vouchers The number of points to be displayed. Defaults to 0 if null.
 */
@BindingAdapter("setPointsText")
fun setPointsText(tv: TextView, vouchers: Int?) {
    val voucherCount = vouchers ?: 0 // Default to 0 if vouchers is null
    val fullText = "You have\n$voucherCount EZeats Points"

    val spannable = SpannableString(fullText)

    // Set the entire text color to black
    spannable.setSpan(
        ForegroundColorSpan(Color.BLACK),
        0,
        fullText.length,
        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
    )

    // Highlight the points count in the main color
    val countStartIndex = fullText.indexOf(voucherCount.toString())
    if (countStartIndex != -1) {
        val mainColor = MyApplication.baseApplication.getColor(R.color.main_color)
        spannable.setSpan(
            ForegroundColorSpan(mainColor),
            countStartIndex,
            countStartIndex + voucherCount.toString().length,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
    }

    tv.text = spannable
}


