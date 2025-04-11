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

@HiltViewModel
class VoucherViewModel @Inject constructor(private val mainUseCase: MainUseCase) :
    MainViewModel(mainUseCase) {
    val isHavingVouchers = MutableLiveData(false)


    private val _voucherResultMutableLiveData =
        MutableLiveData<Resource<BaseResponse<VoucherResponse>>>()
    val voucherResultLiveData: LiveData<Resource<BaseResponse<VoucherResponse>>> get() = _voucherResultMutableLiveData

    init {
        getVoucherResult()
    }

    private fun getVoucherResult() {
        val flow = createVoucherResultRequest()
        apply(flow = flow, maxRetries = 2, onError = { error ->
            _voucherResultMutableLiveData.value =
                Resource.Error("No Internet Connection")
        }, onSuccess = { resource ->
            _voucherResultMutableLiveData.value = resource
        })
    }


    private fun createVoucherResultRequest(): Flow<Resource<BaseResponse<VoucherResponse>>> =
        channelFlow {
            send(Resource.Loading())
            val result = mainUseCase.voucherUseCase.getUserRewardsMobile()
            result.handleResponse(onSuccess = {
                send(Resource.Success(result))
            }, onError = { message, error ->
                send(Resource.Error(message, error))
            }, onUnauthenticated = {
                send(Resource.UnAuthenticated())
            })
        }
}