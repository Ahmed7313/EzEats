package com.easyEats.ezeats.ui.compose.cart.summary

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.easyEats.ezeats.R
import com.easyEats.ezeats.domain.main.model.request.cart.response.UpdateCartResponse
import com.easyEats.ezeats.ui.compose.theme.AppColor.BlackColor2

@Composable
fun PaymentSummary(modifier: Modifier = Modifier, updateCartResponse: UpdateCartResponse){
    Column(modifier = modifier.fillMaxWidth().padding(vertical = 20.dp)) {
        Text(
            text = stringResource(R.string.payment_summary),
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = BlackColor2
        )
        PaymentSummaryItem(
            modifier = Modifier.padding(top = 9.dp),
            title = stringResource(R.string.sub_total), value = "${updateCartResponse.result?.subtotal} EGP")
        PaymentSummaryItem(
            modifier = Modifier.padding(top = 9.dp),
            title = stringResource(R.string.tip), value = "${updateCartResponse.result?.tip} EGP")
        PaymentSummaryItem(
            modifier = Modifier.padding(top = 9.dp),
            title = stringResource(R.string.delivery_fee), value = "${updateCartResponse.result?.deliveryFees} EGP")
        PaymentSummaryItem(
            modifier = Modifier.padding(top = 9.dp),
            title = stringResource(R.string.service_fees), value = "${updateCartResponse.result?.serviceFees} EGP")
        PaymentSummaryItem(
            modifier = Modifier.padding(top = 9.dp),
            title = stringResource(R.string.total_amount), value = "${updateCartResponse.result?.total} EGP")
    }


}