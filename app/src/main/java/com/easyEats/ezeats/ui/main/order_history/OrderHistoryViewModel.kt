package com.easyEats.ezeats.ui.main.order_history

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.easyEats.ezeats.domain.main.model.request.order_history.OrderHistoryRequest
import com.easyEats.ezeats.domain.main.model.response.order_history.Order
import com.easyEats.ezeats.domain.main.use_case.MainUseCase
import com.easyEats.ezeats.ui.main.main.MainViewModel
import com.google.android.gms.common.internal.FallbackServiceBroker
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OrderHistoryViewModel @Inject constructor(val mainUseCase: MainUseCase) :
    MainViewModel(mainUseCase) {
    private val _orders = MutableLiveData<List<Order?>>()
    val orders: LiveData<List<Order?>> get() = _orders
    val isHavingOrders = MutableLiveData(true)
    val isLoading = MutableLiveData(false)

    private var currentPage = 1

    init {
        loadMoreOrders()
    }

    fun loadMoreOrders() {
        viewModelScope.launch {
            isLoading.value = true
            val response = mainUseCase.orderHistoryUseCase.getUserOrderHistory(
                OrderHistoryRequest(
                    page = currentPage
                )
            )
            if (response.result.isSuccess()) {
                isLoading.value = false
                if (response.result.data?.orders?.isEmpty() == true && currentPage == 1)
                    isHavingOrders.value = false
                _orders.value =
                    _orders.value.orEmpty() + (response.result.data?.orders ?: emptyList())
                currentPage++
            }
        }
    }


}