package com.easyEats.ezeats.ui.main.active_order_details.binding_adapters

import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.easyEats.ezeats.MyApplication
import com.easyEats.ezeats.R
import com.easyEats.ezeats.domain.main.model.response.active_order.ActiveOrderResponse
import com.easyEats.ezeats.ui.main.active_order_details.states.States
import java.util.Locale

@BindingAdapter("setOrderStatus")
fun setOrderStatus(tv: TextView, activeOrder: ActiveOrderResponse?) {
    val text = "Your Order from"
    val restaurantName = activeOrder?.restaurantName
    val stateText =
        if (activeOrder?.state?.toLowerCase(Locale.getDefault()) == "placed") "has been ${activeOrder.state}"
        else "is ${activeOrder?.state}"

    // Build the full message
    val fullText = "$text $restaurantName\n$stateText"

    // Create a SpannableString
    val spannableString = SpannableString(fullText)

    // Find the start and end index of the state text
    val stateStartIndex = fullText.indexOf(stateText)
    val stateEndIndex = stateStartIndex + stateText.length

    // Apply a red color span to the state text
    val colorSpan = ForegroundColorSpan(MyApplication.baseApplication.getColor(R.color.main_color))
    spannableString.setSpan(
        colorSpan,
        stateStartIndex,
        stateEndIndex,
        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
    )

    // Set the spannable text to the TextView
    tv.text = spannableString
}

@BindingAdapter("setStateImage")
fun setStateImage(iv: ImageView, state: String?) {
    val backendState = States.fromState(state)
    val imageResId = backendState.imageResId
    iv.setImageResource(imageResId)
}

@BindingAdapter("setOrderStatusId")
fun setOrderStatusId(tv: TextView, activeOrder: ActiveOrderResponse?) {
    val text = "Your Order"
    val orderNo = "#${activeOrder?.orderNumber}"
    val stateText =
        if (activeOrder?.state?.toLowerCase(Locale.getDefault()) == "placed") "has been ${activeOrder.state}"
        else "is ${activeOrder?.state}"

    // Build the full message
    val fullText = "$text $orderNo\n$stateText"

    // Create a SpannableString
    val spannableString = SpannableString(fullText)

    // Find the start and end index of the state text
    val stateStartIndex = fullText.indexOf(orderNo)
    val stateEndIndex = stateStartIndex + (orderNo.length)

    // Apply a red color span to the state text
    val colorSpan = ForegroundColorSpan(MyApplication.baseApplication.getColor(R.color.main_color))
    spannableString.setSpan(
        colorSpan,
        stateStartIndex,
        stateEndIndex,
        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
    )

    // Set the spannable text to the TextView
    tv.text = spannableString
}
