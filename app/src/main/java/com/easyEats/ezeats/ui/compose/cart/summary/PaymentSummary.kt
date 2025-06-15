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
import com.easyEats.ezeats.ui.compose.theme.AppColor.BlackColor2

@Composable
fun PaymentSummary(modifier: Modifier = Modifier){
    Column(modifier = modifier.fillMaxWidth().padding(vertical = 20.dp)) {
        Text(
            text = stringResource(R.string.payment_summary),
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = BlackColor2
        )
        PaymentSummaryItem(
            modifier = Modifier.padding(top = 9.dp),
            title = stringResource(R.string.sub_total), value = "214 EGP")
        PaymentSummaryItem(
            modifier = Modifier.padding(top = 15.dp),
            title = stringResource(R.string.delivery_fee), value = "12.99 EGP")
        PaymentSummaryItem(
            modifier = Modifier.padding(top = 15.dp),
            title = stringResource(R.string.total_amount), value = "197.99 EGP")
    }


}