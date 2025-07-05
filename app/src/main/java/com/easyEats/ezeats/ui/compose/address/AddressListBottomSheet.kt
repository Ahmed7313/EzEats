package com.easyEats.ezeats.ui.compose.address

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.daman.edman.screens.components.HeaderText
import com.daman.edman.screens.components.NormalText
import com.easyEats.ezeats.R
import com.easyEats.ezeats.ui.compose.components.AppButtonPrimary
import com.easyEats.ezeats.ui.compose.theme.AppColor
import com.easyEats.ezeats.ui.main.restaurant.CartViewModel
import com.trend.thecontent.screens.components.LoadingView


@Composable
fun CompoundBottomSheet(
    modifier: Modifier = Modifier,
    cartViewModel: CartViewModel,
    addressId: Int = -1,
    click: (Int) -> Unit,
    onDismiss: () -> Unit,
) {
    val TAG = "CompoundBottomSheet"
    val sheetState = rememberModalBottomSheetState()
    var selectedaddressId by remember {
        mutableStateOf(addressId)
    }
    var showLoading by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(Unit) {
        cartViewModel.getAddressList()
    }


    ModalBottomSheet(
        modifier = modifier
            .fillMaxWidth()
            .height(412.dp),
        dragHandle = {},
        sheetState = sheetState,
        shape = RoundedCornerShape(
            topStart = 16.dp,
            topEnd = 16.dp
        ),
        contentColor = AppColor.backgroundColor,
        content = {
            Box(modifier = modifier.fillMaxWidth()) {
                LazyColumn {
                    item {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            HeaderText(
                                text = stringResource(R.string.saved_address),
                                color = AppColor.BlackColor2,
                                fontSize = 20,
                                fontWeight = FontWeight.Bold
                            )
                            Image(
                                contentDescription = "image",
                                painter = painterResource(R.drawable.img_pin),
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                    items(3) {
                        ItemAddress(modifier = Modifier.fillMaxWidth()) {
                            selectedaddressId = it
                        }
                    }

                    item {
                        Spacer(modifier = Modifier.padding(top = 15.dp))
                        TextButton(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(
                                    AppColor.backgroundColor2,
                                    shape = RoundedCornerShape(5.dp)
                                )
                                .padding(8.dp),
                            onClick = {

                            }) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Image(
                                    painter = painterResource(R.drawable.ic_plus_circle),
                                    contentDescription = "plus_circle"
                                )
                                NormalText(
                                    text = stringResource(R.string.add_new_address),
                                    color = AppColor.BlackColor2,
                                    fontSize = 16,
                                    modifier = Modifier.padding(start = 2.dp)
                                )
                            }
                        }
                    }
                    item {
                        AppButtonPrimary(text = stringResource(R.string.confirm)) {
                            if(addressId != -1)
                                click(selectedaddressId)
                        }
                    }
                }
                LoadingView(showLoading)
            }
        },
        onDismissRequest = {
            onDismiss()
        }
    )
}