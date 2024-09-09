package com.easyEats.ezeats.ui.main.points


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.easyEats.ezeats.R
import com.easyEats.ezeats.base.BaseFragment
import com.easyEats.ezeats.databinding.FragmentPointScreenBinding
import com.easyEats.ezeats.ui.main.points.adapter.PointsAdapter
import com.easyEats.ezeats.utils.Resource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class PointScreenFragment : BaseFragment<FragmentPointScreenBinding>() {
    private val pointsViewModel: PointsViewModel by viewModels()
    private val pointsAdapter by lazy {
        PointsAdapter()
    }

    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentPointScreenBinding.inflate(layoutInflater)

    override fun setUpViews() {
        listenToObserver()
    }

    private fun listenToObserver() {
        lifecycleScope.launch {
            pointsViewModel.getUserToken().collect{token->
                if (token.isNullOrEmpty()){
                    findNavController().navigate(R.id.goToUserGuestScreen)
                }
            }
        }

        pointsViewModel.pointsResultLiveData.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Error -> {
                    hideMainLoading()
                }

                is Resource.Loading -> {
                    showMainLoading()
                }

                is Resource.Success -> {
                    hideMainLoading()
                    binding.pointsResponse = resource.data?.result?.data
                    binding.rvPoints.adapter = pointsAdapter
                    pointsAdapter.submitList(resource.data?.result?.data?.loyaltyPoints)
                }

                is Resource.UnAuthenticated -> {
                    hideMainLoading()
                }
            }

        }
    }

}