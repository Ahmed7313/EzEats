package com.easyEats.ezeats.ui.main.restaurant

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.Toast
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.Visibility
import com.easyEats.ezeats.base.BaseFragment
import com.easyEats.ezeats.base.BaseResponse
import com.easyEats.ezeats.databinding.FragmentRestaurantBinding
import com.easyEats.ezeats.domain.main.model.response.restaurant_menu.Menu
import com.easyEats.ezeats.domain.main.model.response.restaurant_menu.RestaurantMenuResponse
import com.easyEats.ezeats.domain.main.model.response.restaurant_menu.SubMenu
import com.easyEats.ezeats.ui.compose.resturant.MenuItemBottomSheet
import com.easyEats.ezeats.ui.main.restaurant.adapter.MenuTabAdapter
import com.easyEats.ezeats.ui.main.restaurant.adapter.SpecialOfferAdapter
import com.easyEats.ezeats.ui.main.restaurant.adapter.VerticalItem
import com.easyEats.ezeats.ui.main.restaurant.adapter.VerticalMenuAdapter
import com.easyEats.ezeats.ui.main.restaurant.adapter.flattenMenu
import com.easyEats.ezeats.utils.Resource
import com.easyEats.ezeats.utils.extensions.smoothScrollToCenter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

/**
 * A fragment that displays restaurant information and special offers. It allows the user to share the restaurant
 * information or dismiss the fragment. It also handles navigating to the redeem screen when a special offer is clicked.
 */
