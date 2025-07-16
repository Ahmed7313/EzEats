package com.easyEats.ezeats.ui.compose.order

import androidx.lifecycle.ViewModel
import com.easyEats.ezeats.domain.main.use_case.MainUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

//import com.google.android.libraries.maps.model.LatLng

@HiltViewModel
class PaymentSummaryViewModel  @Inject constructor(
    private val mainUseCase: MainUseCase,
): ViewModel() {

    private val _showLoading = MutableStateFlow<Boolean>(false)
    val showLoading = _showLoading.asStateFlow()




}