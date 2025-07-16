package com.easyEats.ezeats.ui.compose.cart.summary

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.daman.edman.screens.components.AppTextHeader
import com.easyEats.ezeats.R
import com.easyEats.ezeats.domain.main.model.request.cart.response.UpdateCartResponse
import com.easyEats.ezeats.ui.compose.helper.AppHelper.formatTextNumber
import com.easyEats.ezeats.ui.compose.theme.AppColor
import com.easyEats.ezeats.ui.compose.theme.AppColor.BlackColor2

@Composable
fun PaymentSummary(modifier: Modifier = Modifier, updateCartResponse: UpdateCartResponse){
    Column(modifier = modifier.fillMaxWidth().padding(bottom = 20.dp)) {
        AppTextHeader(
            text = stringResource(R.string.payment_summary),
            fontSize = 16,
            fontWeight = FontWeight.Bold,
            color = BlackColor2,
            modifier = Modifier.padding(bottom = 9.dp)
        )
        PaymentSummaryItem(
            modifier = Modifier,
            title = stringResource(R.string.sub_total),
            value = "${updateCartResponse.result?.subtotal?.formatTextNumber()} EGP")

        PaymentSummaryItem(
            modifier = Modifier,
            title = stringResource(R.string.delivery_fee), value = "${updateCartResponse.result?.deliveryFees} EGP")
        PaymentSummaryItem(
            modifier = Modifier,
            title = stringResource(R.string.service_fees),
            value = "${updateCartResponse.result?.serviceFees?.formatTextNumber()} EGP")

        PaymentSummaryItem(
            modifier = Modifier,
            title = stringResource(R.string.tax),
            value = "${updateCartResponse.result?.tax?.formatTextNumber()} EGP"
        )

        PaymentSummaryItem(
            modifier = Modifier,
            title = stringResource(R.string.promo_code),
            value = "${updateCartResponse.result?.promoCodeDiscountValue} EGP"
        )

        if(updateCartResponse.result?.tip != 0.0) {
            PaymentSummaryItem(
                modifier = Modifier,
                title = stringResource(R.string.tip),
                value = "${updateCartResponse.result?.tip?.formatTextNumber()} EGP"
            )
        }

        HorizontalDivider(
            modifier = Modifier.padding(vertical = 6.dp),
            thickness = 1.dp,
            color = AppColor.GreyColor
        )
        PaymentSummaryItem(
            modifier = Modifier,
            title = stringResource(R.string.total_amount),
            value = "${updateCartResponse.result?.total?.formatTextNumber()} EGP", fontWeight = FontWeight.Bold)
    }


}