@AndroidEntryPoint
class RestaurantFragment : BaseFragment<FragmentRestaurantBinding>(),
    SpecialOfferAdapter.OnSpecialOfferClicked {

    override val shouldRetainView: Boolean = true

    val TAG = "RestaurantFragment"

    // ViewModel to manage the restaurant data and special offers.
    private val viewModel: RestaurantViewModel by activityViewModels()
    private val cartViewModel: CartViewModel by viewModels()
    private val args by navArgs<RestaurantFragmentArgs>()
    private lateinit var menuTabAdapter: MenuTabAdapter
    private lateinit var verticalMenuAdapter: VerticalMenuAdapter
    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var specialOfferAdapter: SpecialOfferAdapter


    //VzblCVYTa0 ammar shop
    //S7CQuGrgXX ramadan Test
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Request the special offers data from the ViewModel.
//        viewModel.getSpecialOffers(args.restaurantId)
//        viewModel.getRestaurantMenu(args.restaurantId)
//        viewModel.getRestaurantMenu("S7CQuGrgXX")
        val restaurantId = args.restaurantId
        val tableId = args.tableId
        val payment_integration_type = args.PAYMENTINTEGRATIONTYPE
        lifecycleScope.launch {
            showLoading()
            // First check table availability
            if(tableId.isNotEmpty()) {
                viewModel.checkTableAvailability(restaurantId, tableId).collect { tableResponse ->
                    if (tableResponse != null) {
                        Log.d(
                            TAG,
                            "onCreate: ${tableResponse.result?.tableOrder?.tableOrderId}"
                        )//currentOrderId
                        Log.d(
                            TAG,
                            "onCreate: ${tableResponse.result?.currentOrder?.userId}"
                        )//userId
                        Log.d(
                            TAG,
                            "onCreate: ${tableResponse.result?.currentOrder?.currentOrderId}"
                        )
                        // Table is available, now load menu
                        cartViewModel.initShop(
                            tableOrderId = tableResponse.result?.tableOrder?.tableOrderId.toString(),
                            currentOrderId = tableResponse.result?.currentOrder?.currentOrderId.toString(),
                            userId = tableResponse.result?.currentOrder?.userId.toString(),
                            shopId = restaurantId,
                            tableId = tableId
                        )
                        viewModel.getRestaurantMenu(restaurantId)
                    } else {
                        // Handle table not available
                        viewModel.getRestaurantMenu(restaurantId)
                    }
                }
            }else{
                viewModel.getRestaurantMenu(restaurantId)
            }
        }
//        viewModel.isBottomSheetVisible.observe(this) { isVisible ->
//            if (isVisible) {
//                viewModel.selectedSubMenu.observe(this) { subMenu ->
//                    showBottomSheet(subMenu)
//                }
//            } else {
//                // Dismiss the bottom sheet if it is not visible
//                dismissBottomSheet()
//            }
//        }
    }

    var isAppearBefore = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }


    /**
     * Sets up the views and their corresponding actions for the fragment.
     */
    override fun setUpViews() {
        Log.d(TAG, "setUpViewsHere: GONE")
        binding.restaurantContainer.visibility = View.GONE
        binding.restaurantInfo = args.resturantInfo
        // Initialize the special offer adapter and set the click listener for the adapter.
        specialOfferAdapter = SpecialOfferAdapter(this)

        // Set the dismiss button to open the restaurant in a browser.
        binding.btnDismiss.setOnClickListener {
            openBrowser()
        }

        // Set the share button to share the restaurant link.
        binding.tvShare.setOnClickListener {
            shareRestaurantLink()
        }
        binding.ivBack.setOnClickListener {
            findNavController().navigateUp()
        }




        Log.d(TAG, "setUpViews: HERE")
        binding.composeView.apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)

            binding.composeView.setContent {

                val isVisible by viewModel.isBottomSheetVisible.collectAsState()
                val currentSubMenu by viewModel.selectedSubMenu.collectAsState()
                LaunchedEffect(isVisible) {
                    visibility = if (isVisible) View.VISIBLE else View.GONE
                }


                if (isVisible && currentSubMenu != null && cartViewModel.currentOrderId.isNotEmpty() && cartViewModel.tableOrderId.isNotEmpty()) {
                    binding.composeView.visibility = View.VISIBLE
                    MenuItemBottomSheet(
                        modifier = Modifier.fillMaxWidth(),
                        isVisible = true,
                        onDismiss = {
                            viewModel.dismissBottomSheet()
                        },
                        subMenu = currentSubMenu!!,
                        calculateTotal = { count, subTotal, note, selected ->
                            cartViewModel.addToCart(currentSubMenu!!,selected,count,note)
                            viewModel.dismissBottomSheet()
                        }
                    )
                } else {
                    binding.composeView.visibility = View.GONE
                }
//                MenuItemBottomSheet(
//                    modifier = androidx.compose.ui.Modifier.fillMaxWidth(),
//                    isVisible = isVisible,
//                    onDismiss = {
//                        Log.d(TAG, "setUpViews: GONE")
//                        // Hide the compose view when dismissing
//                        binding.composeView.visibility = View.GONE
//                        viewModel.dismissBottomSheet()
//                    },
//                    subMenu = currentSubMenu, onClick = { count, note ->
//                        // Handle the click event for the menu item
//                        Log.d(TAG, "setUpViews: GONE")
//                        binding.composeView.visibility = View.GONE
//                        viewModel.dismissBottomSheet()
//                    }
//                )
            }

        }

    }

    private fun setupBusyOrClose() {
        val fullText = "This Restaurant is currently busy or closed, you can continue browsing the menu but you can’t make an order"


// Change "busy" to red
        val spannable = SpannableString(fullText)

        listOf("busy", "closed").forEach { word ->
            val start = fullText.indexOf(word)
            if (start >= 0) {
                spannable.setSpan(
                    ForegroundColorSpan(Color.RED),
                    start,
                    start + word.length,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
            }
        }
        binding.busyOrCloseText.text = spannable
        if(cartViewModel.currentOrderId.isEmpty() || cartViewModel.tableOrderId.isEmpty()){
            binding.llCloseOrBusy.visibility = View.VISIBLE
        }else{
            binding.llCloseOrBusy.visibility = View.GONE
        }
    }


    /**
     * Observes the special offers and restaurant menu data from the ViewModel and updates the UI accordingly.
     */
    override fun listenToObservers() {
        // Observe the special offers LiveData from the ViewModel.
        viewModel.specialOffers.observe(viewLifecycleOwner) { resources ->
            when (resources) {
                is Resource.Error -> {
                    Log.d(TAG, "listenToObservers: ERROR")
                    // Handle the error case (e.g., show a message to the user).
                }

                is Resource.Loading -> {
                    Log.d(TAG, "listenToObservers: LOADING")
                    // Show a loading indicator while data is being fetched.
                    // binding.loader.visibility = VISIBLE
                }

                is Resource.Success -> {
                    // Hide the loader and update the UI with the fetched data.
                    // binding.loader.visibility = GONE
                    Log.d(TAG, "listenToObservers: found")
                    binding.restaurantInfo = resources.data?.result?.data
                    Log.d(TAG, "listenToObservers: found ${binding.restaurantInfo?.restaurantName}")
                    Log.d(
                        TAG,
                        "listenToObservers: found ${resources.data?.result?.data?.offers?.size}"
                    )

                    viewModel.setRestaurantData(
                        binding,
                        specialOfferAdapter,
                        resources.data?.result?.data?.offers?.size ?: 0,
                        resources.data?.result?.data?.offers
                    )
                }

                is Resource.UnAuthenticated -> {
                    // Handle the unauthenticated case (e.g., redirect to login).
                }
                else ->{}
            }
        }

        // Observe the restaurant menu LiveData from the ViewModel.
        viewModel.restaurantMenu.observe(viewLifecycleOwner) { resources ->
            when (resources) {
                is Resource.Error -> {
                    Log.d(TAG, "listenToObservers: Restaurant Menu ERROR")
                    hideLoading()
                }

                is Resource.Loading -> {
                    Log.d(TAG, "listenToObservers: Restaurant Menu Loading")

                    showLoading()
                }

                is Resource.Success -> {
                    Log.d(TAG, "listenToObservers: Restaurant Menu Success")
                    Log.d(TAG, "listenToObservers: ${resources.data?.result?.data?.menu?.size}")
                    // Hide the loader and update the UI with the fetched data.
                    initializeMenuAndSubMenu(resources)
                    setupBusyOrClose()
                    smoothScrolling()
                    hideLoading()
                }

                is Resource.UnAuthenticated -> {
                    // Handle the unauthenticated case (e.g., redirect to login).
                }
                is Resource.DEFAULT -> {
                    // Handle the unauthenticated case (e.g., redirect to login).
                }
            }
        }
    }

    /**
     * Initializes the menu and submenu for the restaurant.
     *
     * @param resources The success response containing the restaurant menu data.
     */
    private fun initializeMenuAndSubMenu(resources: Resource.Success<BaseResponse<RestaurantMenuResponse>>) {
        // Initialize the menu tab adapter with the restaurant menu data.
        val list = mutableListOf<Menu>()
        resources.data?.result?.data?.menu?.forEach {
            if (it.isHidden == false)
                list.add(it)
        }

        if (list.isNotEmpty()) {
            menuTabAdapter = MenuTabAdapter(
                list,
                list[0].id,
                ::updateSelection
            )
            binding.rvMenuHeader.adapter = menuTabAdapter

            // Flatten the nested menu structure into a single list for vertical display.
            val subMenu = flattenMenu(list)
            verticalMenuAdapter = VerticalMenuAdapter(subMenu, ::onMenuItemClicked)
            binding.rvSubMenu.adapter = verticalMenuAdapter
            binding.rvSubMenu.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            layoutManager = binding.rvSubMenu.layoutManager as LinearLayoutManager
        }
        Log.d(TAG, "setUpViewsHere2: visible")
        binding.restaurantContainer.visibility = View.VISIBLE
    }


    private val _isBottomSheetVisible = MutableLiveData<Boolean>()
    val isBottomSheetVisible: LiveData<Boolean> get() = _isBottomSheetVisible

    private val _selectedSubMenu = MutableLiveData<SubMenu>()
    val selectedSubMenu: LiveData<SubMenu> get() = _selectedSubMenu


    private fun onMenuItemClicked(subMenu: SubMenu) {
        viewModel.cleanupBottomSheet()
        viewModel.showBottomSheet(subMenu)
//        if (subMenu.subOptions.isNullOrEmpty()) {
////            _selectedSubMenu.value = subMenu
////            _isBottomSheetVisible.value = true
//            viewModel.cleanupBottomSheet()
//            viewModel.showBottomSheet(subMenu)
//            Log.d(TAG, "SubMenu is empty or null: ${subMenu.name}")
//        } else {
//            viewModel.cleanupBottomSheet()
//            findNavController().navigate(
//                RestaurantFragmentDirections.goToMenuItemDetailsFragment(
//                    subMenu
//                )
//            )
//        }
    }

    /**
     * Sets up smooth scrolling behavior for the vertical RecyclerView.
     * It ensures that when a vertical item is scrolled, the corresponding header tab is updated.
     */
    private fun smoothScrolling() {
        binding.rvSubMenu.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(rv: RecyclerView, dx: Int, dy: Int) {
                // Determine the first visible item in the vertical RecyclerView.
                val firstVisiblePosition =
                    (rv.layoutManager as? LinearLayoutManager)?.findFirstVisibleItemPosition()
                        ?: return

                // Check if the first visible item is a header.
                val item = verticalMenuAdapter.items[firstVisiblePosition]
                if (item is VerticalItem.Header) {
                    // If the header ID has changed, update the selected tab.
                    if (item.id != menuTabAdapter.selectedId) {
                        menuTabAdapter.updateSelection(item.id)
                        val tabIndex = menuTabAdapter.menus?.indexOfFirst { it.id == item.id } ?: -1
                        if (tabIndex != -1) {
                            binding.rvMenuHeader.smoothScrollToCenter(tabIndex)
                        }
                    }
                }
            }
        })
    }

    override fun onResume() {
        super.onResume()
        viewModel.cleanupBottomSheet()
        Log.d(TAG, "onResume: ")
    }

    /**
     * Updates the selected menu tab in the header when a different menu item is selected.
     *
     * @param menuId The ID of the selected menu item.
     */
    private fun updateSelection(menuId: String) {
        val index = verticalMenuAdapter.items.indexOfFirst {
            it is VerticalItem.Header && it.id == menuId
        }

        if (index != -1) {
            // Scroll to the selected menu item in the vertical RecyclerView.
            layoutManager.scrollToPositionWithOffset(index, 0)
        }
    }

    /**
     * Handles the share button click, allowing the user to share the restaurant link.
     */
    private fun shareRestaurantLink() {
        val restaurantId = viewModel.restaurantId
        val shareText =
            "Check out this restaurant on Ezeats: https://www.ezeats.app/restaurant/$restaurantId"

        val shareIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, shareText)
            type = "text/plain"
        }

        val chooser = Intent.createChooser(shareIntent, "Share via")
        if (shareIntent.resolveActivity(requireContext().packageManager) != null) {
            startActivity(chooser)
        } else {
            Toast.makeText(requireContext(), "No apps available to share", Toast.LENGTH_SHORT)
                .show()
        }
    }

    /**
     * Inflates the fragment's view binding.
     *
     * @param inflater The LayoutInflater used to inflate the view.
     * @param container The container to host the fragment's view.
     * @return The inflated view binding.
     */
    override fun inflateBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentRestaurantBinding.inflate(layoutInflater)

    /**
     * Called when a special offer is clicked. Navigates to the redeem screen with the selected offer ID.
     *
     * @param offerId The ID of the selected offer.
     */
    override fun onSpecialOfferClickListener(offerId: String) {
//        findNavController().navigate(RestaurantFragmentDirections.goToRedeemScreen(offerId))
    }

    /**
     * Opens the restaurant URL in a browser.
     */
    private fun openBrowser() {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse("https://ezeats.app/${viewModel.restaurantId}-1")
        requireActivity().startActivity(intent)
    }

    override fun onDestroyView() {
        super.onDestroyView()

    }
}
