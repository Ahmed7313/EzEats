package com.easyEats.ezeats.ui.main.voucher.adapter

import android.graphics.Color
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.View
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.easyEats.ezeats.R
import com.easyEats.ezeats.domain.main.model.response.loyality_center.SpinWheelVoucher
import com.easyEats.ezeats.domain.main.model.response.voucher.VoucherResponseItem
import java.text.SimpleDateFormat
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale


/**
 * Custom BindingAdapter for formatting and displaying the date in a specific format.
 * It takes a date string, reformats it, and sets the formatted date in the TextView.
 * If the date string is null or empty, it hides the TextView.
 */
@BindingAdapter("setDate")
fun reformatDate(tv: TextView, dateString: String?) {
    if (dateString.isNullOrEmpty().not()) {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        val outputFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        val date = inputFormat.parse(dateString)
        tv.text = "Expires ${outputFormat.format(date)}*"
    } else {
        tv.visibility = View.INVISIBLE
    }
}

/**
 * Custom BindingAdapter for setting the background and text based on the voucher's state.
 * The background and text of the TextView are updated based on whether the voucher is redeemed, expired, or active.
 */
@BindingAdapter("setVoucherBackground")
fun voucherBackground(tv: TextView, voucher: VoucherResponseItem) {
    if (voucher.redeemed == true) {
        tv.setBackgroundResource(R.drawable.redeemed_background)
        tv.text = "Redeemed"
    } else if (isDateBeforeToday(voucher.promoCodeExpiryDate?.iso)) {
        tv.setBackgroundResource(R.drawable.redeemed_background)
        tv.text = "Expired"
    } else {
        tv.setBackgroundResource(R.drawable.active_voucher_background)
        tv.text = "Active"
    }
}

/**
 * Custom BindingAdapter for setting the background and text based on the state of a SpinWheelVoucher.
 * Similar to `setVoucherBackground`, but for SpinWheelVoucher objects.
 */
@BindingAdapter("setSpinWheelVoucherBackground")
fun setSpinWheelVoucherBackground(tv: TextView, voucher: SpinWheelVoucher) {
    if (voucher.isRedeemed == true) {
        tv.setBackgroundResource(R.drawable.redeemed_background)
        tv.text = "Redeemed"
    } else if (isDateBeforeToday(voucher.expiryDate?.iso)) {
        tv.setBackgroundResource(R.drawable.redeemed_background)
        tv.text = "Expired"
    } else {
        tv.setBackgroundResource(R.drawable.active_voucher_background)
        tv.text = "Active"
    }
}

/**
 * Helper function to check if a given date is before the current date.
 * It parses the date string into a ZonedDateTime and compares it with the current date.
 *
 * @param dateString The date string to be checked.
 * @return `true` if the date is before today, `false` otherwise.
 */
fun isDateBeforeToday(dateString: String?): Boolean {
    val dateToCheck = ZonedDateTime.parse(dateString, DateTimeFormatter.ISO_DATE_TIME)
    val currentDate = ZonedDateTime.now()
    return dateToCheck.isBefore(currentDate)
}

/**
 * Custom BindingAdapter for styling and displaying the voucher information in a TextView.
 * It applies a color style to parts of the voucher's text. The promo code is highlighted in red,
 * while the other parts of the text remain black.
 */
@BindingAdapter("styledText")
fun setStyledText(tv: TextView, voucher: VoucherResponseItem) {
    val promoCode = voucher.promoCode
    val promoCodeTitle = voucher.promoCodeTitle
    val restaurantName = voucher.restaurantName

    // Full text to be displayed
    val fullText = "$promoCode- $promoCodeTitle next order from $restaurantName"

    // Create a SpannableString for the full text
    val spannable = SpannableString(fullText)

    // Set the first part (promoCode) in red color
    spannable.setSpan(
        ForegroundColorSpan(Color.RED),
        0, promoCode?.length ?: 0,
        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
    )

    // Set the rest of the text (promoCodeTitle and restaurantName) in black color
    spannable.setSpan(
        ForegroundColorSpan(Color.BLACK),
        promoCode?.length ?: 0, fullText.length,
        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
    )

    // Set the styled text to the TextView
    tv.text = spannable
}


