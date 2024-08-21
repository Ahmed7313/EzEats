package com.easyEats.ezeats.ui.main.qr

import android.Manifest
import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.easyEats.ezeats.MyApplication.Companion.TAG
import com.easyEats.ezeats.R
import com.easyEats.ezeats.base.BaseBottomSheetFragment
import com.easyEats.ezeats.databinding.FragmentQRScreenBinding
import com.easyEats.ezeats.ui.main.qr.permession.DefaultPermissionHandler
import com.easyEats.ezeats.ui.main.qr.permession.PermissionState
import com.easyEats.ezeats.utils.Constants.REQUEST_CAMERA_PERMISSION
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.journeyapps.barcodescanner.BarcodeCallback
import com.journeyapps.barcodescanner.BarcodeResult
import com.journeyapps.barcodescanner.DecoratedBarcodeView
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
@AndroidEntryPoint
class QRScreenFragment : BaseBottomSheetFragment<FragmentQRScreenBinding>() {
    private val permissionHandler: DefaultPermissionHandler by lazy {
        DefaultPermissionHandler(this@QRScreenFragment)
    }

    override fun setUpViews() {

    }

    override fun listenToObservers() {

    }

    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentQRScreenBinding.inflate(layoutInflater)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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
    fun requestCameraPermission() {
        ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf(Manifest.permission.CAMERA),
            REQUEST_CAMERA_PERMISSION
        )
    }
    fun setupScanner() {
        binding.barcodeScanner.decodeContinuous(object : BarcodeCallback {
            override fun barcodeResult(result: BarcodeResult?) {
                result?.let {
                    binding.barcodeScanner.pause()
                    openBrowser(it)
                }
            }

            override fun possibleResultPoints(resultPoints: List<com.google.zxing.ResultPoint>?) {}
        })
    }

    private fun openBrowser(it: BarcodeResult) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse("${it.result}?")
        startActivity(intent)
    }

    override fun onResume() {
        super.onResume()
        binding.barcodeScanner.resume()
    }
    override fun onPause() {
        super.onPause()
        binding.barcodeScanner.pause()
    }
}