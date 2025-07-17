package com.easyEats.ezeats.ui.compose.address

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.daman.edman.screens.components.AppTextNormal
import com.easyEats.ezeats.R
import com.easyEats.ezeats.domain.main.model.response.address.add.AddressModel
import com.easyEats.ezeats.ui.compose.theme.AppColor


@Composable
fun ItemAddress(
    modifier: Modifier = Modifier,
    address: AddressModel,
    isSelected: Boolean = false,
    selectedAddressId: (String) -> Unit
) {

    Column(
        modifier = modifier
            .fillMaxWidth().then(if(isSelected) Modifier.border(
                BorderStroke(1.dp, AppColor.ColorPrimary),
                shape = RoundedCornerShape(5.dp)
            ) else Modifier)
            .background(AppColor.backgroundColor2, shape = RoundedCornerShape(5.dp)).clickable{
                selectedAddressId(address.objectId.toString())
            },
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 9.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start,
        ) {
            Image(
                painter = painterResource(R.drawable.ic_pin),
                modifier = Modifier.padding( end = 5.dp, top = 11.dp),
                contentDescription = "pin"
            )
            AppTextNormal(
                text = address.addressLabel.toString(),
                fontSize = 16,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(start = 2.dp, top = 11.dp)
            )

        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            AppTextNormal(
                text = "${address.streetName} , ${address.buildingNumber}, ${address.floorNumber}, ${address.apartmentNumber}",
                textAlign = TextAlign.Start,
                modifier = Modifier
                    .padding(
                        start = 9.dp,
                        bottom = 9.dp, top = 5.dp
                    ),
                fontWeight = FontWeight.Normal, fontSize = 12
            )
            Spacer(modifier = Modifier.weight(1f))

            RadioButton(
                modifier = Modifier,
                colors = RadioButtonDefaults.colors(
                    selectedColor = AppColor.ColorPrimary,
                    unselectedColor = AppColor.BlackColor,
                ),
                onClick = {
                    selectedAddressId(address.objectId.toString())
                }, selected = isSelected
            )

        }

    }
}

//@Preview
//@Composable
//fun ItemAddressPreview() {
//    ItemAddress(address = AddressModel(), isSelected = false){
//
//    }
//}

