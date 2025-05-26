package com.easyEats.ezeats.ui

import androidx.compose.material3.Typography
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import com.easyEats.ezeats.R

val gilroy = FontFamily(
    Font(R.font.gilroy_400, FontWeight.Black),
    Font(R.font.gilroy_500, FontWeight.Black, FontStyle.Italic),
    Font(R.font.gilroy_700, FontWeight.Bold),
    Font(R.font.gilroy_600, FontWeight.Bold, FontStyle.Italic),
)

// You can then use this font family in your MaterialTheme typography
val typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = gilroy,
        fontWeight = FontWeight.W400,
        fontSize = 12.sp,
        color = Color.Black,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp,
    ),
    // Other text styles...
)