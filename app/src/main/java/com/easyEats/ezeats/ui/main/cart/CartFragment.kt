package com.easyEats.ezeats.ui.main.cart

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.easyEats.ezeats.base.BaseFragment
import com.easyEats.ezeats.databinding.FragmentCartBinding
import com.easyEats.ezeats.ui.compose.cart.CartScreenUI
import com.easyEats.ezeats.ui.main.restaurant.CartViewModel
import com.easyEats.ezeats.ui.main.restaurant.RestaurantViewModel
import com.easyEats.ezeats.ui.main.restaurant.adapter.MenuTabAdapter
import com.easyEats.ezeats.ui.main.restaurant.adapter.SpecialOfferAdapter
import com.easyEats.ezeats.ui.main.restaurant.adapter.VerticalMenuAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

/**
 * A fragment that displays restaurant information and special offers. It allows the user to share the restaurant
 * information or dismiss the fragment. It also handles navigating to the redeem screen when a special offer is clicked.
 */
@AndroidEntryPoint
class CartFragment : BaseFragment<FragmentCartBinding>() {


//    override val shouldRetainView: Boolean = true
    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentCartBinding.inflate(layoutInflater)

    val TAG = "CartFragment"

    // ViewModel to manage the restaurant data and special offers.
    val viewModel: CartViewModel by activityViewModels()
    private val viewModelRestaurant: RestaurantViewModel by activityViewModels()

    private lateinit var menuTabAdapter: MenuTabAdapter
    private lateinit var verticalMenuAdapter: VerticalMenuAdapter
    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var specialOfferAdapter: SpecialOfferAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Request the special offers data from the ViewModel.

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(TAG, "onViewCreated: ")
    }




    private fun loadCart(){
        Log.d(TAG, "loadCart: here")
        viewLifecycleOwner.lifecycleScope.launch {
            Log.d(TAG, "loadCart: launch")
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                Log.d(TAG, "loadCart: started")
                if (view != null) {
                    Log.d(TAG, "loadCart: not null")

                }
            }
        }
    }

    override fun listenToObservers() {
        super.listenToObservers()
        loadCart()
    }

    /**
     * Sets up the views and their corresponding actions for the fragment.
     */
    override fun setUpViews() {
        Log.d(TAG, "setUpViews: Set")
    }

    fun callCheckTableAvailability() {
        lifecycleScope.launch {
            showLoading()
            viewModel.checkTableAvailability().collect { tableResponse ->
                hideLoading()
                if (tableResponse != null) {
                    binding.composeView.setContent {
                        Log.d(TAG, "onViewCreated: setContent")
                        CartScreenUI(
                            modifier = Modifier.fillMaxSize(),
                            viewModel,
                            findNavController()
                        )
                    }
                }else{
                    viewModel.clearShop()
                    findNavController().navigateUp()
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume: ")
        callCheckTableAvailability()

    }

}
