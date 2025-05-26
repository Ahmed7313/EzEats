package com.daman.edman.screens.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.easyEats.ezeats.ui.large
import com.easyEats.ezeats.ui.spacing


@Composable
fun AppHolder(modifier: Modifier = Modifier,
              color: Color = Color.White,
              view : @Composable ()-> Unit) {

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(color = color)
            .padding(start = spacing, end = spacing, top = large, bottom = large)
    ) {
        view()
    }
}