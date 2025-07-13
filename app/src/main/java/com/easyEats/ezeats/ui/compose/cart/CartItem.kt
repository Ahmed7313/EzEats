package com.easyEats.ezeats.ui.compose.cart

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.daman.edman.screens.components.AppTextHeader
import com.easyEats.ezeats.R
import com.easyEats.ezeats.domain.room.ProductWithChoices
import com.easyEats.ezeats.ui.compose.components.AppImage
import com.easyEats.ezeats.ui.compose.theme.AppColor
import com.easyEats.ezeats.ui.compose.theme.AppColor.BlackColor2

@Composable
fun CartItem(
    modifier: Modifier = Modifier,
    productWithChoices: ProductWithChoices,
    allowAction: Boolean = true,
    click: (CartItemAction) -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.Top, horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = productWithChoices.product.name,
                color = BlackColor2,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
            if (productWithChoices.choices.isNotEmpty()) {
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = productWithChoices.choices.joinToString(", ") { it.choiceName },
                    color = BlackColor2,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium
                )
            }
            ProductTotalPriceText(productWithChoices)
//            Spacer(modifier = Modifier.height(6.dp))
//            Row {
//                Icon(painter = painterResource(R.drawable.edit_icon), tint = ColorPrimary, contentDescription = "editColor")
//                Text(
//                    text = stringResource(R.string.edit),
//                    color = ColorPrimary,
//                    fontSize = 11.sp
//                )
//            }


        }

        Box(modifier = Modifier.size(100.dp)) {
            AppImage(
                modifier = Modifier.size(100.dp),
                imageUrl = productWithChoices.product.imageUrl.toString(),
                size = 100,
                cornerRadius = 10,
                backgroundColor = AppColor.ColorPrimary
            )

            if(allowAction)
                Box(
                    modifier = Modifier
                        .padding(horizontal = 4.dp, vertical = 4.dp)
                        .align(Alignment.BottomCenter)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                AppColor.WhiteColor.copy(alpha = 0.7f),
                                shape = RoundedCornerShape(100.dp)
                            )
                            .padding(horizontal = 5.dp, vertical = 2.dp)
                            .align(Alignment.BottomCenter),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Image(
                            modifier = Modifier.clickable {
                                click(CartItemAction.DELETE)
                            },
                            painter = if (productWithChoices.product.qty == 1) painterResource(R.drawable.delete_account_icon) else painterResource(
                                R.drawable.ic_minus
                            ),
                            contentDescription = "minus"
                        )
                        AppTextHeader(
                            text = productWithChoices.product.qty.toString(),
                            fontSize = 15,
                            color = AppColor.BlackColor2
                        )
                        Image(
                            modifier = Modifier.clickable {
                                click(CartItemAction.ADD)
                            },
                            painter = painterResource(R.drawable.ic_plus), contentDescription = "add"
                        )

                    }
                }

        }

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

@Composable
fun ProductTotalPriceText(productWithChoices: ProductWithChoices) {
    val totalPrice =
        (productWithChoices.product.price * productWithChoices.product.qty) + productWithChoices.choices.sumOf { it.choicePrice }


    Spacer(modifier = Modifier.height(14.dp))
    Text(
        text = "${"%.2f".format(totalPrice)} ${stringResource(R.string.currency)}",
        color = BlackColor2,
        fontSize = 13.sp,
        fontWeight = FontWeight.Bold
    )

}