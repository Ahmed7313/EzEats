package com.easyEats.ezeats.ui.main.offers

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.easyEats.ezeats.R
import com.easyEats.ezeats.base.BaseFragment
import com.easyEats.ezeats.databinding.FragmentPercentScreenBinding
import com.easyEats.ezeats.domain.main.model.response.restaurant_reponse.Restaurant
import com.easyEats.ezeats.domain.main.model.response.search_response.Category
import com.easyEats.ezeats.ui.main.main.MainViewModel
import com.easyEats.ezeats.ui.main.offers.adapter.OffersCategoryAdapter
import com.easyEats.ezeats.ui.main.search_result.adapter.OfferAdapter
import com.easyEats.ezeats.ui.main.search_result.adapter.RestaurantAdapter
import com.easyEats.ezeats.utils.Resource
import dagger.hilt.android.AndroidEntryPoint
import es.dmoral.toasty.Toasty


@AndroidEntryPoint
class OffersScreenFragment : BaseFragment<FragmentPercentScreenBinding>(),
    OffersCategoryAdapter.OnCategoryClickListener, RestaurantAdapter.OnRestaurantClicked,
    OfferAdapter.OnOfferClicked {

    private val mainViewModel: MainViewModel by activityViewModels()
    private val offersViewModel: OffersViewModel by viewModels()
    private lateinit var restaurantAdapter: RestaurantAdapter
    private lateinit var cateAdapter: OffersCategoryAdapter


    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentPercentScreenBinding.inflate(layoutInflater)

    override fun setUpViews() {
        restaurantAdapter = RestaurantAdapter(this, this)
        cateAdapter = OffersCategoryAdapter(this)
        with(binding) {
            tvLocation.setOnClickListener { findNavController().navigate(R.id.location_bottom_sheet) }
            ivSearch.setOnClickListener { findNavController().navigate(R.id.goToSearchHistoryFragment) }
        }

        binding.srGetOffers.setOnRefreshListener{
            offersViewModel.getRestaurantsResult("")
            offersViewModel.getCategoriesRestaurantResult()
        }

        listenToObservers()
    }


    private fun listenToObservers() {
        mainViewModel.userLocationAddressMutableLiveData.observe(viewLifecycleOwner) {
            it?.let {
                binding.tvLocation.text =
                    ("You're at " + it.address) ?: getString(R.string.select_location)
                offersViewModel.getRestaurantsResult("")
                offersViewModel.getCategoriesRestaurantResult()
            }
        }
        offersViewModel.restaurantResultLiveData.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Error -> {
                    hideMainLoading()
                    Toasty.error(requireContext(),resource.message,Toast.LENGTH_LONG).show()
                }

                is Resource.Loading -> {
                    showMainLoading()
                }

                is Resource.Success -> {
                    binding.srGetOffers.isRefreshing = false
                    offersViewModel.setRestaurantsData(requireContext(),binding, restaurantAdapter, resource)
                    hideMainLoading()

                }

                is Resource.UnAuthenticated -> {
                    hideMainLoading()
                }
            }
        }

        offersViewModel.categoryResultLiveData.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Error -> {
                    Toasty.error(requireContext(),resource.message,Toast.LENGTH_LONG).show()
                    hideMainLoading()
                }

                is Resource.Loading -> {
                    binding.srGetOffers.isRefreshing = false
                    showMainLoading()
                }

                is Resource.Success -> {
                    hideMainLoading()
                    offersViewModel.setDataOfCategories(binding, cateAdapter, resource)
                }

                is Resource.UnAuthenticated -> {
                    hideMainLoading()
                }
            }
        }
    }


    override fun onCategoryClicked(category: Category) {
        if (category.categoryId == "eqvWcHcHaD")
            offersViewModel.getRestaurantsResult("")
        else
            offersViewModel.getRestaurantsResult(category.categoryId!!)
    }

    override fun onRestaurantClickListener(restaurant: Restaurant) {
        findNavController().navigate(
            OffersScreenFragmentDirections.goToRestaurantFragment(
                restaurant.id!!
            )
        )
    }

    override fun onOfferClicked(offerId: String) {
        findNavController().navigate(OffersScreenFragmentDirections.goToRedeemScreen(offerId))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        hideMainLoading()
    }


}