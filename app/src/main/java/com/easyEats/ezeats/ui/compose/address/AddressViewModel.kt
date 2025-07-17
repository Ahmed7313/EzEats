package com.easyEats.ezeats.ui.compose.address

import androidx.lifecycle.viewModelScope
import com.easyEats.ezeats.base.BaseResponse
import com.easyEats.ezeats.domain.main.model.request.address.AddressListRequest
import com.easyEats.ezeats.domain.main.model.request.order.umbrella.SaveUmbrellaResponse
import com.easyEats.ezeats.domain.main.model.response.address.add.AddAddressRequest
import com.easyEats.ezeats.domain.main.model.response.address.add.AddAddressResponse
import com.easyEats.ezeats.domain.main.model.response.address.add.AddressModel
import com.easyEats.ezeats.domain.main.model.response.address.add.SaveAddressRequest
import com.easyEats.ezeats.domain.main.model.response.address.list.AddressListResponse
import com.easyEats.ezeats.domain.main.use_case.MainUseCase
import com.easyEats.ezeats.ui.main.main.MainViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel responsible for managing the restaurant data, including fetching special offers for the restaurant.
 * It interacts with the use case layer to fetch the special offers and exposes them as LiveData.
 */
@HiltViewModel
class AddressViewModel @Inject constructor(
    private val mainUseCase: MainUseCase,
) : MainViewModel(mainUseCase) {
    val TAG = "AddressViewModel"

    private val _showLoading = MutableStateFlow<Boolean>(false)
    val showLoading = _showLoading.asStateFlow()

    private val _showLoadingAdd = MutableStateFlow<Boolean>(false)
    val showLoadingAdd = _showLoadingAdd.asStateFlow()



    private val _addressList = MutableStateFlow<List<AddressModel>>(emptyList())
    val addressList: StateFlow<List<AddressModel>> = _addressList.asStateFlow()

    fun getAddressList(userId: String) : Flow<List<AddressModel?>?> {
        return flow {
            getAddressListFlow(userId).collect { response ->
                try{
                    response?.addressListResult?.addresses?.let { result ->
                        emit(result)
                    }
                }catch (e: Exception){
                    emit(null)
                }
            }
        }
    }

    fun getAddressListFlow(userId: String) : Flow<AddressListResponse?>  {
        _showLoading.value = true
        return flow {
            try{
                val result = mainUseCase.homeUseCase.addressList(AddressListRequest(userId))
                _showLoading.value = false
                emit(result)
            }catch (Ex: Exception){
                _showLoading.value = false
                emit(null)
            }
        }
    }


    fun addAddress(request: AddAddressRequest) : Flow<AddAddressResponse?>  {
        _showLoadingAdd.value = true
        return flow {
            try{
                val result = mainUseCase.homeUseCase.addAddress(request)
                _showLoadingAdd.value = false
                emit(result)
            }catch (Ex: Exception){
                _showLoadingAdd.value = false
                emit(null)
            }
        }
    }

    fun saveAddressCart(request: SaveAddressRequest) : Flow<SaveUmbrellaResponse?>  {
        _showLoading.value = true
        return flow {
            try{
                val result = mainUseCase.homeUseCase.saveAddress(request)
                _showLoading.value = false
                emit(result)
            }catch (Ex: Exception){
                _showLoading.value = false
                emit(null)
            }
        }
    }

}
