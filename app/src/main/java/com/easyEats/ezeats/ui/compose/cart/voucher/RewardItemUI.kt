package com.easyEats.ezeats.ui.compose.cart.voucher

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.daman.edman.screens.components.AppTextHeader
import com.daman.edman.screens.components.AppTextNormal
import com.easyEats.ezeats.R
import com.easyEats.ezeats.domain.main.model.response.payment.promo.ezeats.Reward
import com.easyEats.ezeats.ui.compose.helper.AppHelper.formatDateIso
import com.easyEats.ezeats.ui.compose.theme.AppColor

@Composable
fun RewardItemUI(modifier: Modifier = Modifier, reward: Reward, onClick: (Reward) -> Unit = {}) {
    Box(modifier = modifier){
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, shape = RoundedCornerShape(10.dp), color = AppColor.GreyColor)
                .padding(10.dp),
            verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.Start
        ) {
            AppTextHeader(
                text = reward.promoCode ?: "",
                fontSize = 14,
                fontWeight = FontWeight.Bold,
                color = com.easyEats.ezeats.ui.compose.theme.AppColor.BlackColor2,
                maxLines = 1
            )

            AppTextNormal(
                text = reward.promoCodeTitle ?: "",
                fontSize = 14,
                color = com.easyEats.ezeats.ui.compose.theme.AppColor.GreyColor,
                maxLines = 1
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {

                Log.d("RewardItemUI", "RewardItemUI: ${reward.promoCodeExpiryDate?.iso}")
                AppTextNormal(
                    text = "Expire: ${reward.promoCodeExpiryDate?.iso?.formatDateIso()}",
                    fontSize = 14,
                    color = com.easyEats.ezeats.ui.compose.theme.AppColor.GreyColor,
                    maxLines = 1
                )

            }



        }

        Box(
            modifier = Modifier
                .size(30.dp)
                .align(Alignment.BottomEnd)
                .background(AppColor.BlackColor2, shape = RoundedCornerShape(topStart = 10.dp))
        ) {
            IconButton(onClick = {
                onClick(reward)
            }) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = "Correct",
                    tint = AppColor.WhiteColor
                )
            }

        }
    }
}