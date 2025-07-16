package com.easyEats.ezeats.ui.compose.cart.summary

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.daman.edman.screens.components.AppTextHeader
import com.daman.edman.screens.components.AppTextNormal

@Composable
fun PaymentSummaryItem(
    modifier: Modifier = Modifier,
    title: String,
    value: String,
    color: Color = Color.Black,
    fontWeight: FontWeight = FontWeight.Normal,
) {

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {

        if(fontWeight == FontWeight.Normal) {

            AppTextNormal(
                text = title,
                fontSize = 16,
                fontWeight = FontWeight.Normal,
                color = color
            )

            AppTextNormal(
                text = value,
                fontSize = 16,
                fontWeight = FontWeight.Normal,
                color = color
            )
        }else{
            AppTextHeader(
                text = title,
                fontSize = 16,
                fontWeight = FontWeight.Bold,
                color = color
            )

            AppTextHeader(
                text = value,
                fontSize = 16,
                fontWeight = FontWeight.Bold,
                color = color
            )
        }

    }


}