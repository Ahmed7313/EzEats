package com.easyEats.ezeats.ui.main.search_result

import android.content.Context
import android.util.Log
import android.view.View
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat.getString
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.recyclerview.widget.GridLayoutManager
import com.easyEats.ezeats.MyApplication.Companion.TAG
import com.easyEats.ezeats.R
import com.easyEats.ezeats.base.BaseResponse
import com.easyEats.ezeats.databinding.FragmentSearchResultBinding
import com.easyEats.ezeats.domain.main.model.request.search.SearchRequest
import com.easyEats.ezeats.domain.main.model.response.home_response.HotDeal
import com.easyEats.ezeats.domain.main.model.response.restaurant_reponse.Restaurant
import com.easyEats.ezeats.domain.main.use_case.MainUseCase
import com.easyEats.ezeats.ui.main.home.adapter.hot_deals.HotDealsAdapter
import com.easyEats.ezeats.ui.main.main.MainViewModel
import com.easyEats.ezeats.ui.main.search_result.adapter.RestaurantAdapter
import com.easyEats.ezeats.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import javax.inject.Inject

/**
 * ViewModel responsible for managing the search results for Restaurants and Hot Deals.
 * This ViewModel handles the data flow and UI state changes based on the search key
 * and selected view type (Restaurant or Offer).
 *
 * @property mainUseCase The use case that provides search functionalities.
 * @property savedStateHandle Handle to saved state for storing the search key.
 */
