package com.easyEats.ezeats.ui.main.offers

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.daman.edman.screens.components.AppSpacer
import com.daman.edman.screens.components.AppTextNormal
import com.easyEats.ezeats.R
import com.easyEats.ezeats.base.BaseFragment
import com.easyEats.ezeats.databinding.FragmentPercentScreenBinding
import com.easyEats.ezeats.domain.main.model.response.restaurant_reponse.RestaurantInfo
import com.easyEats.ezeats.domain.main.model.response.restaurant_reponse.new_restaurants_reponse.RestaurantsResponseItem
import com.easyEats.ezeats.domain.main.model.response.restaurant_reponse.result.RestaurantListResult
import com.easyEats.ezeats.domain.main.model.response.search_response.Category
import com.easyEats.ezeats.ui.compose.offers.OffersList
import com.easyEats.ezeats.ui.compose.offers.SelectionButton
import com.easyEats.ezeats.ui.compose.resturant.CompoundBottomSheet
import com.easyEats.ezeats.ui.compose.resturant.PAYMENT_INTEGRATION_TYPE
import com.easyEats.ezeats.ui.compose.theme.AppColor
import com.easyEats.ezeats.ui.large
import com.easyEats.ezeats.ui.main.main.MainViewModel
import com.easyEats.ezeats.ui.main.offers.adapter.OffersCategoryAdapter
import com.easyEats.ezeats.utils.Constants
import com.easyEats.ezeats.utils.Resource
import dagger.hilt.android.AndroidEntryPoint
import es.dmoral.toasty.Toasty
import kotlinx.coroutines.launch

