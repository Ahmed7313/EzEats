package com.easyEats.ezeats.ui.compose.address

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.RadioButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.daman.edman.screens.components.AppTextNormal
import com.easyEats.ezeats.R
import com.easyEats.ezeats.domain.main.model.response.address.add.AddressModel
import com.easyEats.ezeats.ui.compose.theme.AppColor


@Composable
fun ItemAddress(modifier: Modifier = Modifier,address: AddressModel,selectedAddressId: (Int) -> Unit) {

    Column(
        modifier = modifier
            .fillMaxWidth()
            .border(
                BorderStroke(1.dp, AppColor.ColorPrimary),
                shape = RoundedCornerShape(5.dp)
            )
            .background(AppColor.backgroundColor2, shape = RoundedCornerShape(5.dp)),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
        ) {
//            Image(
//                painter = painterResource(R.drawable.ic_pin),
//                modifier = Modifier.padding(start = 9.dp, end = 5.dp, top = 11.dp),
//                contentDescription = "pin"
//            )
//            RadioButton(selected = address.)
            AppTextNormal(
                text = address.addressLabel.toString(),
                fontSize = 16,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(start = 2.dp, top = 11.dp)
            )
            Spacer(modifier = Modifier.weight(1f))
            AppTextNormal(
                text = stringResource(R.string.selected),
                fontSize = 12,
                color = AppColor.WhiteColor,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .background(
                        AppColor.ColorPrimary,
                        shape = RoundedCornerShape(bottomStart = 1000.dp, topEnd = 5.dp)
                    )
                    .padding(start = 20.dp, end = 10.dp, top = 8.dp, bottom = 8.dp)
            )
        }
        AppTextNormal(
            text = "${address.streetName} , ${address.buildingNumber}, ${address.floorNumber}, ${address.apartmentNumber}",
            textAlign = TextAlign.Start,
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = 9.dp,
                    bottom = 9.dp, top = 5.dp
                ),
            fontWeight = FontWeight.Normal, fontSize = 12
        )
    }
}

//@Preview
//@Composable
//fun ItemAddressPreview() {
//    ItemAddress()
//}