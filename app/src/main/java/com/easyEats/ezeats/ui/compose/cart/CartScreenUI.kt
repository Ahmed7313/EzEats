package com.easyEats.ezeats.ui.compose.cart

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.easyEats.ezeats.R
import com.easyEats.ezeats.ui.compose.cart.summary.PaymentSummary
import com.easyEats.ezeats.ui.compose.components.AppButtonPrimary
import com.easyEats.ezeats.ui.compose.components.AppButtonStroke
import com.easyEats.ezeats.ui.compose.theme.AppColor.BlackColor2
import com.easyEats.ezeats.ui.compose.theme.AppColor.GreyColor

@Composable
fun CartScreenUI(modifier: Modifier = Modifier){

    LazyColumn (
        modifier = modifier.fillMaxWidth().padding(8.dp),
        verticalArrangement = Arrangement.Top, horizontalAlignment = Alignment.Start) {
        item {
            Image(
                painter = painterResource(R.drawable.back_arrow_icon),
                contentDescription = "back"
            )
            Spacer(modifier = Modifier.height(7.dp))
            Text(
                color = BlackColor2,
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(R.string.cart),
                fontSize = 24.sp,
                textAlign = TextAlign.Start,
                fontWeight = FontWeight.Bold
            )

            Text(
                color = GreyColor,
                modifier = Modifier.fillMaxWidth(),
                text = "Nude Bakery",
                fontSize = 11.sp,
                textAlign = TextAlign.Start,
                fontWeight = FontWeight.Bold
            )
        }
        items(3) {
            CartItem(modifier = Modifier.fillMaxWidth())
        }
        item {
            CartSpecialComment(modifier = Modifier.fillMaxWidth().padding(vertical = 20.dp))


            VoucherOrPromoUI(modifier = Modifier.fillMaxWidth())


            PaymentSummary(modifier = Modifier.fillMaxWidth())

            Row(modifier = Modifier.fillMaxWidth()) {
                AppButtonPrimary(
                    modifier = Modifier.weight(1f).padding(horizontal = 6.dp),
                    text = stringResource(R.string.checkout)) { }
                AppButtonStroke(
                    modifier = Modifier.weight(1f).padding(horizontal = 6.dp),
                    text = stringResource(R.string.add_items)) { }
            }
        }

    }
}