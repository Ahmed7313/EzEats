package com.easyEats.ezeats.ui.compose.cart.paymentSummaryScreen

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.easyEats.ezeats.R
import com.easyEats.ezeats.domain.main.model.request.cart.response.UpdateCartResponse
import com.easyEats.ezeats.domain.main.model.request.cart.tip.TipResponse
import com.easyEats.ezeats.domain.main.model.response.user_info.UserInfoResponse
import com.easyEats.ezeats.domain.room.ShopEntity
import com.easyEats.ezeats.ui.compose.address.AddAddressDialog
import com.easyEats.ezeats.ui.compose.address.AddressListDialog
import com.easyEats.ezeats.ui.compose.address.AddressViewModel
import com.easyEats.ezeats.ui.compose.address.UmbrellaDialog
import com.easyEats.ezeats.ui.compose.address.UpdateProfileDialog
import com.easyEats.ezeats.ui.compose.cart.CartItem
import com.easyEats.ezeats.ui.compose.cart.paymentSummaryScreen.dialog.PLACE_ORDER_TYPE
import com.easyEats.ezeats.ui.compose.cart.paymentSummaryScreen.dialog.PlaceOrderDialog
import com.easyEats.ezeats.ui.compose.cart.paymentSummaryScreen.dialog.orderType.ORDER_TYPE
import com.easyEats.ezeats.ui.compose.cart.paymentSummaryScreen.dialog.orderType.OrderTypeDialog
import com.easyEats.ezeats.ui.compose.cart.paymentSummaryScreen.dummy.TipOption
import com.easyEats.ezeats.ui.compose.cart.summary.PaymentSummary
import com.easyEats.ezeats.ui.compose.components.AppButtonPrimary
import com.easyEats.ezeats.ui.compose.map.MapDialog
import com.easyEats.ezeats.ui.compose.map.MapViewModel
import com.easyEats.ezeats.ui.compose.theme.AppColor
import com.easyEats.ezeats.ui.compose.theme.AppColor.BlackColor2
import com.easyEats.ezeats.ui.main.restaurant.CartViewModel
import com.easyEats.ezeats.ui.compose.components.LoadingView
import com.easyEats.ezeats.ui.main.edit_profile.EditProfileViewModel
import com.easyEats.ezeats.ui.main.profile.ProfileViewModel
import com.easyEats.ezeats.utils.Resource
import kotlinx.coroutines.launch

