package com.easyEats.ezeats.ui.main.cart

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
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
import com.easyEats.ezeats.base.BaseResponse
import com.easyEats.ezeats.databinding.FragmentCartBinding
import com.easyEats.ezeats.databinding.FragmentEditProfileBinding
import com.easyEats.ezeats.databinding.FragmentRestaurantBinding
import com.easyEats.ezeats.domain.main.model.response.restaurant_menu.Menu
import com.easyEats.ezeats.domain.main.model.response.restaurant_menu.RestaurantMenuResponse
import com.easyEats.ezeats.domain.main.model.response.restaurant_menu.SubMenu
import com.easyEats.ezeats.ui.compose.cart.CartScreenUI
import com.easyEats.ezeats.ui.main.restaurant.adapter.MenuTabAdapter
import com.easyEats.ezeats.ui.main.restaurant.adapter.SpecialOfferAdapter
import com.easyEats.ezeats.ui.main.restaurant.adapter.VerticalItem
import com.easyEats.ezeats.ui.main.restaurant.adapter.VerticalMenuAdapter
import com.easyEats.ezeats.ui.main.restaurant.adapter.flattenMenu
import com.easyEats.ezeats.utils.Resource
import com.easyEats.ezeats.utils.extensions.smoothScrollToCenter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

/**
 * A fragment that displays restaurant information and special offers. It allows the user to share the restaurant
 * information or dismiss the fragment. It also handles navigating to the redeem screen when a special offer is clicked.
 */
@AndroidEntryPoint
class CartFragment : BaseFragment<FragmentCartBinding>() {


    override val shouldRetainView: Boolean = true
    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentCartBinding.inflate(layoutInflater)


    // ViewModel to manage the restaurant data and special offers.
    private val viewModel: CartViewModel by activityViewModels()
    private lateinit var menuTabAdapter: MenuTabAdapter
    private lateinit var verticalMenuAdapter: VerticalMenuAdapter
    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var specialOfferAdapter: SpecialOfferAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Request the special offers data from the ViewModel.

    }
    /**
     * Sets up the views and their corresponding actions for the fragment.
     */
    override fun setUpViews() {
        binding.composeView.setContent {
            CartScreenUI(modifier = Modifier.fillMaxSize())
        }
    }

}
