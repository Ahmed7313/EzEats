package com.easyEats.ezeats.ui.main.percent

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.easyEats.ezeats.R
import com.easyEats.ezeats.base.BaseFragment
import com.easyEats.ezeats.databinding.FragmentPercentScreenBinding
import com.easyEats.ezeats.domain.main.model.response.search_response.Category
import com.easyEats.ezeats.ui.main.main.MainViewModel
import com.easyEats.ezeats.ui.main.percent.adapter.OffersCategoryAdapter
import com.easyEats.ezeats.ui.main.search_result.adapter.RestaurantAdapter
import com.easyEats.ezeats.utils.Resource
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class PercentScreenFragment : BaseFragment<FragmentPercentScreenBinding>(),
    OffersCategoryAdapter.OnCategoryClickListener {

    private val mainViewModel: MainViewModel by activityViewModels()
    private val percentViewModel: PercentViewModel by viewModels()
    private lateinit var restaurantAdapter: RestaurantAdapter
    private lateinit var cateAdapter: OffersCategoryAdapter
    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentPercentScreenBinding.inflate(layoutInflater)

    override fun setUpViews() {
        restaurantAdapter = RestaurantAdapter()
        cateAdapter = OffersCategoryAdapter(this)
        with(binding) {
            tvLocation.setOnClickListener { findNavController().navigate(R.id.location_bottom_sheet) }
            ivSearch.setOnClickListener { findNavController().navigate(R.id.searchHistoryFragment) }
        }

        listenToObservers()
    }

    private fun listenToObservers() {
        mainViewModel.userLocationAddressMutableLiveData.observe(viewLifecycleOwner) {
            it?.let {
                binding.tvLocation.text = it.address.toString()
            }
        }
        percentViewModel.restaurantResultLiveData.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Error -> {}
                is Resource.Loading -> {}
                is Resource.Success -> {
                    binding.rvRestaurant.adapter = restaurantAdapter
                    restaurantAdapter.submitList(resource.data?.result?.data)
                }

                is Resource.UnAuthenticated -> {}
            }
        }

        percentViewModel.categoryResultLiveData.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Error -> {}
                is Resource.Loading -> {}
                is Resource.Success -> {
                    binding.rvCategories.adapter = cateAdapter
                    val categories = resource.data?.result?.data as MutableList<Category>
                    categories.removeIf {
                        it.categoryId == "eqvWcHcHaD"
                    }
                    val itemAll = Category("eqvWcHcHaD", "All", "All", null)
                    categories.add(0, itemAll)
                    cateAdapter.submitList(categories)
                }

                is Resource.UnAuthenticated -> {}
            }
        }
    }

    override fun onCategoryClicked(category: Category) {
        if (category.categoryId == "eqvWcHcHaD")
            percentViewModel.getRestaurantsRestaurantResult("")
        else
            percentViewModel.getRestaurantsRestaurantResult(category.categoryId!!)
    }


}