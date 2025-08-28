package com.easyEats.ezeats.ui.compose.cart.voucher

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotStateList
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
import com.easyEats.ezeats.domain.main.model.response.payment.promo.ezeats.Reward
import com.easyEats.ezeats.ui.compose.components.AppInputField
import com.easyEats.ezeats.ui.compose.theme.AppColor
import com.easyEats.ezeats.ui.compose.theme.AppColor.BlackColor2
import com.easyEats.ezeats.ui.compose.theme.AppColor.GreyColor
import com.easyEats.ezeats.ui.compose.theme.AppColor.skyColor

@Composable
fun VoucherOrPromoUI(
    modifier: Modifier = Modifier,
    rewards: SnapshotStateList<Reward?>,
    promoCode: String = "",
    isSuccessPromo: Boolean? = null,
    onChange: (String) -> Unit,
    onSelectReward: (Reward) -> Unit,
    add: () -> Unit,
    clear: () -> Unit
) {
    Column(
        modifier = modifier
            .padding(bottom = 20.dp, top = 10.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.vouchers_or_promo),
            fontSize = 16.sp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 10.dp),
            textAlign = TextAlign.Start,
            fontWeight = FontWeight.Bold,
            color = BlackColor2
        )

        if(rewards.isNotEmpty()) {
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 10.dp),
                verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Start) {
                items(rewards.size) { index ->
                    val reward = rewards[index]
                    if (reward != null) {
                        RewardItemUI(modifier = Modifier.width(120.dp), reward, onClick = onSelectReward)
                    }
                }

            }
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
                .border(BorderStroke(2.dp, skyColor), shape = RoundedCornerShape(100.dp))
                .background(AppColor.WhiteColor, shape = RoundedCornerShape(100.dp))
        ) {

            Icon(
                modifier = Modifier
                    .size(28.dp)
                    .padding(start = 10.dp),
                painter = painterResource(R.drawable.ic_gift),
                tint = Color.Black,
                contentDescription = "gift"
            )
            AppInputField(
                modifier = Modifier
                    .weight(1f),
                text = promoCode,
                hint = stringResource(R.string.enter_your_voucher_code),
            ) {
                onChange(it)
            }

            Spacer(modifier = Modifier.weight(1f))
            Box(
                modifier = Modifier
                    .background(
                        color = if(promoCode.isNotEmpty()) AppColor.BlackColor else GreyColor.copy(alpha = 0.1f),
                        shape = RoundedCornerShape(100)
                    )
            ) {
                IconButton(onClick = { if (isSuccessPromo == true) clear() else if (promoCode.isNotEmpty()) add() }) {
                    Icon(
                        modifier = Modifier.size(24.dp),
                        painter = if (isSuccessPromo == true)
                            painterResource(R.drawable.close_ic)
                        else painterResource(
                            R.drawable.ic_plus
                        ),
                        tint = if (promoCode.isNotEmpty()) AppColor.WhiteColor
                        else AppColor.BlackColor,
                        contentDescription = "arrow_right"
                    )
                }
            }

        }

        if (isSuccessPromo != null) {

            Text(
                text = stringResource(
                    if (isSuccessPromo)
                        R.string.you_gained_a_promo_code_value
                    else
                        R.string.promo_code_not_found
                ),
                color = if (isSuccessPromo) AppColor.greenColor else Color.Red,
                fontSize = 14.sp,
                modifier = Modifier.padding(top = 10.dp)
            )

        }


    }


}