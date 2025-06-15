package com.easyEats.ezeats.ui.compose.cart.paymentSummaryScreen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.easyEats.ezeats.ui.compose.components.AppImage
import com.easyEats.ezeats.ui.compose.theme.AppColor

@Composable
fun PaymentMethodItem(modifier: Modifier = Modifier,paymentInt: Int = 0, isSelected: Boolean, onClick: (Int) -> Unit) {
    val selected by remember {
        mutableStateOf(isSelected)
    }
    TextButton(
        modifier = modifier.padding(vertical = 7.dp).fillMaxWidth(),
        shape = RoundedCornerShape(100.dp),
        elevation = ButtonDefaults.buttonElevation(5.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isSelected) AppColor.ColorPrimary else Color.White,
            contentColor = if (isSelected) AppColor.ColorPrimary else Color.White,
            disabledContainerColor = Color.Transparent,
            disabledContentColor = Color.Transparent
        ),
        border = BorderStroke(if(isSelected) 1.dp else 0.dp,if (isSelected) AppColor.ColorPrimary else AppColor.WhiteColor),
        onClick = {
            onClick(paymentInt)
    }) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Start){
            AppImage(
                imageUrl = "https://toppng.com/uploads/preview/visa-logo-11530966316amvzjrobsf.png",
                cornerRadius = 0,
                size = 20
            )

            Text(
                modifier = Modifier.padding(start = 10.dp),
                text = "XXXX-7319",
                fontSize = 15.sp,
                textAlign = TextAlign.Start,
                fontWeight = FontWeight.Bold,
                color = if (isSelected) Color.White else Color.Black
            )
        }
    }
}