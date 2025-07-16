package com.easyEats.ezeats.ui.compose.address

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.daman.edman.screens.components.AppTextHeader
import com.daman.edman.screens.components.AppTextNormal
import com.easyEats.ezeats.R
import com.easyEats.ezeats.domain.main.model.response.address.add.AddressModel
import com.easyEats.ezeats.domain.main.model.response.address.add.SaveAddressRequest
import com.easyEats.ezeats.ui.compose.components.AppButtonPrimary
import com.easyEats.ezeats.ui.compose.components.AppButtonStroke
import com.easyEats.ezeats.ui.compose.theme.AppColor
import com.easyEats.ezeats.ui.compose.components.LoadingView
import kotlinx.coroutines.launch


@Composable
fun AddressListDialog(
    modifier: Modifier = Modifier,
    addressViewModel: AddressViewModel,
    userId: String,
    addNewAddress: () -> Unit,
    selectedAddress: () -> Unit,
    onDismiss: () -> Unit,
) {
    val TAG = "AddressListDialog"
    val sheetState = rememberModalBottomSheetState()
    var selectedaddressId by remember {
        mutableStateOf(-1)
    }
    val scope = rememberCoroutineScope()
    var addressList by remember {
        mutableStateOf<List<AddressModel>>(arrayListOf())
    }

    var isListEmpty by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(Unit) {
        addressViewModel.getAddressList(userId).collect {
            if (it != null) {
                isListEmpty = it.isEmpty()
                addressList = it.filterNotNull()
            }
        }
    }

    ModalBottomSheet(
        modifier = modifier
            .fillMaxWidth()
            .height(480.dp),
        dragHandle = {},
        sheetState = sheetState,
        shape = RoundedCornerShape(
            topStart = 16.dp,
            topEnd = 16.dp
        ),
        contentColor = AppColor.backgroundColor,
        content = {
            Box(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(vertical = 18.dp, horizontal = 28.dp)
            ) {
                LazyColumn {
                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(bottom = 10.dp),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            AppTextHeader(
                                text = stringResource(R.string.saved_address),
                                color = AppColor.BlackColor2,
                                fontSize = 20,
                                textAlign = TextAlign.Center,
                                fontWeight = FontWeight.Bold
                            )
                            Image(
                                contentDescription = "image",
                                painter = painterResource(R.drawable.img_pin),
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }

                    items(addressList) { address ->
                        ItemAddress(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 15.dp),
                            address = address
                        ) {
                            selectedaddressId = it
                        }
                    }

                    item {
                        if (isListEmpty) {
                            Column(
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier
                                    .fillMaxWidth().padding(top = 16.dp)
                                    .align(
                                        Alignment.Center
                                    )
                            ) {
                                Image(
                                    painter = painterResource(R.drawable.ic_pin),
                                    contentDescription = "pin", modifier = Modifier.size(26.dp)
                                )
                                Spacer(modifier = Modifier.height(20.dp))
                                AppTextHeader(
                                    textAlign = TextAlign.Center,
                                    text = stringResource(R.string.no_address_found),
                                    fontSize = 16,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.height(40.dp))
                            }
                        }
                    }
                    item {
                        TextButton(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(
                                    AppColor.skyColor,
                                    shape = RoundedCornerShape(100.dp)
                                ),
                            onClick = {

                            }) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(32.dp)
                                    .clickable {
                                        addNewAddress()
                                    },
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Image(
                                    painter = painterResource(R.drawable.ic_plus_circle),
                                    contentDescription = "plus_circle",
                                    modifier = Modifier.size(16.dp)
                                )
                                AppTextNormal(
                                    text = stringResource(R.string.add_new_address),
                                    color = AppColor.BlackColor2,
                                    fontSize = 16,
                                    modifier = Modifier.padding(start = 2.dp)
                                )
                            }

                        }
                    }
                    item {
                        AppButtonPrimary(
                            enabled = selectedaddressId != -1,
                            modifier = Modifier
                                .fillMaxWidth().padding(top = 16.dp),
                            text = stringResource(R.string.confirm)
                        ) {
                            if (selectedaddressId != -1) {
//                                click(selectedaddressId)
                                scope.launch {
                                    addressViewModel.saveAddressCart(
                                        SaveAddressRequest(
                                            orderId = userId,
                                            userAddressId = selectedaddressId.toString()
                                        )
                                    ).collect {
                                        if (it != null)
                                            selectedAddress()
                                    }
                                }
                            }
                        }

                    }
                }
                LoadingView(addressViewModel.showLoading.collectAsState().value)

            }
        },
        onDismissRequest = {
            onDismiss()
        }
    )
}