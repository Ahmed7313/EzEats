package com.easyEats.ezeats.ui.main.cart.paymentSummary

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.easyEats.ezeats.base.BaseFragment
import com.easyEats.ezeats.databinding.FragmentPaymentSummaryBinding
import com.easyEats.ezeats.ui.compose.cart.paymentSummaryScreen.PaymentSummaryScreenUI
import com.easyEats.ezeats.ui.main.restaurant.CartViewModel
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
    val viewModel: CartViewModel by activityViewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Request the special offers data from the ViewModel.

    }
    private val args by navArgs<PaymentSummaryFragmentArgs>()

    /**
     * Sets up the views and their corresponding actions for the fragment.
     */
    override fun setUpViews() {
        binding.composeView.setContent {
            PaymentSummaryScreenUI(modifier = Modifier.fillMaxSize(),findNavController(),viewModel,args.cartResponse)
        }
    }

}
