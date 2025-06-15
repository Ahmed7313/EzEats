package com.easyEats.ezeats.ui.compose.cart.paymentSummaryScreen

import android.graphics.Color
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
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
    Column(
        modifier = mod
            .width(107.dp)
            .height(72.dp)
            .background(
                if (isSelected) AppColor.ColorPrimaryOpacity else AppColor.WhiteColor,
                shape = RoundedCornerShape(16.dp)
            )
            .border(
                BorderStroke(1.dp, if (isSelected) ColorPrimary else AppColor.BlackColor),
                shape = RoundedCornerShape(16.dp)
            ).clickable {
                onTipSelected(option)
            },
        verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = option.amount ?: "Custom",
            fontSize = 16.sp,
        )
        Text(
            color = if (option.percentage.isNullOrEmpty()) BlackColor else ColorPrimary,
            text = option.percentage ?: "Tip",
            fontSize = 16.sp,
        )

    }
}