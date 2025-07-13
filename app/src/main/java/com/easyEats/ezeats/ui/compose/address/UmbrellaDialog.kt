package com.easyEats.ezeats.ui.compose.address

import android.widget.Toast
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.daman.edman.screens.components.AppTextHeader
import com.daman.edman.screens.components.AppTextNormal
import com.easyEats.ezeats.R
import com.easyEats.ezeats.domain.main.model.request.cart.response.UpdateCartResponse
import com.easyEats.ezeats.domain.main.model.request.order.SavePickupRequest
import com.easyEats.ezeats.ui.compose.components.AppButtonPrimary
import com.easyEats.ezeats.ui.compose.components.AppInputField
import com.easyEats.ezeats.ui.compose.components.AppInputTextLabelTop
import com.easyEats.ezeats.ui.compose.theme.AppColor
import com.easyEats.ezeats.ui.main.restaurant.CartViewModel
import com.easyEats.ezeats.ui.compose.components.LoadingView
import kotlinx.coroutines.launch


@Composable
fun UmbrellaDialog(
    modifier: Modifier = Modifier,
    addressViewModel: AddressViewModel,
    cartViewModel: CartViewModel,
    cartResponse: UpdateCartResponse,
    onDismiss: () -> Unit,
) {
    val TAG = "UmbrellaDialog"
    val sheetState = rememberModalBottomSheetState()

    val scope = rememberCoroutineScope()

    val context = LocalContext.current

    var umbrellaNo by remember {
        mutableStateOf("")
    }


    ModalBottomSheet(
        modifier = modifier
            .fillMaxWidth(),
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
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Start,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 15.dp)
                    ) {
                        AppTextHeader(
                            text = stringResource(R.string.confirm_new_address),
                            color = AppColor.BlackColor2,
                            fontSize = 20,
                            fontWeight = FontWeight.Bold
                        )
                    }


                    AppInputTextLabelTop(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 25.dp),
                        value = cartResponse.result?.order?.restaurant?.compound?.name!!,
                        label = stringResource(R.string.select_your_compound),
                        hint = "${stringResource(R.string.enter)} ${stringResource(R.string.compound)}"
                    ) {

                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Row(
                            Modifier
                                .padding(start = 20.dp)
                                .background(
                                    AppColor.backgroundColor2, shape = RoundedCornerShape(30.dp)
                                ).height(54.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Image(
                                painter = painterResource(R.drawable.img_umberlla),
                                modifier = Modifier.size(20.dp),
                                contentDescription = "umberlla"
                            )
                            Spacer(modifier = Modifier.size(3.dp))
                            AppTextNormal(
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 10.dp),
                                fontSize = 16,
                                text = "${stringResource(R.string.umbrella)} - "
                            )
                        }

                        Spacer(modifier = Modifier.width(10.dp))

                        AppInputField(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f),
                            color = AppColor.backgroundColor2,
                            text = umbrellaNo,
                            keyboardType = KeyboardType.Number,
                            hint = stringResource(R.string.enter_your),
                        ) {
                            umbrellaNo = it
                        }
                    }



                    AppButtonPrimary(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp),
                        text = stringResource(R.string.confirm)
                    ) {
                        if (umbrellaNo.trim().isEmpty()) {
                            Toast.makeText(
                                context,
                                "${context.getText(R.string.enter)} ${context.getText(R.string.umbrella_no)}",
                                Toast.LENGTH_SHORT
                            ).show()
                            return@AppButtonPrimary
                        }
                        scope.launch {
                            cartViewModel.savePickUpStation(
                                SavePickupRequest(
                                    orderId = cartResponse.result?.order?.objectId,
                                    pickUpStation = "umberall-$umbrellaNo",
                                    compoundName = cartResponse.result?.order?.restaurant?.compound?.objectId
                                )
                            ).collect { result ->
                                if (result != null) {
                                    onDismiss()
                                }
                            }
                        }
                    }
                }
//                LoadingView(addressViewModel.showLoading.collectAsState().value)
            }
        },
        onDismissRequest = {
            onDismiss()
        }
    )
}

@Preview(showBackground = true)
@Composable
fun UmbrellaDialogContentPreview() {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
//            .height(412.dp)
            .background(AppColor.backgroundColor),
        shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
    ) {
        UmbrellaDialogContent(
            umbrellaNo = "",
            onUmbrellaNoChanged = {},
            onConfirmClicked = {},
        )
    }
}

@Composable
private fun UmbrellaDialogContent(
    umbrellaNo: String,
    onUmbrellaNoChanged: (String) -> Unit,
    onConfirmClicked: () -> Unit
) {
    val context = LocalContext.current
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 18.dp, horizontal = 28.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        AppInputTextLabelTop(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 25.dp),
            label = stringResource(R.string.select_your_compound),
            hint = "${stringResource(R.string.enter)} ${stringResource(R.string.compound)}"
        ) {}

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Row(
                Modifier
                    .padding(start = 20.dp)
                    .background(
                        AppColor.backgroundColor2, shape = RoundedCornerShape(30.dp)
                    ).height(54.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Image(
                    painter = painterResource(R.drawable.img_umberlla),
                    modifier = Modifier.size(20.dp),
                    contentDescription = "umberlla"
                )
                Spacer(modifier = Modifier.size(3.dp))
                AppTextNormal(
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 10.dp),
                    fontSize = 16,
                    text = "${stringResource(R.string.umbrella)} - "
                )
            }

            Spacer(modifier = Modifier.width(10.dp))

            AppInputField(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                color = AppColor.backgroundColor2,
                text = umbrellaNo,
                keyboardType = KeyboardType.Number,
                hint = stringResource(R.string.enter_your),
            ) {
                onUmbrellaNoChanged(it)
            }
        }

        AppButtonPrimary(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            text = stringResource(R.string.confirm)
        ) {
            if (umbrellaNo.trim().isEmpty()) {
                Toast.makeText(
                    context,
                    "${context.getText(R.string.enter)} ${context.getText(R.string.umbrella_no)}",
                    Toast.LENGTH_SHORT
                ).show()
                return@AppButtonPrimary
            }
            onConfirmClicked()
        }
    }
}