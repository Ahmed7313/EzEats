package com.easyEats.ezeats.ui.main.voucher

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.easyEats.ezeats.base.BaseResponse
import com.easyEats.ezeats.domain.main.model.response.user_info.UserInfoResponse
import com.easyEats.ezeats.domain.main.model.response.voucher.VoucherResponse
import com.easyEats.ezeats.domain.main.use_case.MainUseCase
import com.easyEats.ezeats.ui.main.main.MainViewModel
import com.easyEats.ezeats.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import javax.inject.Inject
/**
 * [VoucherViewModel] is a ViewModel responsible for managing voucher-related data in the app.
 * It communicates with the [MainUseCase] to fetch voucher results and exposes them as LiveData.
 * The ViewModel also manages the state of the UI through `voucherResultLiveData` and `isHavingVouchers`.
 */
@HiltViewModel
class VoucherViewModel @Inject constructor(private val mainUseCase: MainUseCase) :
    MainViewModel(mainUseCase) {

    // LiveData to represent whether the user has vouchers
    val isHavingVouchers = MutableLiveData(false)

    // MutableLiveData to hold the resource state of voucher results
    private val _voucherResultMutableLiveData =
        MutableLiveData<Resource<BaseResponse<VoucherResponse>>>()

    // Exposed LiveData for voucher result to be observed by the UI
    val voucherResultLiveData: LiveData<Resource<BaseResponse<VoucherResponse>>> get() = _voucherResultMutableLiveData

    /**
     * Initializes the ViewModel and triggers the voucher result fetch operation.
     */
    init {
        getVoucherResult()
    }

    /**
     * Fetches the voucher result by making a request through the [createVoucherResultRequest] method.
     * Handles retry logic and updates the LiveData with the appropriate resource state.
     */
    private fun getVoucherResult() {
        val flow = createVoucherResultRequest()
        apply(flow = flow, maxRetries = 2, onError = { error ->
            // Handle error scenario (no internet connection) by updating the LiveData
            _voucherResultMutableLiveData.value =
                Resource.Error("No Internet Connection")
        }, onSuccess = { resource ->
            // On successful response, update the LiveData with the result
            _voucherResultMutableLiveData.value = resource
        })
    }

    /**
     * Creates a Flow that makes the API request to fetch the voucher results.
     * It sends the request state (Loading, Success, Error, or Unauthenticated) to the channel.
     */
    private fun createVoucherResultRequest(): Flow<Resource<BaseResponse<VoucherResponse>>> =
        channelFlow {
            // Initially, send a loading state
            send(Resource.Loading())

            // Make the actual API request to fetch user rewards
            val result = mainUseCase.voucherUseCase.getUserRewardsMobile()

            // Handle the response and send the appropriate state
            result.handleResponse(onSuccess = {
                send(Resource.Success(result))
            }, onError = { message, error ->
                send(Resource.Error(message, error))
            }, onUnauthenticated = {
                send(Resource.UnAuthenticated())
            })
        }
}
