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

    // LiveData to hold the list of orders. Exposed as immutable to the UI.
    private val _orders = MutableLiveData<List<Order?>>()
    val orders: LiveData<List<Order?>> get() = _orders

    // LiveData to indicate whether the user has any orders.
    val isHavingOrders = MutableLiveData(true)

    // LiveData to indicate the loading state of the data.
    val isLoading = MutableLiveData(false)

    // To track the current page number for pagination.
    private var currentPage = 1

    // Initialization block to load the first set of orders.
    init {
        loadMoreOrders()
    }

    /**
     * Function to load more orders with pagination.
     * It fetches orders based on the current page and appends them to the existing list.
     */
    fun loadMoreOrders() {
        viewModelScope.launch {
            // Set the loading state to true while fetching the orders.
            isLoading.value = true

            // Make the network request to fetch order history for the current page.
            val response = mainUseCase.orderHistoryUseCase.getUserOrderHistory(
                OrderHistoryRequest(
                    page = currentPage
                )
            )

            // Check if the response is successful.
            if (response.result.isSuccess()) {
                // Hide the loading indicator after data is fetched.
                isLoading.value = false

                // If the fetched orders list is empty and it's the first page, update the state.
                if (response.result.data?.orders?.isEmpty() == true && currentPage == 1)
                    isHavingOrders.value = false

                // Append the fetched orders to the existing list.
                _orders.value =
                    _orders.value.orEmpty() + (response.result.data?.orders ?: emptyList())

                // Increment the current page for the next request.
                currentPage++
            }
        }
    }
}
