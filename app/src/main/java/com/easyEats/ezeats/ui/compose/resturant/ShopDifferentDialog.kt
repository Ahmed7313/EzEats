package com.easyEats.ezeats.ui.compose.resturant

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.daman.edman.screens.components.AppTextHeader
import com.daman.edman.screens.components.AppTextNormal
import com.easyEats.ezeats.R
import com.easyEats.ezeats.ui.compose.components.AppButtonPrimary
import com.easyEats.ezeats.ui.compose.components.AppButtonStroke
import com.easyEats.ezeats.ui.compose.theme.AppColor


@Composable
fun ShopDifferentDialog(
    modifier: Modifier = Modifier,
    start: () -> Unit,
    dismiss: () -> Unit
) {
    Dialog(

        onDismissRequest = {
            dismiss()
        },
        content = {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = modifier
                    .fillMaxWidth()
                    .background(AppColor.WhiteColor, shape = RoundedCornerShape(16.dp))
                    .padding(20.dp)
            ) {
                AppTextHeader(
                    text = stringResource(R.string.start_new_order),
                    fontSize = 20,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )

                Image(
                    painter = painterResource(R.drawable.img_shop_different),
                    modifier = Modifier
                        .size(150.dp)
                        .padding(top = 13.dp),
                    contentDescription = "img_shop_different"
                )
                AppTextNormal(
                    textAlign = TextAlign.Center,
                    fontSize = 14,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                    text = stringResource(R.string.adding_items)
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {

                    AppButtonPrimary(
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 10.dp),
                        text = stringResource(R.string.start), onClick = {
                            start()
                        })
                    AppButtonStroke(
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 10.dp),
                        text = stringResource(R.string.cancel), onClick = {
                            dismiss()
                        })

                }
            }
        })

}
