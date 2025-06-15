package com.daman.edman.screens.components

import android.icu.text.MeasureFormat.FormatWidth
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun AppSpacer(
    modifier: Modifier = Modifier,
    width: Dp = 0.dp,
    height : Dp = 0.dp,
    color: Color = Color.Transparent,
    shape: Shape = RoundedCornerShape(0),
    borderWidth : Int = 0,
    borderColor: Color = Color.Transparent
){
    Spacer(modifier = modifier.height(height).width(width)
        .background(color = color, shape = shape)
        .border(width = borderWidth.dp, color = borderColor)
    )
}


@Composable
fun AppSpacerHeight(
    modifier: Modifier = Modifier,
    width: Dp = 0.dp,
    height : Dp = 16.dp,
    color: Color = Color.Transparent,
    shape: Shape = RoundedCornerShape(0),
    borderWidth : Int = 0,
    borderColor: Color = Color.Transparent
){
    Spacer(modifier = modifier.height(height).width(width)
        .background(color = color, shape = shape)
        .border(width = borderWidth.dp, color = borderColor)
    )
}