package com.easyEats.ezeats.ui.compose.cart.payment

import android.annotation.SuppressLint
import android.app.ActivityManager
import android.content.Context
import android.os.Process
import android.util.Log
import android.view.ViewGroup
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.background
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import java.util.*


@Composable
fun PaymentScreen (modifier: Modifier = Modifier, url: String) {

    val mUrl = url
    val TAG = "Payment Online"


}

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun MyContent(url : String, modifier : Modifier = Modifier){
//    val context = LocalContext.current
//    val preferences = SavePreferences(context)
    // Declare a string that contains a url


//    if (showDialogue){
//
//        StateDialog(isDdne = showDialogue,
//            icon = R.drawable.ic_verfied_green,
//            title = R.string.payment_done,
//            titleColor = SuccessColor,
//            msg = R.string.payment_full_text,
//            showBtn = true,
//            btnText = R.string.show_campaign,
//            dissMissBtnText = R.string.home,
//            onClick = {
//                showDialogue = false
//                RefreshData.refreshWallet.value = true
//                if (Locale.getDefault().getDisplayLanguage() == "English") {
//                    LocaleUtils.setLocale(context, "en")
//                    preferences.putLanguage("en")
//                    Language.headerLang.value = "en"
//                } else {
//                    LocaleUtils.setLocale(context, "ar")
//                    preferences.putLanguage("ar")
//                    Language.headerLang.value = "ar"
//                }
//
//                navController.navigate(Screen.CampaignDetailsScreen.withArgs(campaignId))
//                {
//                    popUpTo(Screen.HomeScreen.route){
//                        inclusive = false
//                    }
//                }
//            }) {
//            showDialogue = false
//            RefreshData.refreshWallet.value = true
//            navController.popBackStack(Screen.HomeScreen.route,false )
//        }
////        MainPopUpDialogue(
////            isDdne = showDialogue,
////            firstText = stringResource(R.string.payment_done),
////            buttonText = stringResource(id = R.string.show_campaign),
////            dissMissBtnText =stringResource(id = R.string.home) ,
////            secondText = stringResource(R.string.payment_full_text),
////            onClick = {
////                showDialogue = false
////                RefreshData.refreshWallet.value = true
////                if (Locale.getDefault().getDisplayLanguage() == "English") {
////                    LocaleUtils.setLocale(context, "en")
////                    preferences.putLanguage("en")
////                    Language.headerLang.value = "en"
////                } else {
////                    LocaleUtils.setLocale(context, "ar")
////                    preferences.putLanguage("ar")
////                    Language.headerLang.value = "ar"
////                }
////
////                navController.navigate(Screen.CampaignDetailsScreen.withArgs(campaignId))
////                {
////                    popUpTo(Screen.HomeScreen.route){
////                        inclusive = false
////                    }
////                }
////            }) {
////            showDialogue = false
////            RefreshData.refreshWallet.value = true
////            navController.popBackStack(Screen.HomeScreen.route,false )
////        }
//    }

//    SuccessCreatingCampaignDialog(navController, showDialogue) {
//        showDialogue = false
//        RefreshData.refreshWallet.value = true
//        if (Locale.getDefault().getDisplayLanguage() == "English") {
//            LocaleUtils.setLocale(context, "en")
//            preferences.putLanguage("en")
//            Language.headerLang.value = "en"
//        } else {
//            LocaleUtils.setLocale(context, "ar")
//            preferences.putLanguage("ar")
//            Language.headerLang.value = "ar"
//        }
//
//        navController.navigate(Screen.CampaignDetailsScreen.withArgs(campaignId))
//        {
//            popUpTo(Screen.HomeScreen.route){
//                inclusive = false
//            }
//        }
//    }
}

private fun getProcessName(context: Context?): String? {
    if (context == null) return null
    val manager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
    for (processInfo in manager.runningAppProcesses) {
        if (processInfo.pid == Process.myPid()) {
            return processInfo.processName
        }
    }
    return null
}

@Preview
@Composable
fun PaymentScreenPreview () {
  //  PaymentScreen(rememberNavController(), "1",)
}