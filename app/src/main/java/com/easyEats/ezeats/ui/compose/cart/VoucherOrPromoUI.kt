package com.easyEats.ezeats.ui.compose.cart

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.easyEats.ezeats.R
import com.easyEats.ezeats.ui.compose.theme.AppColor.BlackColor2
import com.easyEats.ezeats.ui.compose.theme.AppColor.ColorPrimary
import com.easyEats.ezeats.ui.compose.theme.AppColor.GreyColor
import com.easyEats.ezeats.ui.compose.theme.AppColor.skyColor

@Composable
fun VoucherOrPromoUI(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.padding(bottom = 20.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.vouchers_or_promo),
            fontSize = 16.sp,
            modifier = Modifier.fillMaxWidth().padding(bottom = 10.dp),
            textAlign = TextAlign.Start,
            fontWeight = FontWeight.Bold,
            color = BlackColor2
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
                .border(BorderStroke(2.dp, skyColor), shape = RoundedCornerShape(100.dp))
                .padding(10.dp)
        ) {

            Icon(
                modifier = Modifier.size(21.dp),
                painter = painterResource(R.drawable.ic_gift),
                tint = Color.Black,
                contentDescription = "gift"
            )
            Column(modifier = Modifier.padding(start = 2.dp)) {
                Text(text = "EGP 50 OFF", color = Color.Black, fontSize = 16.sp)
                Text(text = "Add EGP 222 more to apply", color = GreyColor, fontSize = 12.sp)
            }
            Spacer(modifier = Modifier.weight(1f))
            Text(text = stringResource(R.string.apply), color = ColorPrimary, fontSize = 12.sp)
        }


    }




}