package com.easyEats.ezeats.ui.main.search_result

import android.util.Log
import android.view.LayoutInflater
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.easyEats.ezeats.MyApplication.Companion.TAG
import com.easyEats.ezeats.R
import com.easyEats.ezeats.base.BaseBottomSheetFragment
import com.easyEats.ezeats.databinding.FragmentSearchResultBinding
import com.easyEats.ezeats.domain.main.model.response.home_response.HotDeal
import com.easyEats.ezeats.domain.main.model.response.restaurant_reponse.Restaurant
import com.easyEats.ezeats.ui.main.home.adapter.hot_deals.HotDealsAdapter
import com.easyEats.ezeats.ui.main.main.MainViewModel
import com.easyEats.ezeats.ui.main.search_result.adapter.RestaurantAdapter
import com.easyEats.ezeats.utils.Resource
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchResultFragment : BaseBottomSheetFragment<FragmentSearchResultBinding>() {

    private val mainViewModel: MainViewModel by activityViewModels()
    private val searchResultViewModel: SearchResultViewModel by viewModels()
    private lateinit var restaurantAdapter: RestaurantAdapter
    private lateinit var hotDealsAdapter: HotDealsAdapter

    override fun inflateBinding(
        inflater: LayoutInflater, container: ViewGroup?
    ) = FragmentSearchResultBinding.inflate(layoutInflater)

    override fun setUpViews() {
        restaurantAdapter = RestaurantAdapter()
        hotDealsAdapter = HotDealsAdapter()
        binding.rvRestaurantSearch.adapter = restaurantAdapter
        binding.searchResultViewModel = searchResultViewModel
        binding.mcvSearch.setOnClickListener {
            dismiss()
        }
        binding.tvLocation.setOnClickListener {
            findNavController().navigate(R.id.location_bottom_sheet)
        }
    }

    override fun listenToObservers() {
        mainViewModel.userLocationAddressMutableLiveData.observe(viewLifecycleOwner) {
            it?.let {
                binding.tvLocation.text = it.address ?: getString(R.string.select_location)
            }
        }

        searchResultViewModel.viewType.observe(viewLifecycleOwner) { viewType ->
            when (viewType) {
                ViewType.Restaurant -> {
                    binding.ivRestaurantType.setBackgroundResource(R.drawable.selected_button_background)
                    binding.ivOfferType.setBackgroundResource(R.drawable.unselected_button_background)
                    searchResultViewModel.getSearchRestaurantResult(viewType.viewType)
                }

                ViewType.Offer -> {
                    binding.ivRestaurantType.setBackgroundResource(R.drawable.unselected_button_background)
                    binding.ivOfferType.setBackgroundResource(R.drawable.selected_button_background)
                    searchResultViewModel.getSearchOfferResult(viewType.viewType)

                }
            }
        }


        searchResultViewModel.searchResultLiveData.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Error -> {}
                is Resource.Loading -> {}
                is Resource.Success -> {
                    binding.rvRestaurantSearch.visibility = VISIBLE
                    binding.rvOfferSearch.visibility = GONE
                    binding.rvRestaurantSearch.adapter = restaurantAdapter
                    restaurantAdapter.submitList(resource.data?.result?.data)
                }

                is Resource.UnAuthenticated -> {}
            }
        }

        searchResultViewModel.searchOfferResultLiveData.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Error -> {
                    Log.d(TAG, "listenToObservers: ${resource.message}")
                }

                is Resource.Loading -> {}
                is Resource.Success -> {
                    binding.rvRestaurantSearch.visibility = GONE
                    binding.rvOfferSearch.visibility = VISIBLE
                    binding.rvOfferSearch.adapter = hotDealsAdapter
                    binding.rvOfferSearch.layoutManager=GridLayoutManager(requireContext(),2)
                    hotDealsAdapter.submitList(resource.data?.result?.data)
                }

                is Resource.UnAuthenticated -> {}
            }
        }
    }

}