package com.easyEats.ezeats.ui.main.restaurant

import android.util.Log
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.easyEats.ezeats.MyApplication.Companion.TAG
import com.easyEats.ezeats.base.BaseResponse
import com.easyEats.ezeats.databinding.FragmentRestaurantBinding
import com.easyEats.ezeats.domain.main.model.request.restaurant.RestaurantMenuRequest
import com.easyEats.ezeats.domain.main.model.request.restaurant.RestaurantRequest
import com.easyEats.ezeats.domain.main.model.response.home_response.HotDeal
import com.easyEats.ezeats.domain.main.model.response.restaurant_menu.RestaurantMenuResponse
import com.easyEats.ezeats.domain.main.model.response.restaurant_reponse.RestaurantInfo
import com.easyEats.ezeats.domain.main.use_case.MainUseCase
import com.easyEats.ezeats.ui.main.main.MainViewModel
import com.easyEats.ezeats.ui.main.restaurant.adapter.SpecialOfferAdapter
import com.easyEats.ezeats.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel responsible for managing the restaurant data, including fetching special offers for the restaurant.
 * It interacts with the use case layer to fetch the special offers and exposes them as LiveData.
 */
@HiltViewModel
class RestaurantViewModel @Inject constructor(
    private val mainUseCase: MainUseCase,
    savedStateHandle: SavedStateHandle
) : MainViewModel(mainUseCase) {

    // The restaurant ID, obtained from the SavedStateHandle.
    val restaurantId: String? = savedStateHandle["restaurantId"]

    // LiveData that holds the resource of special offers.
    private val _specialOffers = MutableLiveData<Resource<BaseResponse<RestaurantInfo>>>()
    val specialOffers: LiveData<Resource<BaseResponse<RestaurantInfo>>> = _specialOffers

    private val _restaurantMenu = MutableLiveData<Resource<BaseResponse<RestaurantMenuResponse>>>()
    val restaurantMenu: LiveData<Resource<BaseResponse<RestaurantMenuResponse>>> = _restaurantMenu

    /**
     * Fetches special offers for the restaurant by retrieving the current user's location.
     * If the location is available, it creates a request and fetches the special offers for the restaurant.
     */
    fun getSpecialOffers(restaurantId: String?) {
        viewModelScope.launch {
            fetchCurrentAddress().collect {
                if (it?.lat?.isNullOrEmpty()?.not() == true) {
                    // Create a request to get special offers for the restaurant.
                    val flow = createSpecialOffersRequest(
                        RestaurantRequest(
                            restaurantId!!,
                            userLat = "30.0145125310423", //it.lat ?: "" ,
                            userLon = "30.975965400415998" //it.lng ?: ""
                        )
                    )
                    // Apply the request with retry logic.
                    apply(
                        flow = flow,
                        maxRetries = 2,
                        onError = { error ->
                            // Update the LiveData with an error state if there's an issue.
                            _specialOffers.value = Resource.Error("No Internet Connection")
                        },
                        onSuccess = { resource ->
                            // Update the LiveData with the fetched special offers.
                            _specialOffers.value = resource
                        }
                    )
                }
            }
        }
    }

    fun getRestaurantMenu(restaurantId: String?) {
        viewModelScope.launch {
            // Create a request to get special offers for the restaurant.
            val flow = createRestaurantMenuRequest(
                RestaurantMenuRequest(
                    restaurantId!!
                )
            )
            // Apply the request with retry logic.
            apply(
                flow = flow,
                maxRetries = 2,
                onError = { error ->
                    // Update the LiveData with an error state if there's an issue.
                    _restaurantMenu.value = Resource.Error("No Internet Connection")
                },
                onSuccess = { resource ->
                    // Update the LiveData with the fetched special offers.
                    _restaurantMenu.value = resource
                }
            )

        }
    }

    /**
     * Creates a Flow that makes the request for special offers by restaurant ID.
     * Sends the appropriate response (loading, success, error, or unauthenticated).
     *
     * @param restaurantRequest The request object containing the restaurant ID and user's location.
     */
    private fun createSpecialOffersRequest(restaurantRequest: RestaurantRequest): Flow<Resource<BaseResponse<RestaurantInfo>>> =
        channelFlow {
            send(Resource.Loading()) // Emit loading state.
            val result = mainUseCase.restaurantUseCase.getOffersByRestaurantId(restaurantRequest)
            // Handle the response and send the result back to the Flow.
            result.handleResponse(
                onSuccess = {
                    send(Resource.Success(result)) // Emit success state with the result.
                },
                onError = { message, error ->
                    send(
                        Resource.Error(
                            message,
                            error
                        )
                    ) // Emit error state with the message and error.
                },
                onUnauthenticated = {
                    send(Resource.UnAuthenticated()) // Emit unauthenticated state if the user is not authenticated.
                }
            )
        }

    private fun createRestaurantMenuRequest(restaurantMenuRequest: RestaurantMenuRequest): Flow<Resource<BaseResponse<RestaurantMenuResponse>>> =
        channelFlow {
            send(Resource.Loading()) // Emit loading state.
            val result = mainUseCase.restaurantUseCase.getRestaurantMenu(restaurantMenuRequest)
            // Handle the response and send the result back to the Flow.
            result.handleResponse(
                onSuccess = {
                    send(Resource.Success(result)) // Emit success state with the result.
                },
                onError = { message, error ->
                    send(
                        Resource.Error(
                            message,
                            error
                        )
                    ) // Emit error state with the message and error.
                },
                onUnauthenticated = {
                    send(Resource.UnAuthenticated()) // Emit unauthenticated state if the user is not authenticated.
                }
            )
        }

    /**
     * Sets the restaurant data into the UI, including the special offers count, special offers list, and visibility
     * of various UI elements depending on whether the restaurant has offers or not.
     *
     * @param binding The binding for the fragment's layout.
     * @param specialOfferAdapter The adapter used to display the special offers in the RecyclerView.
     * @param offersCount The count of available offers.
     * @param offers The list of special offers to display.
     */
    fun setRestaurantData(
        binding: FragmentRestaurantBinding,
        specialOfferAdapter: SpecialOfferAdapter,
        offersCount: Int,
        offers: List<HotDeal?>?
    ) {
        // Set the offers count text.
        binding.tvOffersCount.text = offersCount.toString() + " Offers"

        // Set the adapter for the RecyclerView.
//        binding.rvSpecialOffers.adapter = specialOfferAdapter

        // Submit the list of special offers to the adapter.
        specialOfferAdapter.submitList(offers)

        // Show or hide views based on whether there are offers.
        if (offers.isNullOrEmpty()) {
            binding.tvOffersCount.visibility = GONE
//            binding.rvSpecialOffers.visibility = GONE
//            binding.llNoOffers.visibility = VISIBLE
            binding.tvSpecialOffers.visibility = GONE
            binding.tvDescription.visibility = GONE
        } else {
//            binding.rvSpecialOffers.visibility = VISIBLE
            binding.llNoOffers.visibility = GONE
            binding.tvSpecialOffers.visibility = VISIBLE
            binding.tvDescription.visibility = VISIBLE
        }
    }

}
