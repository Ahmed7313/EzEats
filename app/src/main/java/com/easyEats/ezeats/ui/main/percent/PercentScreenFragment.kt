package com.easyEats.ezeats.ui.main.percent

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.easyEats.ezeats.R
import com.easyEats.ezeats.base.BaseFragment
import com.easyEats.ezeats.databinding.FragmentPercentScreenBinding
import com.easyEats.ezeats.ui.main.main.MainViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class PercentScreenFragment : BaseFragment<FragmentPercentScreenBinding>() {

    private val mainViewModel: MainViewModel by activityViewModels()
    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentPercentScreenBinding.inflate(layoutInflater)

    override fun setUpViews() {
        binding.tvLocation.setOnClickListener {
            findNavController().navigate(R.id.location_bottom_sheet)
        }

        listenToObservers()
    }

    private fun listenToObservers() {
        mainViewModel.userLocationAddressMutableLiveData.observe(viewLifecycleOwner) {
            it?.let {
                binding.tvLocation.text = it.address.toString()
            }
        }
    }

}