@AndroidEntryPoint
class OffersScreenFragment : BaseFragment<FragmentPercentScreenBinding>(),
    OffersCategoryAdapter.OnCategoryClickListener {

    //override val shouldRetainView: Boolean = true

    private val mainViewModel: MainViewModel by activityViewModels()
    private val offersViewModel: OffersViewModel by viewModels()
    private lateinit var cateAdapter: OffersCategoryAdapter

    val TAG = "OffersScreenFragment"
    lateinit var restaurantListFull: RestaurantListResult
    lateinit var restaurantsSelected: RestaurantListResult

    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentPercentScreenBinding.inflate(inflater, container, false)

    @SuppressLint("SetTextI18n")
    override fun setUpViews() {
        binding.tvLocation.setOnClickListener {
            findNavController().navigate(R.id.location_bottom_sheet)
        }
        binding.ivSearch.setOnClickListener {
            findNavController().navigate(R.id.goToSearchHistoryFragment)
        }
        drawSelectedCompoundOrLocation()
        getCategoriesAndRestaurants()
        handleBackDirection()
        Log.d(TAG, "setUpViews: compound ${mainViewModel.compoundId}")

        lifecycleScope.launch {
            mainViewModel.compoundIdValue.observe(viewLifecycleOwner) {
                drawSelectedCompoundOrLocation()
            }
        }
    }

    fun drawSelectedCompoundOrLocation() {
        Log.d(TAG, "drawSelectedCompoundOrLocation: here compoundOrLocation")
        if (mainViewModel.compoundId.isNotEmpty()) {
            binding.tvLocation.text =
                getString(R.string.your_are_at) + " " + mainViewModel.compoundName
        } else {
            Log.d(TAG, "drawSelectedCompoundOrLocation: here")
            lifecycleScope.launch {
                mainViewModel.userLocationAddressMutableLiveData.collect { loc ->
                    binding.tvLocation.text = loc?.address?.let { "You're at $it" }
                        ?: getString(R.string.select_location)
                    //osama
                    offersViewModel.getRestaurantsResult(
                        "",
                        loc?.lat,
                        loc?.lng,
//                    30.0145125310423.toString(),
//                    30.975965400415998.toString(),
                        mainViewModel.compoundId,
                    )
                }
            }

        }
    }

    private fun getCategoriesAndRestaurants() {
        offersViewModel.getCategoriesRestaurantResult()
        getRestaurantResults()
    }

    private fun getRestaurantResults() {
        offersViewModel.getRestaurantsResult(
            "",
            mainViewModel.userLocationAddressMutableLiveData.value?.lat,
            mainViewModel.userLocationAddressMutableLiveData.value?.lng,
            mainViewModel.compoundId
        )
    }

    private fun handleBackDirection() {
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    findNavController().navigate(R.id.homeScreenFragment)
                }
            }
        )
    }

    override fun listenToObservers() {
        listenToCurrentLocation()
        listenToCategories()
        listenToRestaurants()
    }

    private fun listenToCategories() {
        offersViewModel.categoryResultLiveData.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Success -> {
                    cateAdapter = OffersCategoryAdapter(this)
                    offersViewModel.setDataOfCategories(binding, cateAdapter, resource)
                }

                is Resource.Error -> {
                    hideMainLoading()
                    Toasty.error(requireContext(), resource.message ?: "", Toast.LENGTH_LONG).show()
                }

                is Resource.Loading -> showMainLoading()
                is Resource.UnAuthenticated -> hideMainLoading()
                else -> {}

            }
        }
    }

    private fun listenToRestaurants()   {
        // This is the correct and safe way to collect a StateFlow from a Fragment's UI
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {

                // This will now automatically re-collect when you navigate back to the screen
                offersViewModel.restaurantResultStateFlow.collect { resource ->
                    Log.v(
                        "Fragment_Debug",
                        "Collector received a new resource: ${resource::class.java.simpleName}"
                    )


                    when (resource) {
                        is Resource.Loading -> showMainLoading()

                        is Resource.Error -> {
                            hideMainLoading()
                            Toasty.error(
                                requireContext(),
                                resource.message ?: "",
                                Toast.LENGTH_LONG
                            ).show()
                        }

                        is Resource.Success -> {
                            hideMainLoading()
                            resource.data?.restaurantListResult?.let { list ->
                                Log.d(
                                    TAG,
                                    "listenToRestaurants: Data is Herre ${list.restaurants?.pickupRestaurants?.size}"
                                )
                                Log.d(
                                    TAG,
                                    "listenToRestaurants: Data is Herre ${list.restaurants?.deliveryRestaurants?.size}"
                                )
                                restaurantListFull = list
                                restaurantsSelected = list
                            }
//                            Log.v(
//                                "Fragment_Debug",
//                                "SUCCESS: Received a list of size: ${list}. Calling setContent."
//                            )

                            // Check if the fragment's view is still active
                            if (view != null) {
                                binding.composeRestaurantList.setContent {

                                    val compounds = mainViewModel.compoundList.collectAsState()

                                    var showCompound by remember {
                                        mutableStateOf(false)
                                    }
                                    var clickFromCompound by remember {
                                        mutableStateOf(false)
                                    }

                                    var compoundSelected by remember {
                                        mutableStateOf(mainViewModel.compoundId)
                                    }

                                    var scope = rememberCoroutineScope()

                                    var selectedTab by remember { mutableStateOf(0) }
                                    var categorySelected by remember { mutableStateOf("") }

                                    var filterList by remember {
                                        mutableStateOf<List<RestaurantsResponseItem>>(
                                            restaurantListFull.restaurants?.pickupRestaurants
                                                ?: emptyList()
                                        )
                                    }


                                    fun getData(): List<RestaurantsResponseItem> {
                                        Log.d(
                                            TAG,
                                            "getDataListList: ${restaurantListFull.restaurants?.pickupRestaurants?.size}"
                                        )
                                        Log.d(
                                            TAG,
                                            "getDataListList: ${restaurantListFull.restaurants?.deliveryRestaurants?.size}"
                                        )
                                        var list = arrayListOf<RestaurantsResponseItem>()
                                        if (selectedTab == 0) {
                                            Log.d(TAG, "getData: selected tab 0")
                                            restaurantListFull.restaurants?.pickupRestaurants?.forEach {
                                                Log.d(TAG, "getData: ${it.categories?.contains(categorySelected)} , $categorySelected")
                                                if (it.categories?.contains(categorySelected) == true || categorySelected == "eqvWcHcHaD" || categorySelected == Constants.ALL || categorySelected == "") {
                                                    list.add(it)
                                                }
                                            }
                                            Log.d(TAG, "size List ${list.size} , $selectedTab")

                                        } else if (selectedTab == 1) {
                                            Log.d(TAG, "getData: selected tab 1")
                                            restaurantListFull.restaurants?.deliveryRestaurants?.forEach {
                                                if (it.categories?.contains(categorySelected) == true || categorySelected == "eqvWcHcHaD"  || categorySelected == Constants.ALL || categorySelected == "") {
                                                    list.add(it)
                                                }
                                            }
                                            Log.d(TAG, "size List ${list.size} , $selectedTab")
                                        }
                                        Log.d(TAG, "getDataListSize: ${list.size} , $selectedTab")

//                                        val initialList = if (selectedTab == 0) {
//                                            restaurantListFull.restaurants?.pickupRestaurants
//                                        } else {
//                                            restaurantListFull.restaurants?.deliveryRestaurants
//                                        }

                                        // Filter by category
//                                        list?.forEach {
//                                            if (it.categories?.contains(categorySelected) == true || categorySelected == Constants.ALL || categorySelected == "") {
//                                                list.add(it)
//                                            }
//                                        }

                                        // Sort the list according to priority:
                                        // 1. Open restaurants (not busy, not closed, not coming soon)
                                        // 2. Busy restaurants
                                        // 3. Closed restaurants
                                        // 4. Coming soon restaurants
                                        return list
                                            ?.sortedWith(compareBy<RestaurantsResponseItem> { restaurant ->
                                                when {
                                                    // Open restaurants first (lowest priority number)
                                                    restaurant.isBusy == false && restaurant.isClose != "Closed" && restaurant.isCommingSoon == false -> 1
                                                    // Busy restaurants second
                                                    restaurant.isBusy == true -> 2
                                                    // Closed restaurants third
                                                    restaurant.isClose == "Closed" -> 3
                                                    // Coming soon restaurants last
                                                    restaurant.isCommingSoon == true -> 4
                                                    // Fallback for any other cases
                                                    else -> 5
                                                }
                                            }) ?: emptyList()

//                                        return list
                                    }

                                    LaunchedEffect(Unit,restaurantListFull) {
                                        filterList = getData()
                                    }

                                    

                                    LaunchedEffect(selectedTab) {
                                        filterList = getData()
                                    }
                                    LaunchedEffect(categorySelected) {
                                        filterList = getData()
                                    }



                                    Box {

                                        Column(modifier = Modifier) {

                                            Row(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                            ) {
                                                SelectionButton(
                                                    modifier = Modifier.padding(horizontal = 12.dp),
                                                    text = getString(R.string.pickup),
                                                    icon = R.drawable.ic_dish,
                                                    isSelected = selectedTab == 0,
                                                    onSelect = { selectedTab = 0 }
                                                )

//                                                Spacer(modifier = Modifier.width(8.dp))

                                                SelectionButton(
                                                    modifier = Modifier.padding(horizontal = 12.dp),
                                                    text = getString(R.string.delivery),
                                                    isSelected = selectedTab == 1,
                                                    onSelect = {
                                                        if (mainViewModel.compoundId.isEmpty()) {
                                                            clickFromCompound = false
                                                            showCompound = true
                                                        } else {
                                                            selectedTab = 1
                                                        }
                                                    },
                                                    icon = R.drawable.ic_delivery,

                                                    )

                                                Spacer(modifier = Modifier.weight(1f))

                                                SelectionButton(
                                                    modifier = Modifier,
                                                    text = if(mainViewModel.compoundId.isEmpty()) getString(R.string.compound) else mainViewModel.compoundName,
                                                    isSelected = mainViewModel.compoundId.isNotEmpty(),
                                                    onSelect = {
                                                        clickFromCompound = true
                                                        showCompound = true
                                                    },
                                                    icon = R.drawable.ic_building,

                                                    )
                                            }

                                            AppSpacer(height = large)

                                            fun selectFilterList() {

                                            }

                                            LaunchedEffect(offersViewModel.categorySelectState) {
                                                offersViewModel.categorySelectState.collect {
                                                    categorySelected = it
                                                    Log.d(TAG, "listenToRestaurants: $categorySelected")
                                                }
                                            }
//
                                            if (filterList.isNotEmpty()) {
                                                OffersList(
                                                    restaurants = filterList,
                                                    onRestaurantClick = { restaurant ->
                                                        Log.d(TAG, "listenToRestaurants: geher")
                                                        if(restaurant.isCommingSoon == false) {
                                                            findNavController().navigate(
                                                                OffersScreenFragmentDirections
                                                                    .goToRestaurantFragment(
                                                                        restaurant.objectId!!,
                                                                        restaurant.redirectionTable!!,
                                                                        if (restaurant.isKashier == true) PAYMENT_INTEGRATION_TYPE.KASHIER else PAYMENT_INTEGRATION_TYPE.PAYMOB,
                                                                        RestaurantInfo(
                                                                            restaurantName = restaurant.name.toString(),
                                                                            restaurantLogo = restaurant.logo.toString(),
                                                                            restaurantCover = restaurant.logo.toString(),
                                                                            reviewsCount = restaurant.reviewsCount,
                                                                            distance = restaurant.distance,
                                                                            rating = restaurant.rating
                                                                        )
                                                                    )
                                                            )
                                                        }
                                                    }
                                                )
                                            } else {

                                                Column(
                                                    verticalArrangement = Arrangement.Center,
                                                    horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally,
                                                    modifier = Modifier.fillMaxWidth()
                                                ) {
                                                    Image(
                                                        modifier = Modifier
                                                            .width(250.dp)
                                                            .height(220.dp),
                                                        painter = painterResource(R.drawable.img_foods),
                                                        contentDescription = "No Restaurants Found",
                                                    )

                                                    Spacer(modifier = Modifier.height(16.dp))

                                                    AppTextNormal(
                                                        modifier = Modifier.fillMaxWidth(),
                                                        textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                                                        text = getString(R.string.this_category_has_no_restaurants_yet),
                                                        fontSize = 18,
                                                        color = AppColor.BlackColor
                                                    )
                                                }

                                            }


                                        }

                                        if (showCompound) {
                                            CompoundBottomSheet(
                                                modifier = Modifier.fillMaxWidth(),
                                                list = compounds.value,
                                                selected = compoundSelected,
                                                expand = clickFromCompound,
                                                click = { compoundId, compoundName ->
                                                    mainViewModel.compoundId = compoundId
                                                    mainViewModel.compoundName = compoundName
                                                    compoundSelected = compoundId
                                                    if(!clickFromCompound) selectedTab = 1
                                                    showCompound = false
                                                    scope.launch {
                                                        getRestaurantResults()
                                                    }
                                                },
                                                onDismiss = {
                                                    showCompound = false
                                                })
                                        }
                                    }
                                }
                            }
                        }

                        is Resource.UnAuthenticated -> hideMainLoading()
                        else -> {}

                    }
                }
            }
        }
    }

    private fun listenToCurrentLocation() {
        lifecycleScope.launch {
            mainViewModel.userLocationAddressMutableLiveData.collect { loc ->
                if (mainViewModel.compoundId.isEmpty()) {
                    binding.tvLocation.text = loc?.address?.let { "You're at $it" }
                        ?: getString(R.string.select_location)
                    //osama
                    offersViewModel.getRestaurantsResult(
                        "",
                        loc?.lat,
                        loc?.lng,
//                    30.0145125310423.toString(),
//                    30.975965400415998.toString(),
                        mainViewModel.compoundId,
                    )
                }
            }
        }
    }

    override fun onCategoryClicked(category: Category) {
        Log.d(TAG, "onCategoryClicked: ${category.categoryName}")
        offersViewModel.setSelectedCategoryName(category.categoryName ?: Constants.CATEGORY_ID_ALL)

//        offersViewModel.getRestaurantsResult(
//            id,
//            mainViewModel.userLocationAddressMutableLiveData.value?.lat,
//            mainViewModel.userLocationAddressMutableLiveData.value?.lng,
//            mainViewModel.compoundId
//        )
    }

    override fun onPause() {
        super.onPause()
        hideMainLoading()
    }
}