@Composable
fun PaymentSummaryScreenUI(
    modifier: Modifier = Modifier,
    navigate: NavController,
    viewModel: CartViewModel,
    mapViewModel: MapViewModel,
    addressViewModel: AddressViewModel,
    profileViewModel: ProfileViewModel,
    editProfileViewModel: EditProfileViewModel,
    updateCartResponse: UpdateCartResponse
) {


    val TAG = "PaymentSummaryScreenUI"
    var cartResponse by remember {
        mutableStateOf(updateCartResponse)
    }
    var tipId by remember {
        mutableStateOf(-1)
    }
    var placeOrderType by remember {
        mutableStateOf(PLACE_ORDER_TYPE.DEFAULT)
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

    var showPlaceOrder by remember {
        mutableStateOf(false)
    }
    var orderTypeDialog by remember {
        mutableStateOf(true)
    }
    var showAddressList by remember {
        mutableStateOf(false)
    }
    var showUmbrellaDialog by remember {
        mutableStateOf(false)
    }
    var lat by remember {
        mutableStateOf(0.0)
    }
    var lng by remember {
        mutableStateOf(0.0)
    }
    var address by remember {
        mutableStateOf("")
    }
    var orderTypeSelected by remember {
        mutableStateOf(ORDER_TYPE.DEFAULT)
    }


    var addNewAddress by remember {
        mutableStateOf(false)
    }
    var addNewAddressForm by remember {
        mutableStateOf(false)
    }
    var showMap by remember {
        mutableStateOf(false)
    }

    var showEditProfile by remember {
        mutableStateOf(false)
    }


    val tipOptions = remember {
        listOf(
            TipOption("1", "10", "5%"),
            TipOption("2", "15", "5%"),
            TipOption("2", "20", "5%"),
//            TipOption("3", null, null, isCustom = true)
        )
    }

    var showLoading by remember {
        mutableStateOf(false)
    }

    var selectedTipOption by remember { mutableStateOf<TipOption?>(null) }

    var tipResponse by remember {
        mutableStateOf<TipResponse>(TipResponse())
    }
    val userInfoResult = profileViewModel.userResultLiveData.observeAsState().value
    var userInfo by remember {
        mutableStateOf(UserInfoResponse())
    }

    LaunchedEffect(userInfoResult) {
        if (userInfoResult is Resource.Success) {
            viewModel._showLoading.value = false

            // Handle success case
//            val userInfo = userInfoResult.data
            // Do something with userInfo
            userInfo = userInfoResult.data?.result?.data ?: UserInfoResponse()
            if(userInfo.email.isNullOrEmpty() || userInfo.name.isNullOrEmpty())
                showEditProfile = true
            else{
                Log.d(TAG, "PaymentSummaryScreenUI: Call WebBrowser Payment")
            }
        }else if(userInfoResult is Resource.Loading){
            viewModel._showLoading.value = true
        }else{
            viewModel._showLoading.value = false
        }
    }


    val scope = rememberCoroutineScope()
    LaunchedEffect(tipResponse) {
        viewModel.checkout()
    }
    LaunchedEffect(Unit) {
        viewModel.getShop()
    }
    Box {
        LazyColumn(
            modifier = modifier
                .align(Alignment.Center)
                .fillMaxWidth()
                .padding(8.dp),
            verticalArrangement = Arrangement.Top, horizontalAlignment = Alignment.Start
        ) {
            item {
                Image(
                    painter = painterResource(R.drawable.back_arrow_icon),
                    modifier = Modifier.clickable {
                        navigate.popBackStack()
                    },
                    contentDescription = "back"
                )
                Spacer(modifier = Modifier.height(7.dp))
                Text(
                    color = BlackColor2,
                    modifier = Modifier.fillMaxWidth(),
                    text = stringResource(R.string.payment_summary),
                    fontSize = 24.sp,
                    textAlign = TextAlign.Start,
                    fontWeight = FontWeight.Bold
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start
                ) {

                    Image(
                        painter = painterResource(R.drawable.ic_bank_note),
                        contentDescription = "bank_note",
                        modifier = Modifier.size(18.dp)
                    )

                    Text(
                        color = AppColor.BlackColor,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 6.dp, top = 14.dp, bottom = 10.dp),
                        text = stringResource(R.string.add_tip),
                        fontSize = 15.sp,
                        textAlign = TextAlign.Start,
                        fontWeight = FontWeight.Bold
                    )
                }

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(
                            BorderStroke(1.dp, AppColor.BlackColor),
                            shape = RoundedCornerShape(10.dp)
                        )
                        .background(AppColor.BlackColor3, shape = RoundedCornerShape(10.dp))
                        .padding(17.dp)
                ) {

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Start
                    ) {
                        Image(
                            painter = painterResource(R.drawable.img_man),
                            contentDescription = "img_man",
                            modifier = Modifier
                                .width(32.dp)
                                .height(42.dp)
                        )
                        Column(
                            modifier = Modifier.padding(start = 5.dp),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.Start
                        ) {
                            Text(
                                text = "Don’t forget to tip ${shopData?.shopName}",
                                color = AppColor.BlackColor,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Start
                            )
                            Text(
                                text = stringResource(R.string.tip_goes_to_your_ride),
                                color = AppColor.BlackColor.copy(0.5f),
                                fontSize = 16.sp,
                            )
                        }
                    }

                    LazyRow(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {

                        items(tipOptions) { option ->
                            TipOptionCurrencyItem(
                                mod = Modifier.padding(horizontal = 5.dp),
                                option = option,
                                isSelected = option == selectedTipOption, // Check if current option is selected
                                onTipSelected = { tip ->

                                    scope.launch {
                                        viewModel.saveTip(
                                            tip.amount,
                                        ).collect {
                                            if (it != null) {
                                                cartResponse = cartResponse.copy(
                                                    result = cartResponse.result?.copy(
                                                        tip = it.result?.data?.tipAmount,
                                                        total = it.result?.data?.finalTotal
                                                    )
                                                )
                                                selectedTipOption =
                                                    tip // Update the selected option when clicked                                            }
                                            }
                                        }
                                    }
                                }
                            )
                        }
                    }
                }


//                LazyRow(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(vertical = 8.dp),
//                    verticalAlignment = Alignment.CenterVertically,
//                    horizontalArrangement = Arrangement.SpaceBetween
////                horizontalArrangement = Arrangement.spacedBy(16.dp), // Space between items
////                contentPadding = PaddingValues(horizontal = 16.dp) // Padding around the entire row
//                ) {
//                    items(tipOptions) { option ->
//                        TipOptionItem(
//                            option = option,
//                            isSelected = option == selectedTipOption, // Check if current option is selected
//                            onTipSelected = { tipSelected ->
//                                showLoading = true
//                                scope.launch {
////                                Log.d(TAG, "PaymentSummaryScreenUI: ${cartResponse.result?.order?.objectId}")
////                                delay(2000)
//                                    viewModel.saveTip(
//                                        tipSelected.amount,
//                                        cartResponse.result?.order?.objectId
//                                    ).collect {
//                                        Log.d(TAG, "PaymentSummaryScreenUI: tip")
//                                        showLoading = false
//                                        if (it != null) {
//                                            selectedTipOption =
//                                                tipSelected // Update the selected option when clicked
//                                            Log.d(TAG, "PaymentSummaryScreenUI: not null tip")
//                                            tipResponse = it
//                                        }
//                                    }
//                                }
//                            }
//                        )
//                    }
//                }


                HorizontalDivider(
                    color = Color.Black.copy(0.5f),
                    thickness = 1.dp,
                    modifier = Modifier.padding(vertical = 23.dp)
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start
                ) {
                    Image(
                        painter = painterResource(R.drawable.ic_hand),
                        contentDescription = "back"
                    )

                    Text(
                        color = BlackColor2,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 6.dp),
                        text = stringResource(R.string.payment_method),
                        fontSize = 15.sp,
                        textAlign = TextAlign.Start,
                        fontWeight = FontWeight.Bold
                    )

                }


            }
//        items(3) {
//            PaymentMethodItem(
//                modifier = Modifier.fillMaxWidth(),
//                paymentInt = it,
//                isSelected = it == paymentMethodId
//            ) {
//                paymentMethodId = it
//            }
//        }
            items(cartData) {
                CartItem(modifier = Modifier.fillMaxWidth(), it, false) { cartItemAction ->

                }
            }
            item {

//            HorizontalDivider(
//                color = Color.Black.copy(0.5f),
//                thickness = 1.dp,
//                modifier = Modifier.padding(vertical = 23.dp)
//            )

                PaymentSummary(
                    modifier = Modifier.fillMaxWidth(),
                    updateCartResponse = cartResponse
                )

                AppButtonPrimary(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 6.dp),
                    text = stringResource(R.string.place_order)
                ) {
                    //here call getProfile
                    scope.launch {
                        profileViewModel.getUserInfoResult()
                    }
                }
            }

        }
        if (orderTypeDialog) {
            Log.d(
                TAG,
                "PaymentSummaryScreenUI isPickup: ${cartResponse.result?.order?.restaurant?.isPickUp}"
            )
            Log.d(
                TAG,
                "PaymentSummaryScreenUI isDelivery: ${cartResponse.result?.order?.restaurant?.isDelivery}"
            )
            Log.d(
                TAG,
                "PaymentSummaryScreenUI Compound objectId: ${cartResponse.result?.order?.restaurant?.compound?.objectId}"
            )
            OrderTypeDialog(
                modifier = Modifier.fillMaxWidth(),
                cartResponse,
                cartViewModel = viewModel,
                shop = shopData!!,
                confirm = { selectedOrderType ->
                    orderTypeSelected = selectedOrderType
                    orderTypeDialog = false
                    if (orderTypeSelected != ORDER_TYPE.PICKUP) {
                        showPlaceOrder = true
                    }
                })
        }

        if (showPlaceOrder) PlaceOrderDialog(
            modifier = Modifier.fillMaxWidth(), confirm = {
                placeOrderType = it
                showPlaceOrder = false
                if (placeOrderType == PLACE_ORDER_TYPE.ADDRESS) {
                    showAddressList = true
                } else if (placeOrderType == PLACE_ORDER_TYPE.UMBERLLA) {
                    showUmbrellaDialog = true
                }
            }, dismiss = { showPlaceOrder = false })
        if (showUmbrellaDialog) {
            UmbrellaDialog(
                modifier = Modifier.fillMaxWidth(),
                addressViewModel, cartViewModel = viewModel, cartResponse
            ) {
                showUmbrellaDialog = false
            }
        }
        if (showAddressList) {

            AddressListDialog(
                addressViewModel = addressViewModel,
                orderId = updateCartResponse.result?.order?.objectId.toString(),
                selectedAddress = {
                    showAddressList = false
                }, addNewAddress = {
                    showAddressList = false
                    lat = 0.0
                    lng = 0.0
                    address = ""
                    showMap = true
                }, onDismiss = {
                    showAddressList = false
                },
                modifier = Modifier.fillMaxWidth()
            )
        }
        if (showMap) {
            MapDialog(
                modifier = Modifier.fillMaxWidth(),
                mapViewModel,
                onResult = { latitude, longitude, selectedAddress ->
                    lat = latitude
                    lng = longitude
                    address = selectedAddress
                    showMap = false
                    addNewAddressForm = true
                },
                onDismiss = { showMap = false })
        }
        if (addNewAddressForm) {
            AddAddressDialog(
                modifier = Modifier.fillMaxWidth(),
                cartResponse,
                addressViewModel,
                onDismiss = {
                    addNewAddressForm = false
                },
                onResult = { address ->
                    addNewAddressForm = false
                    showAddressList = true
                })
        }
        if(showEditProfile){
            UpdateProfileDialog(modifier = Modifier.fillMaxWidth(),editProfileViewModel,viewModel,userInfo, onUpdate = {
                //done
                showEditProfile = false
                Log.d(TAG, "PaymentSummaryScreenUI: Updated")
            }, onDismiss = {
              showEditProfile = false  
            })
        }
        LoadingView(viewModel.showLoading.collectAsState(false).value)

    }

}

