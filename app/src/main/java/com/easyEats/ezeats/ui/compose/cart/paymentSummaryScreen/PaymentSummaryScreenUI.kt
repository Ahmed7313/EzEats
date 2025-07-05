package com.easyEats.ezeats.ui.compose.cart.paymentSummaryScreen

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
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
import com.easyEats.ezeats.ui.compose.cart.CartItem
import com.easyEats.ezeats.ui.compose.cart.paymentSummaryScreen.dummy.TipOption
import com.easyEats.ezeats.ui.compose.cart.summary.PaymentSummary
import com.easyEats.ezeats.ui.compose.components.AppButtonPrimary
import com.easyEats.ezeats.ui.compose.components.AppImage
import com.easyEats.ezeats.ui.compose.theme.AppColor
import com.easyEats.ezeats.ui.compose.theme.AppColor.BlackColor2
import com.easyEats.ezeats.ui.main.restaurant.CartViewModel
import com.trend.thecontent.screens.components.LoadingView
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun PaymentSummaryScreenUI(
    modifier: Modifier = Modifier,
    navigate: NavController,
    viewModel: CartViewModel,
    cartResponse: UpdateCartResponse
) {

    val TAG = "PaymentSummaryScreenUI"
    var tipId by remember {
        mutableStateOf(-1)
    }

    val cartData =
        viewModel.cartProducts.collectAsState(arrayListOf()).value


    val tipOptions = remember {
        listOf(
            TipOption("1", "20", "5%"),
            TipOption("2", "30", "5%"),
            TipOption("3", null, null, isCustom = true)
        )
    }

    var showLoading by remember {
        mutableStateOf(false)
    }

    var selectedTipOption by remember { mutableStateOf<TipOption?>(null) }

    var tipResponse by remember {
        mutableStateOf<TipResponse>(TipResponse())
    }

    val scope = rememberCoroutineScope()
    LaunchedEffect(tipResponse) {
        viewModel.checkout()
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
                    modifier = Modifier.clickable{
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



                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
//                horizontalArrangement = Arrangement.spacedBy(16.dp), // Space between items
//                contentPadding = PaddingValues(horizontal = 16.dp) // Padding around the entire row
                ) {
                    items(tipOptions) { option ->
                        TipOptionItem(
                            option = option,
                            isSelected = option == selectedTipOption, // Check if current option is selected
                            onTipSelected = { tipSelected ->
                                showLoading = true
                                scope.launch {
//                                Log.d(TAG, "PaymentSummaryScreenUI: ${cartResponse.result?.order?.objectId}")
//                                delay(2000)
                                    viewModel.saveTip(tipSelected.amount,cartResponse.result?.order?.objectId).collect {
                                        Log.d(TAG, "PaymentSummaryScreenUI: tip")
                                        showLoading = false
                                        if(it != null){
                                            selectedTipOption = tipSelected // Update the selected option when clicked
                                            Log.d(TAG, "PaymentSummaryScreenUI: not null tip")
                                            tipResponse = it
                                        }
                                    }
                                }
                            }
                        )
                    }
                }


//            Column(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .border(BorderStroke(1.dp, AppColor.BlackColor.copy(alpha = 0.5f)))
//            ) {

//                Row(
//                    verticalAlignment = Alignment.CenterVertically,
//                    horizontalArrangement = Arrangement.Center
//                ) {
//                    AppImage(
//                        imageUrl = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcS03kZXomLjR2oTlWzo5P2MKLI5mEcC8lx-Kw&s",
//                        size = 47,
//                        cornerRadius = 0
//                    )
//                    Column(
//                        verticalArrangement = Arrangement.Center,
//                        horizontalAlignment = Alignment.CenterHorizontally
//                    ) {
//                        Text(
//                            text = "Buy your rider a coffee",
//                            color = AppColor.BlackColor,
//                            fontSize = 16.sp,
//                        )
//                        Text(
//                            text = "100% of this tip goes to your rider",
//                            color = AppColor.BlackColor.copy(0.5f),
//                            fontSize = 16.sp,
//                        )
//                    }
//                }

//                LazyRow(
//                    modifier = Modifier.fillMaxWidth(),
//                    verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center) {
//
//                    items(tipOptions){ option ->
//                        TipOptionCurrencyItem(
//                            mod = Modifier,
//                            option = option,
//                            isSelected = option == selectedTipOption, // Check if current option is selected
//                            onTipSelected = {
//                                selectedTipOption = it // Update the selected option when clicked
//                            }
//                        )
//                    }
//                }
//            }

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
                CartItem(modifier = Modifier.fillMaxWidth(), it , false) { cartItemAction ->

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
                ) { }
            }

        }
        LoadingView(showLoading)
    }

}

