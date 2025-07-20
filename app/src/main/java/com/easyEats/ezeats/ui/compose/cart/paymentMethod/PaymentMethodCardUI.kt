package com.easyEats.ezeats.ui.compose.cart.paymentMethod

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.easyEats.ezeats.R
import com.easyEats.ezeats.domain.main.model.response.payment.savedCard.response.Card


@Composable
fun PaymentMethodCardUI(
    modifier: Modifier = Modifier,
    paymentMethods: List<Card>,
    selectedPaymentMethod: PaymentMethodModel,
    select: (PaymentMethodModel) -> Unit,
) {

    val context = LocalContext.current


    val paymentMethodNewCard = PaymentMethodModel(
        text = context.getString(R.string.add_card),
        paymentMethod = PAYMENT_METHOD.CARD,
        isVector = true,
        painter = painterResource(id = R.drawable.ic_plus)
    )


    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        paymentMethods.forEach { card ->
            val paymentMethodModel = PaymentMethodModel(
                text = "XXXX-${card.last4Digits}",
                isVector = true,
                paymentMethod = PAYMENT_METHOD.CARD,
                painter = painterResource(id = R.drawable.ic_visa),
                key = card.objectID ?: ""
            )
            PaymentMethodItem(
                modifier = Modifier.fillMaxWidth(),
                paymentMethodModel = paymentMethodModel,
                isSelected = selectedPaymentMethod.key == card.objectID.toString()
            ) {
                select(paymentMethodModel)
            }
        }
        PaymentMethodItem(
            modifier = Modifier.fillMaxWidth(),
            paymentMethodNewCard,
            isSelected = selectedPaymentMethod.key == "",
        ) {
            select(it)
        }
    }

}

@Preview
@Composable
fun PaymentMethodCardUIPreview() {
//    PaymentMethodUI()
}

