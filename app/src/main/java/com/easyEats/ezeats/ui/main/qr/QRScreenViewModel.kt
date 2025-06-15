package com.easyEats.ezeats.ui.main.qr

import androidx.lifecycle.viewModelScope
import com.easyEats.ezeats.domain.main.use_case.MainUseCase
import com.easyEats.ezeats.ui.main.main.MainViewModel
import com.journeyapps.barcodescanner.BarcodeResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject
/**
 * ViewModel responsible for handling QR code scanning and transforming the link based on the user's token.
 * It retrieves the token and transforms the scanned link accordingly before passing it to the caller.
 */
@HiltViewModel
class QRScreenViewModel @Inject constructor(private val mainUseCase: MainUseCase) :
    MainViewModel(mainUseCase) {

    /**
     * Transforms the scanned link by appending the user token and restaurant ID.
     *
     * @param link The scanned QR code link.
     * @param token The user authentication token.
     * @return A transformed URL containing the token and restaurant ID.
     */
    private fun transformLink(link: String, token: String): String {
        // Extract the restaurant ID from the link (the part after the last '/')
        val restaurantId = link.substringAfterLast("/")
        // Return the transformed link with the token and restaurant ID
        return "https://ezeats.app/signIn?token=$token&restaurant=$restaurantId"
    }

    /**
     * Handles the QR code result, retrieves the token, and transforms the link before calling the provided callback.
     *
     * @param result The result of the barcode scan.
     * @param onLinkReady The callback to be invoked with the transformed link.
     */
    fun handleOpenBrowser(result: BarcodeResult, onLinkReady: (String) -> Unit) {
        viewModelScope.launch {
            // Collect the user's token and perform link transformation
            getToken().collect { token ->
                // If the token is available, transform the link; otherwise, use the original link
                val transformedLink = if (token.isNotEmpty()) {
                    transformLink(result.text.toString(), token)
                } else {
                    result.text.toString() // Use the original link if no token is available
                }
                // Call the callback with the transformed link
                onLinkReady(transformedLink)
            }
        }
    }
}

