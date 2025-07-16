package com.easyEats.ezeats.ui.compose.cart.paymentMethod

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.easyEats.ezeats.R


@Composable
fun PaymentMethodUI(
    modifier: Modifier = Modifier,
    selectedPaymentMethod: PaymentMethodModel,
    select: (PaymentMethodModel) -> Unit
) {

    val context = LocalContext.current

    val paymentMethods = listOf(
        PaymentMethodModel(
            text = context.getString(R.string.visa),
            paymentMethod = PAYMENT_METHOD.CARD,
            painter = painterResource(id = R.drawable.credit_card_icon)
        ),
        PaymentMethodModel(
            text = context.getString(R.string.wallet),
            paymentMethod = PAYMENT_METHOD.WALLET,
            painter = painterResource(id = R.drawable.ic_wallet)
        ),
        PaymentMethodModel(
            text = "",
            paymentMethod = PAYMENT_METHOD.VALU,
            painter = painterResource(id = R.drawable.img_valu),
            isVector = false
        )
    )


    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        PaymentMethodItem(
            modifier = Modifier.fillMaxWidth(),
            paymentMethods[0],
            paymentMethods[0] == selectedPaymentMethod
        ) {
            select(paymentMethods[0])

        }
        PaymentMethodItem(
            modifier = Modifier.fillMaxWidth(),
            paymentMethods[1],
            paymentMethods[1] == selectedPaymentMethod
        ) {
            select(paymentMethods[1])

        }
        PaymentMethodItem(
            modifier = Modifier.fillMaxWidth(),
            paymentMethods[2],
            paymentMethods[2] == selectedPaymentMethod
        ) {
            select(paymentMethods[2])
        }

    }

}

@Preview
@Composable
fun PaymentMethodUIPreview() {
//    PaymentMethodUI()
}

