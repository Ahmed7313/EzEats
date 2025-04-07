package com.easyEats.ezeats.ui.main.restaurant

import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.easyEats.ezeats.R
import com.easyEats.ezeats.base.BaseBottomSheetFragment
import com.easyEats.ezeats.base.BaseFragment
import com.easyEats.ezeats.databinding.FragmentRestaurantBinding
import com.easyEats.ezeats.domain.main.model.response.restaurant_menu.Menu
import com.easyEats.ezeats.ui.main.restaurant.adapter.MenuTabAdapter
import com.easyEats.ezeats.ui.main.restaurant.adapter.SpecialOfferAdapter
import com.easyEats.ezeats.ui.main.restaurant.adapter.VerticalItem
import com.easyEats.ezeats.ui.main.restaurant.adapter.VerticalMenuAdapter
import com.easyEats.ezeats.ui.main.restaurant.adapter.flattenMenu
import com.easyEats.ezeats.utils.Resource
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

/**
 * A fragment that displays restaurant information and special offers. It allows the user to share the restaurant
 * information or dismiss the fragment. It also handles navigating to the redeem screen when a special offer is clicked.
 */
@AndroidEntryPoint
class RestaurantFragment : BaseFragment<FragmentRestaurantBinding>(),
    SpecialOfferAdapter.OnSpecialOfferClicked {

    // ViewModel to manage the restaurant data and special offers.
    private val viewModel: RestaurantViewModel by activityViewModels()
    private val args by navArgs<RestaurantFragmentArgs>()
    private lateinit var menuTabAdapter: MenuTabAdapter
    private lateinit var verticalMenuAdapter: VerticalMenuAdapter
    private lateinit var  layoutManager :LinearLayoutManager
    private lateinit var snapHelper: LinearSnapHelper



    // Adapter to display the special offers.
    private lateinit var specialOfferAdapter: SpecialOfferAdapter

    /**
     * Sets up the views and their corresponding actions for the fragment.
     */
    override fun setUpViews() {
        // Initialize the special offer adapter and set the click listener for the adapter.
        specialOfferAdapter = SpecialOfferAdapter(this)

        // Set the dismiss button to open the restaurant in a browser.
        binding.btnDismiss.setOnClickListener {
            openBrowser()
        }


        // Set the share button to share the restaurant link.
        binding.tvShare.setOnClickListener {
            // Get the restaurant ID and prepare the share text.
            val restaurantId = viewModel.restaurantId
            val shareText =
                "Check out this restaurant on Ezeats: https://www.ezeats.app/restaurant/$restaurantId"

            // Create the share intent with the share text.
            val shareIntent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, shareText)
                type = "text/plain"
            }

            // Start the activity with a chooser to share the restaurant link.
            val chooser = Intent.createChooser(shareIntent, "Share via")
            if (shareIntent.resolveActivity(requireContext().packageManager) != null) {
                startActivity(chooser)
            } else {
                // If no apps can handle the intent, show a message to the user.
                Toast.makeText(requireContext(), "No apps available to share", Toast.LENGTH_SHORT)
                    .show()
            }
        }

        // Request the special offers data from the ViewModel.
        viewModel.getSpecialOffers(args.restaurantId)
        viewModel.getRestaurantMenu(args.restaurantId)
    }

    /**
     * Observes the special offers data from the ViewModel and updates the UI accordingly.
     */
    override fun listenToObservers() {
        // Observe the special offers LiveData from the ViewModel.
        viewModel.specialOffers.observe(viewLifecycleOwner) { resources ->
            when (resources) {
                is Resource.Error -> {
                    // Handle the error case (e.g., show a message to the user).
                }

                is Resource.Loading -> {
                    // Show a loading indicator while data is being fetched.
//                    binding.loader.visibility = VISIBLE
                }

                is Resource.Success -> {
                    // Hide the loader and update the UI with the fetched data.
//                    binding.loader.visibility = GONE
                    binding.restaurantInfo = resources.data?.result?.data
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
            }
        }

        viewModel.restaurantMenu.observe(viewLifecycleOwner) { resources ->
            when (resources) {
                is Resource.Error -> {

                }

                is Resource.Loading -> {
                    // Show a loading indicator while data is being fetched.
//                    binding.loader.visibility = VISIBLE
                }

                is Resource.Success -> {
                    // Hide the loader and update the UI with the fetched data.
                    menuTabAdapter = MenuTabAdapter(
                        resources.data?.result?.data?.menu,
                        resources.data?.result?.data?.menu?.get(0)?.id, ::updateSelection
                    )

                    binding.rvMenuHeader.adapter = menuTabAdapter
                    val subMenu = flattenMenu(resources.data?.result?.data?.menu)
                    verticalMenuAdapter = VerticalMenuAdapter(subMenu)
                    binding.rvSubMenu.adapter = verticalMenuAdapter
                    binding.rvSubMenu.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                    layoutManager=binding.rvSubMenu.layoutManager as LinearLayoutManager
                    snapHelper = LinearSnapHelper()

                    // Attach it to your RecyclerView
                    snapHelper.attachToRecyclerView(binding.rvMenuHeader)
                    binding.rvSubMenu.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                        override fun onScrolled(rv: RecyclerView, dx: Int, dy: Int) {
                            val firstVisiblePosition =
                                (rv.layoutManager as? LinearLayoutManager)?.findFirstCompletelyVisibleItemPosition()
                                    ?: return
                            val item = verticalMenuAdapter.items[firstVisiblePosition]
                            if (item is VerticalItem.Header) {
                                menuTabAdapter.updateSelection(item.id)

                            }

                        }
                    })

                }

                is Resource.UnAuthenticated -> {
                    // Handle the unauthenticated case (e.g., redirect to login).
                }
            }
        }


    }

    private fun updateSelection(menuId:String) {
        val index = verticalMenuAdapter.items.indexOfFirst {
            it is VerticalItem.Header && it.id == menuId
        }

        if (index != -1) {
            // Scroll to position with offset to make sure header aligns properly
            layoutManager.scrollToPositionWithOffset(index, 0)

        }
    }


    /**
     * Inflates the fragment's view binding.
     */
    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentRestaurantBinding.inflate(layoutInflater)

    /**
     * Called when a special offer is clicked. Navigates to the redeem screen.
     *
     * @param offerId The ID of the selected offer.
     */
    override fun onSpecialOfferClickListener(offerId: String) {
        // Navigate to the redeem screen with the selected offer ID.
        findNavController().navigate(RestaurantFragmentDirections.goToRedeemScreen(offerId))
    }

    /**
     * Opens the restaurant URL in a browser.
     */
    private fun openBrowser() {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse("https://ezeats.app/${viewModel.restaurantId}-1")

        // Start the activity to open the restaurant in the browser.
        requireActivity().startActivity(intent)
    }
}

