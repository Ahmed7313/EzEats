package com.easyEats.ezeats.ui.main.restaurant

import android.util.Log
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import com.easyEats.ezeats.MyApplication.Companion.TAG
import com.easyEats.ezeats.base.BaseResponse
import com.easyEats.ezeats.databinding.FragmentRestaurantBinding
import com.easyEats.ezeats.domain.main.model.request.restaurant.RestaurantRequest
import com.easyEats.ezeats.domain.main.model.response.home_response.HotDeal
import com.easyEats.ezeats.domain.main.model.response.restaurant_reponse.RestaurantInfo
import com.easyEats.ezeats.domain.main.use_case.MainUseCase
import com.easyEats.ezeats.ui.main.main.MainViewModel
import com.easyEats.ezeats.ui.main.restaurant.adapter.SpecialOfferAdapter
import com.easyEats.ezeats.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import javax.inject.Inject

@HiltViewModel
class RestaurantViewModel @Inject constructor(
    private val mainUseCase: MainUseCase,
    savedStateHandle: SavedStateHandle
) :
    MainViewModel(mainUseCase) {

    val restaurantId: String? = savedStateHandle["restaurantId"]

    private val _specialOffers = MutableLiveData<Resource<BaseResponse<RestaurantInfo>>>()
    val specialOffers: LiveData<Resource<BaseResponse<RestaurantInfo>>> = _specialOffers

    init {
        getSpecialOffers()
    }

    private fun getSpecialOffers() {
        val flow = createSpecialOffersRequest(
            RestaurantRequest(
                restaurantId!!,
                userLat = userLocationAddressMutableLiveData.value?.lat ?: "0.0",
                userLon = userLocationAddressMutableLiveData.value?.lng ?: "0.0"
            )
        )
        apply(
            flow = flow,
            maxRetries = 2,
            onError = { error ->
                _specialOffers.value =
                    Resource.Error("No Internet Connection")
            },
            onSuccess = { resource ->
                _specialOffers.value = resource
            }
        )
    }


    private fun createSpecialOffersRequest(restaurantRequest: RestaurantRequest): Flow<Resource<BaseResponse<RestaurantInfo>>> =
        channelFlow {
            send(Resource.Loading())
            val result = mainUseCase.restaurantUseCase.getOffersByRestaurantId(restaurantRequest)
            result.handleResponse(
                onSuccess = {
                    send(Resource.Success(result))
                },
                onError = { message, error ->
                    send(Resource.Error(message, error))
                }, onUnauthenticated = {
                    send(Resource.UnAuthenticated())
                }
            )
        }

    fun setRestaurantData(
        binding: FragmentRestaurantBinding,
        specialOfferAdapter: SpecialOfferAdapter,
        offersCount: Int,
        offers: List<HotDeal?>?
    ) {
        binding.tvOffersCount.text =
            offersCount.toString() + " Offers"
        binding.rvSpecialOffers.adapter = specialOfferAdapter
        specialOfferAdapter.submitList(offers)
        if (offers.isNullOrEmpty()) {
            binding.tvOffersCount.visibility = GONE
            binding.rvSpecialOffers.visibility = GONE
            binding.llNoOffers.visibility = VISIBLE
            binding.tvSpecialOffers.visibility = GONE
            binding.tvDescription.visibility = GONE
        } else {
            binding.rvSpecialOffers.visibility = VISIBLE
            binding.llNoOffers.visibility = GONE
            binding.tvSpecialOffers.visibility = VISIBLE
            binding.tvDescription.visibility = VISIBLE
        }
    }
}
