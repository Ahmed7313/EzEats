package com.daman.edman.screens.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.easyEats.ezeats.R
import com.easyEats.ezeats.ui.gilroy


@Composable
fun IconTextView(
    modifier: Modifier = Modifier,
    text: String,
    icon: Int,
    textColor : Color = Color(0xFF3A3D42),
    tint : Color = Color.Black
) {

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically) {

        Icon(
            painter = painterResource(id = icon),
            contentDescription = null,
            modifier = Modifier.size(10.dp),
            tint = tint
        )

        AppSpacer(width = 2.dp)
        AutoSizeText(text = text, fontSize = 12.sp, color = textColor, fontFamily = gilroy)
    }
}

@Preview
@Composable
fun IconTextViewPreview() {
    IconTextView(text = "Icon Text View", icon = es.dmoral.toasty.R.drawable.ic_check_white_24dp)
}