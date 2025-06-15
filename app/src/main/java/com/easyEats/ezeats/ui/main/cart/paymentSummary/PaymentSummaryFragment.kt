package com.easyEats.ezeats.ui.main.cart.paymentSummary

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.easyEats.ezeats.base.BaseFragment
import com.easyEats.ezeats.databinding.FragmentPaymentSummaryBinding
import com.easyEats.ezeats.ui.compose.cart.paymentSummaryScreen.PaymentSummaryScreenUI
import com.easyEats.ezeats.ui.main.restaurant.adapter.MenuTabAdapter
import com.easyEats.ezeats.ui.main.restaurant.adapter.SpecialOfferAdapter
import com.easyEats.ezeats.ui.main.restaurant.adapter.VerticalMenuAdapter
import dagger.hilt.android.AndroidEntryPoint

/**
 * A fragment that displays restaurant information and special offers. It allows the user to share the restaurant
 * information or dismiss the fragment. It also handles navigating to the redeem screen when a special offer is clicked.
 */
@AndroidEntryPoint
class PaymentSummaryFragment : BaseFragment<FragmentPaymentSummaryBinding>() {


    override val shouldRetainView: Boolean = true
    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentPaymentSummaryBinding.inflate(layoutInflater)


    // ViewModel to manage the restaurant data and special offers.
    private val viewModel: PaymentSummaryViewModel by activityViewModels()
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
            PaymentSummaryScreenUI(modifier = Modifier.fillMaxSize(),findNavController())
        }
    }

}
