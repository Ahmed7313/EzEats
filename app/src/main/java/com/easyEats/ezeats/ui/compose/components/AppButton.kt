package com.easyEats.ezeats.ui.compose.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.easyEats.ezeats.ui.compose.theme.AppColor


@Composable
fun AppButtonPrimary(
    modifier: Modifier = Modifier,
    text: String,
    enabled: Boolean = true,
    showLoader: Boolean = false,
    color: Color = AppColor.ColorPrimary,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier
            .width(173.dp)
            .height(45.dp),
        shape = RoundedCornerShape(50.dp), // Full rounded
        colors = ButtonDefaults.buttonColors(containerColor = color) // Red color
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {

            if (showLoader) {
                LoadingCircle()
                Spacer(modifier = Modifier.width(30.dp))
            }

            Text(
                text = text,
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )

        }

    }
}

@Composable
fun AppButtonStroke(
    modifier: Modifier = Modifier,
    text: String,
    enabled: Boolean = true,
    onClick: () -> Unit
) {
    OutlinedButton(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier
            .width(190.dp)
            .height(45.dp),
        shape = RoundedCornerShape(50.dp), // Fully rounded
        border = BorderStroke(width = 1.dp, Color.Black)
    ) {
        Text(
            text = text,
            color = Color.Black,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )
    }
}