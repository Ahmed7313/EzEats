package com.easyEats.ezeats.ui.compose.cart.paymentSummaryScreen

import android.graphics.Color
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.easyEats.ezeats.R
import com.easyEats.ezeats.ui.compose.cart.paymentSummaryScreen.dummy.TipOption
import com.easyEats.ezeats.ui.compose.theme.AppColor
import com.easyEats.ezeats.ui.compose.theme.AppColor.BlackColor
import com.easyEats.ezeats.ui.compose.theme.AppColor.BlackColor2
import com.easyEats.ezeats.ui.compose.theme.AppColor.ColorPrimary


@Composable
fun TipOptionItem(
    mod: Modifier = Modifier,
    option: TipOption,
    isSelected: Boolean,
    onTipSelected: (TipOption) -> Unit
) {
    Box(modifier = mod.padding(end = 16.dp)) {
        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .width(107.dp)
                .height(72.dp)
                .background(
                    if (isSelected) AppColor.ColorPrimaryOpacity else AppColor.WhiteColor,
                    shape = RoundedCornerShape(5.dp)
                )
                .border(
                    BorderStroke(1.dp, if (isSelected) ColorPrimary else AppColor.BlackColor),
                    shape = RoundedCornerShape(5.dp)
                )
                .clickable {
                    onTipSelected(option)
                },
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = if (option.amount != null)
                    "${option.amount} EGP" else "Custom",
                fontSize = 16.sp,
            )
            Text(
                color = if (option.percentage.isNullOrEmpty()) BlackColor else ColorPrimary,
                text = option.percentage ?: "Tip",
                fontSize = 16.sp,
            )

        }
    }

}


@Composable
fun TipOptionCurrencyItem(
    mod: Modifier = Modifier,
    option: TipOption,
    isSelected: Boolean,
    onTipSelected: (TipOption) -> Unit
) {
    TextButton(
        modifier = mod,
        onClick = {
            onTipSelected(option)
        }) {
        Text(
            text = option.amount ?: "Custom",
            color = if (isSelected) AppColor.WhiteColor else AppColor.BlackColor,
            fontSize = 16.sp,
            modifier = Modifier
                .padding(horizontal = 20.dp, vertical = 11.dp)
                .background(
                    if (isSelected) AppColor.WhiteColor else AppColor.ColorPrimary,
                    shape = RoundedCornerShape(50.dp)
                )
        )
    }

}