package com.easyEats.ezeats.ui.main.active_order_details

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.easyEats.ezeats.base.BaseFragment
import com.easyEats.ezeats.base.BaseResponse
import com.easyEats.ezeats.databinding.FragmentActiveOrderDetailsBinding
import com.easyEats.ezeats.domain.main.model.request.complaint_request.ComplaintRequest
import com.easyEats.ezeats.domain.main.model.response.active_order.ActiveOrderResponse
import com.easyEats.ezeats.domain.main.model.response.order_history.SubOrder
import com.easyEats.ezeats.ui.main.complaint.ComplaintFragment
import com.easyEats.ezeats.ui.main.order_history.adapter.SubOrderHistoryAdapter
import com.easyEats.ezeats.utils.Resource
import dagger.hilt.android.AndroidEntryPoint
import es.dmoral.toasty.Toasty

/**
 * Fragment to display active order details, including support for complaints and viewing sub-orders.
 * This fragment uses `ActiveOrderDetailsViewModel` to manage the data and supports interaction with
 * the complaint dialog for user feedback.
 */
@AndroidEntryPoint
class ActiveOrderDetailsFragment : BaseFragment<FragmentActiveOrderDetailsBinding>(),
    ComplaintFragment.DialogCallback {

    // ViewModel instance for active order details
    private val activeOrderViewModel by viewModels<ActiveOrderDetailsViewModel>()

    // Adapter to manage sub-order items
    private lateinit var subOrderAdapter: SubOrderHistoryAdapter

    // Variables to store restaurant and order IDs
    private var restaurantId: String? = null
    private var orderId: String? = null

    /**
     * Inflates the fragment's layout and initializes data binding.
     */
    override fun inflateBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentActiveOrderDetailsBinding.inflate(inflater, container, false)

    /**
     * Sets up views and initializes the observer for the ViewModel.
     */
    override fun setUpViews() {
        setupUIInteractions()
        observeViewModel()
    }

    /**
     * Sets up UI interactions, such as click listeners for various buttons and views.
     */
    private fun setupUIInteractions() {
        binding.ivCollapse.setOnClickListener { toggleSubOrderListVisibility() }
        binding.ivBack.setOnClickListener { findNavController().popBackStack() }
        binding.btnCallSupport.setOnClickListener {
            val complaintFragment = ComplaintFragment()
            complaintFragment.setDialogCallback(this)
            complaintFragment.show(parentFragmentManager, "CustomDialog")
        }
    }

    /**
     * Toggles the visibility of the sub-order list and rotates the collapse icon.
     */
    private fun toggleSubOrderListVisibility() {
        binding.rvSubOrder.isVisible = !binding.rvSubOrder.isVisible
        binding.ivCollapse.rotation = if (binding.rvSubOrder.isVisible) 180f else 0f
    }

    /**
     * Opens the complaint dialog fragment and sets the current fragment as the dialog's callback.
     */
    private fun openComplaintDialog() {

    }

    /**
     * Observes the ViewModel LiveData to react to changes in the active order details or support email responses.
     */
    private fun observeViewModel() {
        activeOrderViewModel.trackActiveOrderResultLiveData.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Success -> handleOrderSuccess(resource)
                is Resource.Error -> showErrorToast(resource.message)
                else -> Unit
            }
        }
    }

    /**
     * Handles the successful retrieval of active order details.
     *
     * @param resource The success resource containing the order data.
     */
    private fun handleOrderSuccess(resource: Resource.Success<BaseResponse<ActiveOrderResponse>>) {
        resource.data?.result?.data?.let { orderData ->
            restaurantId = orderData.restaurantId
            orderId = orderData.orderId
            binding.activeOrder = orderData
            setupSubOrderAdapter(orderData.subOrders)
            setupRestaurantCallButton(orderData.phoneNumber)
        }
    }

    /**
     * Configures the adapter for displaying sub-order items in a RecyclerView.
     *
     * @param subOrders The list of sub-orders to be displayed.
     */
    private fun setupSubOrderAdapter(subOrders: List<SubOrder?>?) {
        subOrderAdapter = SubOrderHistoryAdapter().apply {
            submitList(subOrders)
        }
        binding.rvSubOrder.adapter = subOrderAdapter
    }

    /**
     * Sets up the call button with the restaurant's phone number.
     *
     * @param phoneNumber The phone number to call.
     */
    private fun setupRestaurantCallButton(phoneNumber: String?) {
        binding.btnCallRestaurant.setOnClickListener {
            phoneNumber?.let { callRestaurant(it) }
        }
    }

    /**
     * Initiates a call to the specified phone number.
     *
     * @param phoneNumber The phone number to dial.
     */
    private fun callRestaurant(phoneNumber: String) {
        Intent(Intent.ACTION_DIAL, Uri.parse("tel:$phoneNumber")).also {
            startActivity(it)
        }
    }

    /**
     * Displays an error message as a toast notification.
     *
     * @param message The error message to display.
     */
    private fun showErrorToast(message: String?) {
        Toasty.error(requireContext(), message.orEmpty(), Toast.LENGTH_LONG).show()
    }


    /**
     * Observes the ViewModel's complaint response and invokes the success callback accordingly.
     *
     * @param complaintSavedSuccessfully Callback to indicate if the complaint was saved successfully.
     */
    private fun observeComplaintResponse(complaintSavedSuccessfully: (Boolean) -> Unit) {
        activeOrderViewModel.sendSupportEmailResultLiveData.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Success -> complaintSavedSuccessfully(resource.data?.result?.data == true)
                is Resource.Error -> showErrorToast(resource.message)
                else -> Unit
            }
        }
    }

    /**
     * Callback function from the ComplaintFragment.DialogCallback interface. Called when the user submits a complaint.
     *
     * @param name The name of the complainant.
     * @param email The email of the complainant.
     * @param message The complaint message.
     * @param complaintSavedSuccessfully A callback to notify if the complaint was saved successfully.
     */
    override fun onSubmitComplaint(
        name: String,
        email: String,
        message: String,
        complaintSavedSuccessfully: (Boolean) -> Unit
    ) {
        val complaintRequest =
            ComplaintRequest(name, email, message, restaurantId.toString(), orderId.toString())
        activeOrderViewModel.sendSupportEmailResult(complaintRequest)
        observeComplaintResponse(complaintSavedSuccessfully)
    }
}
