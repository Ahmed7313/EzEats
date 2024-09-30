package com.easyEats.ezeats.ui.main.qr

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.easyEats.ezeats.base.BaseBottomSheetFragment
import com.easyEats.ezeats.databinding.FragmentQRScreenBinding
import com.easyEats.ezeats.ui.main.qr.permession.DefaultPermissionHandler
import com.easyEats.ezeats.ui.main.qr.permession.PermissionState
import com.easyEats.ezeats.utils.Constants.REQUEST_CAMERA_PERMISSION
import com.journeyapps.barcodescanner.BarcodeCallback
import com.journeyapps.barcodescanner.BarcodeResult
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class QRScreenFragment : BaseBottomSheetFragment<FragmentQRScreenBinding>() {
    private val viewModel: QRScreenViewModel by viewModels()

    // Lazy-initialized property for handling permissions
    private val permissionHandler: DefaultPermissionHandler by lazy {
        DefaultPermissionHandler(this)
    }

    override fun setUpViews() {
        // Initialize views here if needed. Currently, it's empty.
    }

    override fun listenToObservers() {
        // Set up observers here if needed. Currently, it's empty.
    }

    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentQRScreenBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Check the camera permission status and handle it
        val state = when {
            ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED -> {
                PermissionState.PERMISSION_GRANTED
            }

            else -> {
                PermissionState.PERMISSION_NOT_GRANTED
            }
        }
        permissionHandler.handlePermission(state)
    }

    /**
     * Requests camera permission from the user.
     * This method is called if the permission is not granted.
     */
    fun requestCameraPermission() {
        requestPermissions(
            arrayOf(Manifest.permission.CAMERA),
            REQUEST_CAMERA_PERMISSION
        )
    }

    /**
     * Sets up the barcode scanner to continuously decode QR codes.
     * This method is called if the camera permission is granted.
     */
    fun setupScanner() {
        binding.barcodeScanner.decodeContinuous(object : BarcodeCallback {
            override fun barcodeResult(result: BarcodeResult?) {
                result?.let {
                    // Pause scanning and open the URL in a browser
                    binding.barcodeScanner.pause()
                    openBrowser(it)
                }
            }

            override fun possibleResultPoints(resultPoints: List<com.google.zxing.ResultPoint>?) {}
        })
    }

    /**
     * Opens a browser with the URL obtained from the scanned QR code.
     *
     * @param result The result of the scanned QR code.
     */
    private fun openBrowser(result: BarcodeResult) {
        viewModel.handleOpenBrowser(result) { link ->
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(link))
            requireActivity().startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        // Resume barcode scanning when the fragment is visible
        binding.barcodeScanner.resume()
    }

    override fun onPause() {
        super.onPause()
        // Pause barcode scanning when the fragment is not visible
        binding.barcodeScanner.pause()
    }

    /**
     * Handles the result of the camera permission request.
     *
     * @param requestCode The request code passed in `requestPermissions()`.
     * @param permissions The requested permissions.
     * @param grantResults The grant results for the corresponding permissions.
     */
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            val state =
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    PermissionState.PERMISSION_GRANTED
                } else {
                    PermissionState.PERMISSION_NOT_GRANTED
                }
            permissionHandler.handlePermission(state)
        }
    }
}
