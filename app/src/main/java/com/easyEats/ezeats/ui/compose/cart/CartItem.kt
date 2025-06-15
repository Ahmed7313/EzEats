package com.easyEats.ezeats.ui.compose.cart

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.easyEats.ezeats.R
import com.easyEats.ezeats.ui.compose.components.AppImage
import com.easyEats.ezeats.ui.compose.theme.AppColor.BlackColor2
import com.easyEats.ezeats.ui.compose.theme.AppColor.ColorPrimary

@Composable
fun CartItem(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier.fillMaxWidth().padding(10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {

        Column(verticalArrangement = Arrangement.Top, horizontalAlignment = Alignment.Start) {
            Text(
                text = "Nutella Giant Cookie",
                color = BlackColor2,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = "Olive Ciabattae",
                color = BlackColor2,
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium
            )
            Spacer(modifier = Modifier.height(6.dp))
            Row {
                Icon(painter = painterResource(R.drawable.edit_icon), tint = ColorPrimary, contentDescription = "editColor")
                Text(
                    text = stringResource(R.string.edit),
                    color = ColorPrimary,
                    fontSize = 11.sp
                )
            }
            Spacer(modifier = Modifier.height(14.dp))
            Text(
                text = "100 EGP",
                color = BlackColor2,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold
            )

        }

        AppImage(
            imageUrl = "https://upload.wikimedia.org/wikipedia/commons/6/6d/Good_Food_Display_-_NCI_Visuals_Online.jpg",
            size = 100,
            cornerRadius = 10
        )
//        Text("welcome")
    }
    HorizontalDivider(
        modifier = Modifier.fillMaxWidth(),
        thickness = 1.dp,
        color = BlackColor2.copy(
            alpha = 0.5f
        )
    )

}