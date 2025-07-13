package com.easyEats.ezeats.ui.compose.address

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.daman.edman.screens.components.AppTextHeader
import com.easyEats.ezeats.R
import com.easyEats.ezeats.domain.main.model.request.cart.response.UpdateCartResponse
import com.easyEats.ezeats.domain.main.model.response.address.add.AddAddressRequest
import com.easyEats.ezeats.domain.main.model.response.address.add.AddAddressResponse
import com.easyEats.ezeats.ui.compose.components.AppButtonPrimary
import com.easyEats.ezeats.ui.compose.components.AppInputTextLabelTop
import com.easyEats.ezeats.ui.compose.theme.AppColor
import com.easyEats.ezeats.ui.compose.components.LoadingView
import kotlinx.coroutines.launch


@Composable
fun AddAddressDialog(
    modifier: Modifier = Modifier,
    cartResponse: UpdateCartResponse,
    addressViewModel: AddressViewModel,
    onDismiss: () -> Unit,
    onResult: (AddAddressResponse) -> Unit
) {
    val TAG = "AddAddressDialog"
    val sheetState = rememberModalBottomSheetState()

    var addressLabel by remember {
        mutableStateOf("")
    }

    var streetName by remember {
        mutableStateOf("")
    }

    var buildingNo by remember {
        mutableStateOf("")
    }

    var apartementNo by remember {
        mutableStateOf("")
    }

    var floor by remember {
        mutableStateOf("")
    }

    val scope = rememberCoroutineScope()

    val context = LocalContext.current

    ModalBottomSheet(
        modifier = modifier
            .fillMaxWidth()
            .height(570.dp),
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
                    .padding(horizontal = 31.dp, vertical = 23.dp)
            ) {
                Column {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Start,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 15.dp)
                    ) {
                        AppTextHeader(
                            text = stringResource(R.string.please_enter_your_address),
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

                    AppInputTextLabelTop(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 25.dp),
                        value = addressLabel,
                        label = stringResource(R.string.address_label),
                        hint = "${stringResource(R.string.enter)} ${stringResource(R.string.address_label)}"
                    ) {
                        addressLabel = it
                    }

                    AppInputTextLabelTop(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 25.dp),
                        value = cartResponse.result?.order?.restaurant?.compound?.name!!,
                        label = stringResource(R.string.select_compound),
                        hint = "${stringResource(R.string.enter)} ${stringResource(R.string.compound)}"
                    ) {
//                        addressLabel = it
                    }
                    //DropDownCompound
                    AppInputTextLabelTop(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 25.dp),
                        value = streetName,
                        label = stringResource(R.string.street_name),
                        hint = "${stringResource(R.string.enter)} ${stringResource(R.string.street_name)}"
                    ) {
                        streetName = it
                    }
                    AppInputTextLabelTop(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 25.dp),
                        value = buildingNo,
                        label = stringResource(R.string.building_number),
                        hint = "${stringResource(R.string.enter)} ${stringResource(R.string.building_number)}"
                    ) {
                        buildingNo = it
                    }
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 20.dp)
                    ) {
                        AppInputTextLabelTop(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f)
                                .padding(end = 6.dp),
                            value = apartementNo,
                            label = stringResource(R.string.apartement_no),
                            hint = "${stringResource(R.string.enter)} ${stringResource(R.string.apartement_no)}"
                        ) {
                            apartementNo = it
                        }
                        AppInputTextLabelTop(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f)
                                .padding(start = 6.dp),
                            value = floor,
                            label = stringResource(R.string.apartement_no),
                            hint = "${stringResource(R.string.enter)} ${stringResource(R.string.floor)}"
                        ) {
                            floor = it
                        }
                    }
                    AppButtonPrimary(
                        text = stringResource(R.string.confirm),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        if (addressLabel.trim().isEmpty()) {
                            Toast.makeText(
                                context,
                                "${context.getText(R.string.enter)} ${context.getText(R.string.address_label)}",
                                Toast.LENGTH_SHORT
                            ).show()
                            return@AppButtonPrimary
                        }
                        if (streetName.trim().isEmpty()) {
                            Toast.makeText(
                                context,
                                "${context.getText(R.string.enter)} ${context.getText(R.string.street_name)}",
                                Toast.LENGTH_SHORT
                            ).show()
                            return@AppButtonPrimary
                        }
                        if (buildingNo.trim().isEmpty()) {
                            Toast.makeText(
                                context,
                                "${context.getText(R.string.enter)} ${context.getText(R.string.building_number)}",
                                Toast.LENGTH_SHORT
                            ).show()
                            return@AppButtonPrimary
                        }
                        if (apartementNo.trim().isEmpty()) {
                            Toast.makeText(
                                context,
                                "${context.getText(R.string.enter)} ${context.getText(R.string.apartement_no)}",
                                Toast.LENGTH_SHORT
                            ).show()
                            return@AppButtonPrimary
                        }
                        if (floor.trim().isEmpty()) {
                            Toast.makeText(
                                context,
                                "${context.getText(R.string.enter)} ${context.getText(R.string.floor)}",
                                Toast.LENGTH_SHORT
                            ).show()
                            return@AppButtonPrimary
                        }
                        scope.launch {
                            addressViewModel.addAddress(
                                AddAddressRequest(
                                    addressLabel = addressLabel,
                                    streetName = streetName,
                                    buildingNumber = buildingNo,
                                    apartmentNumber = apartementNo,
                                    floorNumber = floor
                                )
                            ).collect {
                                if(it != null){
                                    onResult(it)
                                }
                            }
                        }
                    }
                }
                LoadingView(addressViewModel.showLoadingAdd.collectAsState(false).value)
            }
        },
        onDismissRequest = {
            onDismiss()
        }
    )
}