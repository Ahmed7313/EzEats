package com.easyEats.ezeats.ui.compose.cart

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.easyEats.ezeats.R
import com.easyEats.ezeats.domain.main.model.request.cart.response.UpdateCartResponse
import com.easyEats.ezeats.domain.main.model.response.payment.promo.ezeats.Reward
import com.easyEats.ezeats.domain.main.model.response.restaurant_reponse.RestaurantInfo
import com.easyEats.ezeats.domain.room.ShopEntity
import com.easyEats.ezeats.ui.compose.cart.summary.PaymentSummary
import com.easyEats.ezeats.ui.compose.cart.voucher.VoucherOrPromoUI
import com.easyEats.ezeats.ui.compose.components.AppButtonPrimary
import com.easyEats.ezeats.ui.compose.components.AppButtonStroke
import com.easyEats.ezeats.ui.compose.resturant.PAYMENT_INTEGRATION_TYPE
import com.easyEats.ezeats.ui.compose.theme.AppColor.BlackColor2
import com.easyEats.ezeats.ui.compose.theme.AppColor.GreyColor
import com.easyEats.ezeats.ui.main.cart.CartFragmentDirections
import com.easyEats.ezeats.ui.main.restaurant.CartViewModel
import kotlinx.coroutines.launch

@Composable
fun CartScreenUI(
    modifier: Modifier = Modifier,
    viewModel: CartViewModel,
    findNavController: NavController,
) {

    val TAG = "CartScreenUI"
    val scope = rememberCoroutineScope()
    var isEmptyCart by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(Unit) {
        viewModel.getShop()
    }
    val shopData =
        viewModel.shopDataState.collectAsState(
            ShopEntity(
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                0,
                "",
                0.0
            )
        ).value

    val cartData =
        viewModel.cartProducts.collectAsState(arrayListOf()).value

//    val updateCartResponse =
//        viewModel.updateCartResponse.collectAsState(UpdateCartResponse()).value

    var updateCartResponse by remember {
        mutableStateOf<UpdateCartResponse>(UpdateCartResponse())
    }
//        viewModel.updateCartResponse.collectAsState(UpdateCartResponse()).value

    val showLoading =
        viewModel.showLoading.collectAsState(false).value

    var promo by remember {
        mutableStateOf("")
    }
    var isSuccessPromo by remember {
        mutableStateOf<Boolean?>(null)
    }

    var rewards = remember {
        mutableStateListOf<Reward?>()
    }

    var enableTakeAction by remember {
        mutableStateOf(false)
    }

    var isAddedBefore by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(shopData?.shopId) {
        if (shopData?.shopId?.isNotEmpty() == true)
            viewModel.getCartData(shopData.shopId)
    }
    LaunchedEffect(cartData, isAddedBefore) {
        if (cartData.isEmpty() && isAddedBefore) {
            viewModel.clearShop()
            isEmptyCart = true
        }
    }

    suspend fun updateCartData(){
        enableTakeAction = false
        viewModel.updateCart().collect {
            Log.d(TAG, "CartScreenUI: start result $it")
            if (it != null) {
                Log.d(TAG, "CartScreenUI: not null")
                updateCartResponse = it
                Log.d(TAG, "CartScreenUI: start promocode here")
                if(updateCartResponse.result?.order?.promoCode?.code?.isNotEmpty() == true){
                    Log.d(TAG, "CartScreenUI: start promocode")
                    promo = updateCartResponse.result?.order?.promoCode?.code.toString()
                    isSuccessPromo = true
                }
                if (updateCartResponse.result?.tip != 0.0) {
                    viewModel.saveTip("0").collect { tip ->
                        if (tip != null) {
                            updateCartResponse = updateCartResponse.copy(
                                result = updateCartResponse.result?.copy(
                                    tip = tip.result?.data?.tipAmount,
                                    total = tip.result?.data?.finalTotal
                                )
                            )
                        }
                    }

                }
                Log.d(
                    TAG,
                    "CartScreenUI: ${updateCartResponse.result?.order?.restaurant?.paymentSystem}"
                )
                Log.d(
                    TAG,
                    "CartScreenUI: ${updateCartResponse.result?.order?.restaurant?.isPickUp}"
                )
                Log.d(
                    TAG,
                    "CartScreenUI: ${updateCartResponse.result?.order?.restaurant?.isDelivery}"
                )
                enableTakeAction = true
            } else {
                viewModel.clearShop()
//                findNavController.navigateUp()
            }
        }
    }

    LaunchedEffect(Unit) {

        viewModel.getPromoCodeEzeats().collect { response ->
            if (response != null) {
                Log.d(TAG, "PromoCode Ezeats fetched successfully")
                response.result?.rewards?.let { newRewards ->
                    rewards +=  newRewards
                }
                updateCartData()

            } else {
                Log.e(TAG, "Failed to fetch PromoCode Ezeats")
            }
        }


    }
//    LaunchedEffect(updateCartResponse) {
//        if(!updateCartResponse.result?.order?.userId.isNullOrBlank()){
//        }else{
//
//        }
//    }

    if(isEmptyCart == false) {
        LazyColumn(
            modifier = modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {
            item {
                IconButton(onClick = {
                    findNavController.navigateUp()
                }) {
                    Image(
                        painter = painterResource(R.drawable.back_arrow_icon),
                        contentDescription = "back"
                    )
                }

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
                    text = shopData?.shopName.toString(),
                    fontSize = 11.sp,
                    textAlign = TextAlign.Start,
                    fontWeight = FontWeight.Bold
                )
            }
            items(cartData) {
                CartItem(modifier = Modifier.fillMaxWidth(), it) { cartItemAction ->
                    when (cartItemAction) {
                        CartItemAction.ADD -> {
                            viewModel.updateProductQty(it.product.productId, it.product.qty + 1)
                        }

                        CartItemAction.DELETE -> {
                            isAddedBefore = true
                            viewModel.updateProductQty(it.product.productId, it.product.qty - 1)
                        }
                    }
                    scope.launch {
                        updateCartData()
                    }
                }
            }
            item {
//            CartSpecialComment(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(vertical = 20.dp)
//            )


                VoucherOrPromoUI(
                    modifier = Modifier.fillMaxWidth(),
                    rewards = rewards,
                    promo, isSuccessPromo, { it ->
                        promo = it
                    }, add = {
                        scope.launch {
                            viewModel.addPromo(promo).collect {
                                Log.d(TAG, "CartScreenUI: starting")
                                if (it != null) {
                                    isSuccessPromo = it.result?.success
                                    Log.d(TAG, "CartScreenUI: promo added successfully")
                                    if (it.result?.success == true) {
                                        scope.launch {
                                            updateCartData()
                                        }
                                    }
                                }
                                Log.d(TAG, "CartScreenUI: result")
                            }
                        }
                    }, clear = {
                        scope.launch {
                            viewModel.cancelPromoCodeReedm().collect { response ->
                                if (response != null) {
                                    promo = ""
                                    isSuccessPromo = null
                                    updateCartData()
                                }
                            }
                        }
                    }, onSelectReward = {

                    })

                HorizontalDivider(
                    modifier = Modifier.padding(vertical = 16.dp),
                    thickness = 1.dp,
                    color = BlackColor2.copy(
                        alpha = 0.5f
                    )
                )

                PaymentSummary(
                    modifier = Modifier
                        .fillMaxWidth()
                        .graphicsLayer { alpha = if (showLoading) 0.5f else 1f }
                        .blur(if (showLoading) 10.dp else 0.dp), updateCartResponse)

                Row(modifier = Modifier.fillMaxWidth()) {
                    AppButtonPrimary(
                        enabled = enableTakeAction,
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 6.dp),
                        text = stringResource(R.string.checkout)
                    ) {
                        findNavController.navigate(
                            CartFragmentDirections.actionCartFragmentToPaymentSummaryFragment(
                                updateCartResponse
                            )
                        )
                    }
                    AppButtonStroke(
                        enabled = enableTakeAction,
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 6.dp),
                        text = stringResource(R.string.add_items)
                    ) {
                        Log.d(TAG, "CartScreenUI: ${shopData?.shopId}")
                        Log.d(TAG, "CartScreenUI: ${shopData?.tableId}")
                        findNavController.navigate(
                            CartFragmentDirections.actionCartFragmentToRestaurantFragment2(
                                shopData?.shopId ?: "",
                                shopData?.tableId ?: "",
                                PAYMENTINTEGRATIONTYPE = if (updateCartResponse.result?.order?.restaurant?.paymentSystem == "paymob")
                                    PAYMENT_INTEGRATION_TYPE.PAYMOB else PAYMENT_INTEGRATION_TYPE.KASHIER,
                                RestaurantInfo(
                                    rating = 0.0,
                                    restaurantName = shopData?.shopName ?: "",
                                    reviewsCount = shopData?.reviewsCount ?: 0,
                                    distance = shopData?.distance ?: "",
                                    restaurantLogo = shopData?.logo ?: "",
                                    restaurantCover = shopData?.coverLogo ?: "",
                                )
                            )
                        )
                    }
                }
            }
        }
    }else{
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(R.drawable.img_foods),
                contentDescription = "Empty Cart",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .padding(bottom = 16.dp)
            )

            Text(
                text = stringResource(R.string.cart_is_empty),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )

            Text(
                modifier = Modifier.padding(top = 8.dp),
                text = stringResource(R.string.ready_to_order),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
            AppButtonPrimary(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                text = stringResource(R.string.goto_home)) {
                findNavController.navigateUp()
            }
        }
    }
}

