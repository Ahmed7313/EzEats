package com.easyEats.ezeats.ui.compose.cart.paymentSummaryScreen.dialog.orderType

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
import androidx.compose.runtime.collectAsState
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
import com.easyEats.ezeats.domain.main.model.request.order.SavePickupRequest
import com.easyEats.ezeats.domain.room.ShopEntity
import com.easyEats.ezeats.ui.compose.components.AppButtonPrimary
import com.easyEats.ezeats.ui.compose.theme.AppColor
import com.easyEats.ezeats.ui.main.restaurant.CartViewModel
import kotlinx.coroutines.launch

@Composable
fun OrderTypeDialog(
    modifier: Modifier = Modifier,
    cartResponse: UpdateCartResponse,
    shop: ShopEntity,
    cartViewModel: CartViewModel,
    confirm: (ORDER_TYPE) -> Unit
) {
    var selectedPlaceOrder by remember { mutableStateOf(ORDER_TYPE.DEFAULT) }
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true,
        confirmValueChange = {
            // Prevent user from closing sheet manually
            it != SheetValue.Hidden
        }
    )
    val scope = rememberCoroutineScope()

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
                    text = stringResource(R.string.how_do_you_want_to_collect_your_order),
                    fontSize = 20,
                    fontWeight = FontWeight.Normal,
                    modifier = Modifier.padding(end = 20.dp, bottom = 20.dp)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    OrderTypeItem(
                        modifier = Modifier.weight(1f).then(
                            if (cartResponse.result?.order?.restaurant?.isDelivery == false) Modifier.alpha(0.5f) else Modifier
                        ),
                        image = painterResource(R.drawable.img_delivery),
                        contentDescription = "img_delivery",
                        label = stringResource(R.string.delivery),
                        selected = selectedPlaceOrder == ORDER_TYPE.DELIVERY
                    ){
                        if(cartResponse.result?.order?.restaurant?.isDelivery == true) selectedPlaceOrder = ORDER_TYPE.DELIVERY
                    }
                    OrderTypeItem(
                        modifier = Modifier.weight(1f).then(
                            if (cartResponse.result?.order?.restaurant?.isPickUp == false) Modifier.alpha(0.5f) else Modifier
                        ),
                        label = stringResource(R.string.pickup),
                        image = painterResource(R.drawable.img_pickup),
                        contentDescription = "img_pickup",
                        selected = selectedPlaceOrder == ORDER_TYPE.PICKUP
                    ){
                        if(cartResponse.result?.order?.restaurant?.isPickUp == true){
                            selectedPlaceOrder = ORDER_TYPE.PICKUP

                                /*
                                pickup ->
{
   "orderId": "4QxXfNMnVE",
   "pickUpStation":"Restaurant",
   "compoundName" : " from updateCart -> compoundObj -> name "
}
                                 */
//                            selectedPlaceOrder = ORDER_TYPE.PICKUP
                        }
                    }
                }

                AppButtonPrimary(text = stringResource(R.string.confirm),
                    showLoader = cartViewModel.showLoading.collectAsState(false).value,
                    modifier = Modifier.fillMaxWidth().padding(top = 20.dp)) {
                    if(selectedPlaceOrder != ORDER_TYPE.DEFAULT) {
                        scope.launch {
                            cartViewModel.savePickUpStation(SavePickupRequest(
                                orderId = cartResponse.result?.order?.objectId,
                                pickUpStation = "Restaurant",
                                compoundName = cartResponse.result?.order?.restaurant?.compound?.objectId
                            )).collect { result ->
                                if(result != null){
                                    confirm(selectedPlaceOrder)
                                }
                            }
                        }

                    }
                }

            }
        },
        onDismissRequest = {}
    )

}
//Lagenak Habes (Muhammed Felfel Remix Official Music Video) | لاقيناك حابس (ريميكس محمد فلفل كليب)



@Composable
fun OrderTypeItem(
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
                    if (selected)  AppColor.ColorPrimaryOpacity else AppColor.ColorPrimaryOpacity2,
                    shape = CircleShape
                )
                .clip(CircleShape).clickable {
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