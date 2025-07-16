package com.easyEats.ezeats.ui.compose.cart.paymentMethod

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.daman.edman.screens.components.AppTextNormal
import com.easyEats.ezeats.R
import com.easyEats.ezeats.ui.compose.theme.AppColor


@Composable
fun PaymentMethodItem(
    modifier: Modifier = Modifier,
    paymentMethodModel: PaymentMethodModel,
    isSelected: Boolean,
    select: (PaymentMethodModel) -> Unit
) {

    Row(
        modifier = modifier
            .fillMaxWidth()
            .shadow(
                elevation = 4.dp, // ✅ Shadow elevation
                shape = RoundedCornerShape(20.dp),
                clip = true // keeps the shadow outside the bounds
            )
            .background(
                if (isSelected) AppColor.BlackColor2 else AppColor.WhiteColor,
                shape = RoundedCornerShape(20.dp)
            )
            .padding(horizontal = 12.dp, vertical = 6.dp)
            .clickable {
                select(paymentMethodModel)
            },
        verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Start
    ) {

        Image(
            painter = paymentMethodModel.painter!!,
            contentDescription = paymentMethodModel.text.ifEmpty { null },
            modifier = Modifier.size(if(paymentMethodModel.isVector) 24.dp else 48.dp),
            colorFilter = if (paymentMethodModel.isVector)
                ColorFilter.tint(if (isSelected) AppColor.WhiteColor else AppColor.BlackColor2) else null
        )

        AppTextNormal(
            modifier = Modifier.padding(start = 8.dp),
            text = paymentMethodModel.text,
            fontSize = 18,
            fontWeight = FontWeight.Normal,
            color = if (isSelected) AppColor.WhiteColor else AppColor.BlackColor2
        )

        Spacer(modifier = Modifier.weight(1f))

        RadioButton(
            selected = isSelected,
            colors = RadioButtonDefaults.colors(
                selectedColor = AppColor.WhiteColor,
                unselectedColor = AppColor.BlackColor2,
            ),
            onClick = {
                select(paymentMethodModel)
            },
        )

    }

}


@Preview
@Composable
fun PaymentMethodItemPreview() {
//    PaymentMethodItem()
}