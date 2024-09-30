package com.easyEats.ezeats.ui.main.qr

import androidx.lifecycle.viewModelScope
import com.easyEats.ezeats.domain.main.use_case.MainUseCase
import com.easyEats.ezeats.ui.main.main.MainViewModel
import com.journeyapps.barcodescanner.BarcodeResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class QRScreenViewModel @Inject constructor(private val mainUseCase: MainUseCase) :
    MainViewModel(mainUseCase) {

    private fun transformLink(link: String, token: String): String {
        val restaurantId = link.substringAfterLast("/")
        return "https://ezeats.app/signIn?token=$token&restaurant=$restaurantId"
    }

    fun handleOpenBrowser(result: BarcodeResult, onLinkReady: (String) -> Unit) {
        viewModelScope.launch {
            getToken().collect { token ->
                val transformedLink = if (token.isNotEmpty()) {
                    transformLink(result.text.toString(), token)
                } else {
                    result.text.toString()
                }
                onLinkReady(transformedLink)
            }
        }
    }
}
