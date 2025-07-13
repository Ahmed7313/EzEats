package com.easyEats.ezeats.ui.compose.map

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.easyEats.ezeats.R
import com.easyEats.ezeats.ui.compose.components.AppButtonPrimary
import com.easyEats.ezeats.ui.compose.components.AppButtonStroke
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState
import com.easyEats.ezeats.ui.compose.map.mapUtils.GetLocation
import com.easyEats.ezeats.ui.compose.map.mapUtils.MapMarkerModel
import com.easyEats.ezeats.ui.compose.map.mapUtils.RequestLocationPermissionUsingRememberLauncherForActivityResult
import com.easyEats.ezeats.ui.compose.theme.AppColor
import com.easyEats.ezeats.ui.compose.components.LoadingView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


@Composable
fun MapViewUI(
    modifier: Modifier = Modifier,
    viewModel: MapViewModel,
    currentLocation: Boolean = true,
    scrollable: Boolean = true,
    onResult: (Double, Double, String) -> Unit,
    dismiss: () -> Unit
) {
    val context = LocalContext.current
    val location = GetLocation(context)
    //search
//    var searchQuery by remember { mutableStateOf("") }
//    var searchResults by remember { mutableStateOf<List<AutocompletePrediction>>(emptyList()) }
//    var showResults by remember { mutableStateOf(false) }
//
//    val keyboardController = LocalSoftwareKeyboardController.current
//    val googleMapPlacesApi = GoogleMapPlacesApi(context)
//    val coroutineScope = rememberCoroutineScope()

    val TAG = "MapViewUI"
    // Debounced search function
    val coroutineScope = rememberCoroutineScope()

    var requestCurrentLocation by remember { mutableStateOf(false) }

    var address by remember {
        mutableStateOf("")
    }
    val markerModel by remember {
        mutableStateOf(
            MapMarkerModel(
                LatLng(21.492500, 39.177570),
                context.getString(R.string.done_selected_address)
            )
        )
    }
    var showMarker by remember {
        mutableStateOf(false)
    }
    val marker by remember {
        mutableStateOf(markerModel)
    }
    var mapPosition by remember {
        mutableStateOf<LatLng>(LatLng(0.0, 0.0))
    } //جده
    val markerState = rememberMarkerState(position = mapPosition)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(mapPosition, 16f)
    }
    var onMapLoaded by remember { mutableStateOf(false) }

    var currentLocationFirstTime by remember { mutableStateOf(false) }

    LaunchedEffect(onMapLoaded) {
        if (onMapLoaded && currentLocation && !currentLocationFirstTime) {
            requestCurrentLocation = true
        }
    }

    fun moveCamera(latLng: LatLng) {
        coroutineScope.launch(Dispatchers.Main) {
            viewModel.showProgress()
        }
        address = ""
        markerState.position = latLng
        mapPosition = latLng
        cameraPositionState.position = CameraPosition.fromLatLngZoom(latLng, 16f)
        coroutineScope.launch {
            location.getAddressFromLatLng(
                context,
                latLng.latitude,
                latLng.longitude
            ) { addressFetch ->
                coroutineScope.launch(Dispatchers.Main) {
                    viewModel.hideProgress()
                }
                address = addressFetch.ifEmpty {
                    context.getString(R.string.done_selected_address)
                }
            }
        }
    }

    LaunchedEffect(marker.latlng) {
        showMarker = true
        moveCamera(marker.latlng)
    }


    var locationText by remember { mutableStateOf("No location obtained :(") }
    var showPermissionResultText by remember { mutableStateOf(false) }
    var permissionResultText by remember { mutableStateOf("Permission Granted...") }

    fun hideProgress() {
        coroutineScope.launch {
            viewModel.hideProgress()
        }
    }

    fun showProgress() {
        coroutineScope.launch {
            viewModel.showProgress()
        }
    }

    fun getCurrentLocation2() {
        val locationRequest = LocationRequest.create().apply {
            LocationRequest.PRIORITY_HIGH_ACCURACY
        }
        val builder = LocationSettingsRequest.Builder().addLocationRequest(locationRequest)
        val settingsClient = LocationServices.getSettingsClient(context)

        settingsClient.checkLocationSettings(builder.build())
            .addOnSuccessListener { response ->
                hideProgress()
                // Location settings are satisfied
                Log.d(
                    TAG,
                    "getCurrentLocation2: ${response.locationSettingsStates?.isNetworkLocationUsable}"
                )
            }
            .addOnFailureListener { exception ->
                hideProgress()
                showPermissionResultText = true
                locationText =
                    exception.localizedMessage ?: "Error Getting Last Location"
                requestCurrentLocation = false

                Log.d(TAG, "getCurrentLocation2: ${exception.toString()}")
            }
    }

    fun getCurrentLocation() {
        Log.d(TAG, "getCurrentLocation: ")
        showProgress()
        location.getCurrentLocation(
            onGetCurrentLocationSuccess = {
                Log.d(TAG, "MapLocationScreenUILocation2: ${it.first} , ${it.second}")
                hideProgress()
//                        if (currentLocation)
                marker.latlng = LatLng(it.first, it.second)
//                currentLatLng = LatLng(it.first, it.second)
                moveCamera(marker.latlng)
                requestCurrentLocation = false

            },
            onGetCurrentLocationFailed = { exception ->
                Log.d(TAG, "BranchesScreenUI: exition ${exception.message}")
                hideProgress()
                showPermissionResultText = true
                locationText =
                    exception.localizedMessage ?: "Error Getting Last Location"
//                requestCurrentLocation = false

//                        Toast.makeText(context, locationText, Toast.LENGTH_SHORT).show()

                location.getLastUserLocation(
                    onGetLastLocationSuccess = {
                        Log.d(TAG, "MapLocationScreenUILocation: ${it.first} , ${it.second}")
                        hideProgress()
                        marker.latlng = LatLng(it.first, it.second)
//                        currentLatLng = LatLng(it.first, it.second)
                        moveCamera(marker.latlng)
                        requestCurrentLocation = false


                    },
                    onGetLastLocationFailed = { exception ->
                        Log.d(TAG, "MapLocationScreenUI: exs")
                        hideProgress()
                        getCurrentLocation2()


//                        Toast.makeText(context, locationText, Toast.LENGTH_SHORT).show()
                    },
                    onGetLastLocationIsNull = {
                        Log.d(TAG, "MapLocationScreenUI: null")
                        hideProgress()
                        Log.d(TAG, "BranchesScreenUI: exition null")
                        requestCurrentLocation = false

                        // Attempt to get the current user location

                    }
                )

            },
            priority = true
        )
    }


    if (requestCurrentLocation) {

        // Request location permission using a Compose function
        RequestLocationPermissionUsingRememberLauncherForActivityResult(
            onPermissionGranted = {
                if (currentLocation) {
                    coroutineScope.launch {
                        viewModel.showProgress()
                    }
                    showPermissionResultText = true
                    permissionResultText = "Permission Granted..."
                    // Attempt to get the last known user location
                    getCurrentLocation()
                }
            },
            onPermissionDenied = {
                // Callback when permission is denied
                showPermissionResultText = true
                permissionResultText = "Permission Denied :("
            },
        )
    }


    val uiSettings by remember {
        mutableStateOf(
            MapUiSettings(
                zoomControlsEnabled = false,
                scrollGesturesEnabled = scrollable,
//                compassEnabled = true,
//                scrollGesturesEnabled = true
            )
        )
    }
    val properties by remember {
        mutableStateOf(MapProperties(mapType = MapType.NORMAL, isBuildingEnabled = true))
    }


    Box(modifier = modifier.fillMaxSize()) {
        Column(
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {

            Text(
                text = stringResource(R.string.confirm_location),
                fontSize = 30.sp,
                modifier = Modifier.padding(bottom = 16.dp),
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )


            Column(
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize().weight(1f)
                        .background(AppColor.GreyColor, shape = RoundedCornerShape(20.dp))
                ) {
                    GoogleMap(
                        modifier = Modifier
                            .fillMaxSize(),
                        cameraPositionState = cameraPositionState,
                        properties = properties,
                        uiSettings = uiSettings,
                        onMapLoaded = {
                            onMapLoaded = true
                        },
                        onMapClick = { latlng ->
                            marker.latlng = LatLng(latlng.latitude, latlng.longitude)
                            moveCamera(latlng)
                        },
                        onMyLocationClick = { log ->

                            Log.d(TAG, "MapLocationScreenUI-L: ${log.longitude} , ${log.latitude}")
                            if (log.latitude != 0.0) {
                                marker.latlng = LatLng(log.latitude, log.longitude)
                                moveCamera(marker.latlng)
                            }
                        },
                    ) {
                        if (showMarker)
                            Marker(
                                state = markerState,
                                title = context.getString(R.string.location),
                                snippet = context.getString(R.string.done_selected_address)
                            )
                    }

                    FloatingActionButton(
                        onClick = {
                            Log.d(TAG, "MapLocationScreenUI: trueee")
                            requestCurrentLocation = true
                        },
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(top = 20.dp),
                        containerColor = AppColor.ColorPrimary
                    ) {
                        Icon(
                            imageVector = Icons.Default.MyLocation,
                            contentDescription = stringResource(id = R.string.location),
                            tint = Color.White
                        )
                    }
                }
                Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)) {
                    AppButtonPrimary(
                        text = stringResource(R.string.confirm),
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .padding(
                                top = 28.dp,
                                start = 24.dp,
                                end = 24.dp,
                                bottom = 24.dp
                            )
                    ) {
                        onResult(mapPosition.latitude, mapPosition.longitude, address)
                    }
                    AppButtonStroke(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f).padding(
                                top = 28.dp,
                                start = 24.dp,
                                end = 24.dp,
                                bottom = 24.dp
                            ),
                        text = stringResource(R.string.cancel)
                    ) {
                        dismiss()
                    }
                }
            }
        }
        LoadingView(viewModel.progressLocation.collectAsState(initial = false).value)


    }


}

