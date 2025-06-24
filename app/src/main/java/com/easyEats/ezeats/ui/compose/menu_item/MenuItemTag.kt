package com.easyEats.ezeats.ui.compose.menu_item

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.daman.edman.screens.components.HeaderText
import com.easyEats.ezeats.R
import com.easyEats.ezeats.ui.compose.theme.AppColor


@Composable
fun MenuItemTag(modifier: Modifier = Modifier, menuItemTag: Boolean = true) {
    if (menuItemTag) {
        Box(
            modifier = modifier.background(
                color = AppColor.BlackColor,
                shape = RoundedCornerShape(16.dp)
            )
        ) {
            HeaderText(
                text = stringResource(R.string.required),
                modifier = modifier.align(Alignment.Center).padding(horizontal = 14.dp, vertical = 4.dp),
                color = AppColor.WhiteColor,
                fontSize = 16
            )
        }
    }
}