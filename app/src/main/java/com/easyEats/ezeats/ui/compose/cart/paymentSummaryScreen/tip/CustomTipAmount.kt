package com.easyEats.ezeats.ui.compose.cart.paymentSummaryScreen.tip

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.daman.edman.screens.components.AppTextHeader
import com.daman.edman.screens.components.AppTextNormal
import com.easyEats.ezeats.R
import com.easyEats.ezeats.ui.compose.components.AppButtonPrimary
import com.easyEats.ezeats.ui.compose.components.AppInputField
import com.easyEats.ezeats.ui.compose.components.AppInputTextLabelTop
import com.easyEats.ezeats.ui.compose.theme.AppColor

@Composable
fun CustomTipAmount(
    modifier: Modifier = Modifier,
    total: Double = 2000.0,
    result: (String) -> Unit
) {

    var value by remember {
        mutableStateOf("")
    }

    var percentage by remember {
        mutableStateOf("0")
    }


    Spacer(modifier = Modifier.height(10.dp))

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxWidth()
            .shadow(
                elevation = 8.dp,
                shape = RoundedCornerShape(20.dp),
                clip = false
            )
            .background(AppColor.WhiteColor, shape = RoundedCornerShape(20.dp))
            .padding(20.dp)
    ) {


        AppTextNormal(
            text = stringResource(R.string.insert_your_amount_you_like_to_tip),
            color = AppColor.GreyColor,
            fontSize = 12
        )

        Spacer(modifier = Modifier.height(9.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {

            AppInputField(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                text = value,
                hint = "${stringResource(R.string.enter)} ${stringResource(R.string.tip)}"
            ) {
                value = it
                try {
                    val tipAmount = value.toDouble()
                    val calculatedPercentage = (tipAmount / total) * 100
                    percentage = "%.1f".format(calculatedPercentage)
                } catch (e: Exception) {
                    percentage = "0"
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                AppTextHeader(
                    text = stringResource(R.string.currency),
                    fontSize = 20,
                    color = AppColor.BlackColor2
                )
                AppTextNormal(
                    modifier = Modifier.padding(top = 3.dp),
                    text = "$percentage %",
                    fontSize = 12,
                    color = AppColor.ColorPrimary
                )
            }
        }

        Spacer(modifier = Modifier.height(9.dp))

        AppButtonPrimary(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(R.string.add_tip),
            color = AppColor.BlackColor2
        ) {
            if (value.trim().isNotEmpty()) {
                result(value)
            }
        }
    }
}