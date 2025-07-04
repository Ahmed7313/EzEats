package com.easyEats.ezeats.ui.compose.cart

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.easyEats.ezeats.R
import com.easyEats.ezeats.domain.room.ShopEntity
import com.easyEats.ezeats.ui.compose.cart.summary.PaymentSummary
import com.easyEats.ezeats.ui.compose.components.AppButtonPrimary
import com.easyEats.ezeats.ui.compose.components.AppButtonStroke
import com.easyEats.ezeats.ui.compose.theme.AppColor.BlackColor2
import com.easyEats.ezeats.ui.compose.theme.AppColor.GreyColor
import com.easyEats.ezeats.ui.main.cart.CartFragmentDirections
import com.easyEats.ezeats.ui.main.restaurant.CartViewModel
import kotlinx.coroutines.launch

@Composable
fun CartScreenUI(
    modifier: Modifier = Modifier,
    viewModel: CartViewModel,
    findNavController: NavController,) {

    val TAG = "CartScreenUI"
    val scope = rememberCoroutineScope()

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

    var isAddedBefore by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(shopData?.shopId) {
        if (shopData?.shopId?.isNotEmpty() == true)
            viewModel.getCartData(shopData.shopId)
    }
    LaunchedEffect(cartData,isAddedBefore) {
        if(cartData.isEmpty() && isAddedBefore)
            findNavController.navigateUp()
    }

    LaunchedEffect(Unit) {
        viewModel.checkout()
    }

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
            }
        }
        item {
            CartSpecialComment(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 20.dp)
            )


            VoucherOrPromoUI(modifier = Modifier.fillMaxWidth())


            PaymentSummary(modifier = Modifier.fillMaxWidth())

            Row(modifier = Modifier.fillMaxWidth()) {
                AppButtonPrimary(
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 6.dp),
                    text = stringResource(R.string.checkout)
                ) {
//                    findNavController.navigate(CartFragmentDirections.actionCartFragmentToPaymentSummaryFragment())
                }
                AppButtonStroke(
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 6.dp),
                    text = stringResource(R.string.add_items)
                ) {
//                    navigate.navigate(R.)
                }
            }
        }

    }
}

