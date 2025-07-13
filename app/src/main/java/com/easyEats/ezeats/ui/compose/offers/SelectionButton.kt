package com.easyEats.ezeats.ui.compose.offers

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.daman.edman.screens.components.AppSpacer
import com.daman.edman.screens.components.AppTextNormal
import com.easyEats.ezeats.ui.RedColor
import com.easyEats.ezeats.ui.medium
import com.easyEats.ezeats.ui.spacing

@Composable
fun SelectionButton(
    text: String,
    modifier: Modifier = Modifier,
    icon: Int,
    isSelected: Boolean,
    onSelect: () -> Unit
) {
    val backgroundColor = if (isSelected) Color.Blue else Color.LightGray
    val textColor = if (isSelected) Color.White else Color.DarkGray

    Row(
        modifier = modifier
            .height(34.dp)
            .clickable {
                onSelect()
            }
            .background(
                color = if (isSelected) RedColor else Color.White,
                shape = RoundedCornerShape(spacing)
            )
            .border(
                width = 1.dp,
                color = if (isSelected) RedColor else Color.White,
                shape = RoundedCornerShape(spacing)
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        AppTextNormal(text = text, color = if (isSelected) Color.White else Color.Black, fontSize = 15)
        AppSpacer(width = medium)
        Icon(
            painter = painterResource(id = icon),
            contentDescription = null,
            modifier = Modifier.size(18.dp),
            tint = if (isSelected) Color.White else Color.Black
        )
    }
}