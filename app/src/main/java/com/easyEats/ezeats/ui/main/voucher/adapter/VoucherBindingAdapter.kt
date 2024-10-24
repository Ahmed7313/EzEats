package com.easyEats.ezeats.ui.main.voucher.adapter

import android.graphics.Color
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.View
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.easyEats.ezeats.R
import com.easyEats.ezeats.domain.main.model.response.voucher.VoucherResponseItem
import java.text.SimpleDateFormat
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

@BindingAdapter("setDate")
fun reformatDate(tv: TextView, dateString: String?) {
    if (dateString.isNullOrEmpty().not()) {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        val outputFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val date = inputFormat.parse(dateString)
        tv.text = outputFormat.format(date)
    } else {
        tv.visibility = View.GONE
    }
}

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

fun isDateBeforeToday(dateString: String?): Boolean {
    // Parse the input date string to a ZonedDateTime
    val dateToCheck = ZonedDateTime.parse(dateString, DateTimeFormatter.ISO_DATE_TIME)

    // Get the current date and time in the same time zone
    val currentDate = ZonedDateTime.now()

    // Compare the date to check if it's before today
    return dateToCheck.isBefore(currentDate)
}


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


