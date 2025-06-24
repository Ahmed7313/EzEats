package com.easyEats.ezeats.ui.compose.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage


@Composable
fun AppImage(
    modifier: Modifier = Modifier,
    imageUrl : String , size: Int = 100,cornerRadius : Int = 0){
    AsyncImage(
        model = imageUrl,
        contentDescription = "Loaded Image",
        contentScale = ContentScale.FillBounds,
        modifier = modifier
            .size(size.dp)
            .clip(RoundedCornerShape(cornerRadius.dp))
    )
}

@Composable
fun AppImageCornerTop(
    modifier: Modifier = Modifier,
    imageUrl : String , size: Int = 100,cornerRadius : Int = 0){
    AsyncImage(
        model = imageUrl,
        contentDescription = "Loaded Image",
        contentScale = ContentScale.FillBounds,
        modifier = modifier
            .size(size.dp)
            .clip(RoundedCornerShape(topStart = cornerRadius.dp, topEnd = cornerRadius.dp))
    )
}