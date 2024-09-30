package com.easyEats.ezeats.ui.main.order_history

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.easyEats.ezeats.domain.main.model.request.order_history.OrderHistoryRequest
import com.easyEats.ezeats.domain.main.model.response.order_history.Order
import com.easyEats.ezeats.domain.main.use_case.MainUseCase
import com.easyEats.ezeats.ui.main.main.MainViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OrderHistoryViewModel @Inject constructor(val mainUseCase: MainUseCase) :
    MainViewModel(mainUseCase) {
    private val _orders = MutableLiveData<List<Order?>>()
    val orders: LiveData<List<Order?>> get() = _orders

    private var currentPage = 1

    init {
        loadMoreOrders()
    }

    fun loadMoreOrders() {
        viewModelScope.launch {
            val response = mainUseCase.orderHistoryUseCase.getUserOrderHistory(
                OrderHistoryRequest(
                    page = currentPage
                )
            )
            if (response.result.isSuccess()) {
                _orders.value =
                    _orders.value.orEmpty() + (response.result.data?.orders ?: emptyList())
                currentPage++
            }
        }
    }


}