@HiltViewModel
class SearchResultViewModel @Inject constructor(
    val mainUseCase: MainUseCase, savedStateHandle: SavedStateHandle
) : MainViewModel(mainUseCase) {

    // The search key extracted from the saved state, used to perform search queries.
    val searchKey: String? = savedStateHandle["searchKey"]

    // LiveData that holds the current view type (Restaurant or Offer).
    val viewType = MutableLiveData(ViewType.Restaurant)

    // LiveData that holds the search results for Restaurants.
    private val _searchResultMutableLiveData =
        MutableLiveData<Resource<BaseResponse<List<Restaurant>>>>()
    val searchResultLiveData: LiveData<Resource<BaseResponse<List<Restaurant>>>> get() = _searchResultMutableLiveData

    // LiveData that holds the search results for Hot Deals.
    private val _searchOfferResultMutableLiveData =
        MutableLiveData<Resource<BaseResponse<List<HotDeal>>>>()
    val searchOfferResultLiveData: LiveData<Resource<BaseResponse<List<HotDeal>>>> get() = _searchOfferResultMutableLiveData

    // Initializer block to fetch the initial search results for Restaurants.
    init {
        getSearchRestaurantResult()
    }

    /**
     * Sets the view type to Restaurant.
     */
    fun setRestaurantViewType() {
        viewType.value = ViewType.Restaurant
    }

    /**
     * Sets the view type to Offer.
     */
    fun setOfferViewType() {
        viewType.value = ViewType.Offer
    }

    /**
     * Toggles the view type between Restaurant and Offer.
     */
    fun changeViewType() {
        when (viewType.value) {
            ViewType.Restaurant -> setOfferViewType()
            ViewType.Offer -> setRestaurantViewType()
            null -> {}
        }
    }

    /**
     * Fetches the search results for Restaurants based on the search key and view type.
     *
     * @param viewType The type of view for which to fetch results, default is "restaurant".
     */
    private fun getSearchRestaurantResult(viewType: String = "restaurant") {
        val flow = createSearchResultRequest(
            SearchRequest(
                searchKey ?: "", viewType,
                userLat = userLocationAddressMutableLiveData.value?.lat.toString(),
                userLon = userLocationAddressMutableLiveData.value?.lng.toString()
            )
        )
        apply(flow = flow, maxRetries = 2, onError = { error ->
            _searchResultMutableLiveData.value =
                Resource.Error(error.localizedMessage ?: "Unknown error")
        }, onSuccess = { resource ->
            _searchResultMutableLiveData.value = resource
        })
    }

    /**
     * Creates a flow for fetching the restaurant search results.
     *
     * @param searchRequest The request containing search parameters.
     * @return A flow emitting the search result resource.
     */
    private fun createSearchResultRequest(searchRequest: SearchRequest): Flow<Resource<BaseResponse<List<Restaurant>>>> =
        channelFlow {
            send(Resource.Loading())
            val homeResult = mainUseCase.searchUseCase.searchRestaurant(searchRequest)
            homeResult.handleResponse(onSuccess = {
                send(Resource.Success(homeResult))
            }, onError = { message, error ->
                send(Resource.Error(message, error))
            }, onUnauthenticated = {
                send(Resource.UnAuthenticated())
            })
        }

    /**
     * Fetches the search results for Hot Deals based on the search key and view type.
     *
     * @param viewType The type of view for which to fetch results, default is "offers".
     */
    private fun getSearchOfferResult(viewType: String = "offers") {
        val flow = createSearchOfferResultRequest(
            SearchRequest(
                searchKey ?: "",
                viewType,
                userLat = userLocationAddressMutableLiveData.value?.lat.toString(),
                userLon = userLocationAddressMutableLiveData.value?.lng.toString()
            )
        )
        apply(flow = flow, maxRetries = 2, onError = { error ->
            _searchOfferResultMutableLiveData.value =
                Resource.Error(error.localizedMessage ?: "Unknown error")
        }, onSuccess = { resource ->
            _searchOfferResultMutableLiveData.value = resource
        })
    }

    /**
     * Creates a flow for fetching the hot deals search results.
     *
     * @param searchRequest The request containing search parameters.
     * @return A flow emitting the search result resource.
     */
    private fun createSearchOfferResultRequest(searchRequest: SearchRequest): Flow<Resource<BaseResponse<List<HotDeal>>>> =
        channelFlow {
            send(Resource.Loading())
            val homeResult = mainUseCase.searchUseCase.searchOffer(searchRequest)
            homeResult.handleResponse(onSuccess = {
                send(Resource.Success(homeResult))
            }, onError = { message, error ->
                send(Resource.Error(message, error))
            }, onUnauthenticated = {
                send(Resource.UnAuthenticated())
            })
        }

    /**
     * Updates the UI with the fetched hot deals data.
     *
     * @param context The context for accessing resources.
     * @param binding The binding object for the fragment.
     * @param hotDealsAdapter The adapter for the hot deals RecyclerView.
     * @param resource The resource containing the hot deals data.
     */
    fun setOffersData(
        context: Context,
        binding: FragmentSearchResultBinding,
        hotDealsAdapter: HotDealsAdapter,
        resource: Resource.Success<BaseResponse<List<HotDeal>>>
    ) {
        binding.rvRestaurantSearch.visibility = View.GONE
        binding.rvOfferSearch.visibility = View.VISIBLE
        binding.rvOfferSearch.adapter = hotDealsAdapter
        binding.llExplore.visibility =
            if (resource.data?.result?.data?.isEmpty() == true) View.VISIBLE else View.GONE
        binding.btnExplore.text = getString(context, R.string.explore_our_restaurants)
        binding.rvOfferSearch.layoutManager = GridLayoutManager(context, 2)
        hotDealsAdapter.submitList(resource.data?.result?.data)
    }

    /**
     * Updates the UI with the fetched restaurant data.
     *
     * @param context The context for accessing resources.
     * @param binding The binding object for the fragment.
     * @param restaurantAdapter The adapter for the restaurant RecyclerView.
     * @param resource The resource containing the restaurant data.
     */
    fun setRestaurantsData(
        context: Context,
        binding: FragmentSearchResultBinding,
        restaurantAdapter: RestaurantAdapter,
        resource: Resource.Success<BaseResponse<List<Restaurant>>>
    ) {
        binding.rvRestaurantSearch.visibility = View.VISIBLE
        binding.rvOfferSearch.visibility = View.GONE
        binding.rvRestaurantSearch.adapter = restaurantAdapter
        binding.llExplore.visibility =
            if (resource.data?.result?.data?.isEmpty() == true) View.VISIBLE else View.GONE
        binding.btnExplore.text = getString(context, R.string.explore_our_offers)
        restaurantAdapter.submitList(resource.data?.result?.data)
    }

    /**
     * Updates the UI components based on the selected view type.
     *
     * @param binding The binding object for the fragment.
     * @param viewType The current view type (Restaurant or Offer).
     */
    fun listenToViewType(binding: FragmentSearchResultBinding, viewType: ViewType) {
        when (viewType) {
            ViewType.Restaurant -> {
                binding.ivRestaurantType.setBackgroundResource(R.drawable.selected_button_background)
                binding.ivOfferType.setBackgroundResource(R.drawable.unselected_button_background)
                getSearchRestaurantResult(viewType.viewType)
            }

            ViewType.Offer -> {
                binding.ivRestaurantType.setBackgroundResource(R.drawable.unselected_button_background)
                binding.ivOfferType.setBackgroundResource(R.drawable.selected_button_background)
                getSearchOfferResult(viewType.viewType)
            }
        }
    }
}
