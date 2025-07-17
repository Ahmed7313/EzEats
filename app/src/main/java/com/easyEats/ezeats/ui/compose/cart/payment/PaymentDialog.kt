package com.easyEats.ezeats.ui.compose.cart.payment

import android.util.Log
import android.view.ViewGroup
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.daman.edman.screens.components.AppTextHeader
import com.easyEats.ezeats.R
import com.easyEats.ezeats.domain.main.model.request.cart.response.UpdateCartResponse
import com.easyEats.ezeats.domain.main.model.response.address.add.AddAddressRequest
import com.easyEats.ezeats.domain.main.model.response.address.add.AddAddressResponse
import com.easyEats.ezeats.ui.compose.address.AddressViewModel
import com.easyEats.ezeats.ui.compose.components.AppButtonPrimary
import com.easyEats.ezeats.ui.compose.components.AppInputTextLabelTop
import com.easyEats.ezeats.ui.compose.theme.AppColor
import com.easyEats.ezeats.ui.compose.components.LoadingView
import kotlinx.coroutines.launch


@Composable
fun PaymentDialog(
    modifier: Modifier = Modifier,
    paymentUrl: String,
    onResult: (PAYMENT_RESULT) -> Unit,
    onDismiss: () -> Unit,
) {
    val TAG = "AddAddressDialog"
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true,
        confirmValueChange = { true }
    )


    val scope = rememberCoroutineScope()

    val context = LocalContext.current

    ModalBottomSheet(
        modifier = modifier
            .fillMaxWidth()
            .requiredHeight(700.dp),
        dragHandle = {},
        sheetState = sheetState,
        shape = RoundedCornerShape(
            topStart = 16.dp,
            topEnd = 16.dp
        ),
        contentColor = AppColor.backgroundColor,
        content = {
            var showDialogue by remember { mutableStateOf(false) }
            // Adding a WebView inside AndroidView
            // with layout as full screen
            AndroidView(
                factory = {
                WebView(it).apply {
                    layoutParams = ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )
                    webViewClient = object : WebViewClient() {
                        override fun onPageFinished(view: WebView?, url: String?) {
                            super.onPageFinished(view, url)
                            Log.d(TAG, "onPageFinished: $url")
                            if (url?.contains("success=true") == true || url?.contains("status=success") == true || url?.contains("paymentStatus=SUCCESS") == true ) {
                                showDialogue = true
                                onResult(PAYMENT_RESULT.SUCCESS)
                            }
                        }
                    }
                    val webSettings: WebSettings = this.getSettings()

                    webSettings.javaScriptEnabled = true
                    webSettings.javaScriptCanOpenWindowsAutomatically = true


                    loadUrl(paymentUrl)


                }
            }, update = {
                it.loadUrl(paymentUrl)
            },
                modifier = modifier.background(Color.White)
            )

        },
        onDismissRequest = {
            onDismiss()
        }
    )
}