package com.easyEats.ezeats.ui.compose.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.easyEats.ezeats.ui.compose.theme.AppColor


@Composable
fun AppButtonPrimary(modifier: Modifier = Modifier,text: String, onClick: () -> Unit){
    Button(
        onClick = onClick,
        modifier = modifier
            .width(173.dp)
            .height(45.dp),
        shape = RoundedCornerShape(50.dp), // Full rounded
        colors = ButtonDefaults.buttonColors(containerColor = AppColor.ColorPrimary) // Red color
    ) {
        Text(
            text = text,
            color = Color.White,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun AppButtonStroke(modifier: Modifier = Modifier, text: String, onClick: () -> Unit){
    OutlinedButton(
        onClick = onClick,
        modifier = modifier
            .width(190.dp)
            .height(45.dp),
        shape = RoundedCornerShape(50.dp), // Fully rounded
        border = BorderStroke(width = 1.dp,Color.Black)
    ) {
        Text(
            text = text,
            color = Color.Black,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )
    }
}