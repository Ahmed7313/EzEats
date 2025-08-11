package com.easyEats.ezeats.ui.compose.cart.paymentSummaryScreen.dialog

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetValue
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.daman.edman.screens.components.AppTextHeader
import com.daman.edman.screens.components.AppTextNormal
import com.easyEats.ezeats.R
import com.easyEats.ezeats.domain.main.model.request.cart.response.UpdateCartResponse
import com.easyEats.ezeats.ui.compose.components.AppButtonPrimary
import com.easyEats.ezeats.ui.compose.theme.AppColor
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

@Composable
fun PlaceOrderDialog(
    modifier: Modifier = Modifier,
    updateCartResponse: UpdateCartResponse,
    confirm: (PLACE_ORDER_TYPE) -> Unit,
    dismiss: () -> Unit,
) {
    var selectedPlaceOrder by remember { mutableStateOf(PLACE_ORDER_TYPE.DEFAULT) }
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true,
        confirmValueChange = { true }
    )
    val coroutineScope = rememberCoroutineScope()


    BackHandler(enabled = sheetState.isVisible) {
        Log.d("PlaceOrderDialog", "PlaceOrderDialog: BACK")
        coroutineScope.launch {
            sheetState.show()
        }
    }

    ModalBottomSheet(
        modifier = modifier
            .fillMaxWidth()
            .height(324.dp),
        dragHandle = {},
        sheetState = sheetState,
        shape = RoundedCornerShape(
            topStart = 16.dp,
            topEnd = 16.dp
        ),
        contentColor = AppColor.backgroundColor,
        content = {
            Column(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(vertical = 24.dp, horizontal = 29.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                AppTextHeader(
                    text = stringResource(R.string.where_do_you_want_order_your_order),
                    fontSize = 20,
                    fontWeight = FontWeight.Normal,
                    modifier = Modifier.padding(end = 20.dp, bottom = 20.dp)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    PlaceOrderImage(
                        modifier = Modifier
                            .weight(1f)
                            .alpha(
                                if (updateCartResponse.result?.order?.restaurant?.isUmbrellaDelivery == true)
                                    1f else 0.5f
                            ),
                        image = painterResource(R.drawable.ic_umberlla),
                        contentDescription = "ic_umberlla",
                        label = stringResource(R.string.umbrella),
                        selected = selectedPlaceOrder == PLACE_ORDER_TYPE.UMBERLLA
                    ){
                        if (updateCartResponse.result?.order?.restaurant?.isUmbrellaDelivery == true) selectedPlaceOrder = PLACE_ORDER_TYPE.UMBERLLA
                    }
                    PlaceOrderImage(
                        modifier = Modifier.weight(1f),
                        label = stringResource(R.string.address),
                        image = painterResource(R.drawable.ic_address),
                        contentDescription = "ic_address",
                        selected = selectedPlaceOrder == PLACE_ORDER_TYPE.ADDRESS
                    ){
                        selectedPlaceOrder = PLACE_ORDER_TYPE.ADDRESS

                    }
                    PlaceOrderImage(
                        modifier = Modifier
                            .weight(1f)
                            .alpha(0.5f),
                        label = stringResource(R.string.office),
                        image = painterResource(R.drawable.ic_office),
                        contentDescription = "ic_office",
                        selected = selectedPlaceOrder == PLACE_ORDER_TYPE.OFFICE
                    ){
//                        selectedPlaceOrder = PLACE_ORDER_TYPE.OFFICE
                    }
                }

                AppButtonPrimary(
                    enabled = selectedPlaceOrder != PLACE_ORDER_TYPE.DEFAULT,
                    text = stringResource(R.string.confirm), modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 20.dp)) {
                    if(selectedPlaceOrder != PLACE_ORDER_TYPE.DEFAULT) confirm(selectedPlaceOrder)
                }


            }
        },
        onDismissRequest = { dismiss() }
    )
}

@Composable
fun PlaceOrderImage(
    modifier: Modifier = Modifier,
    image: Painter,
    label : String,
    contentDescription: String,
    selected: Boolean,
    onSelect: () -> Unit = {}
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .padding(horizontal = 8.dp)
                .size(100.dp)
                .background(
                    if (selected) AppColor.ColorPrimaryOpacity else AppColor.ColorPrimaryOpacity2,
                    shape = CircleShape
                )
                .clip(CircleShape)
                .clickable {
                    onSelect()
                }
        ) {
            Image(
                painter = image,
                contentDescription = contentDescription,
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .size(80.dp) // size of the image inside the circle
//                .clip(CircleShape)
            )
        }

        AppTextNormal(
            color = if (selected) AppColor.ColorPrimary else AppColor.BlackColor2,
            text = label, fontSize = 16, fontWeight = FontWeight.Normal, modifier = Modifier.padding(top = 10.dp))
    }

}