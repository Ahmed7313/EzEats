package com.easyEats.ezeats.ui.main.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.easyEats.ezeats.R
import com.easyEats.ezeats.base.BaseFragment
import com.easyEats.ezeats.databinding.FragmentHomeScreenBinding
import com.easyEats.ezeats.ui.main.main.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeScreenFragment : BaseFragment<FragmentHomeScreenBinding>() {

    val mainViewModel: MainViewModel by activityViewModels()

    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    )= FragmentHomeScreenBinding.inflate(inflater,container,false)

    override fun setUpViews() {
        binding.tvLocation.setOnClickListener {
            findNavController().navigate(R.id.action_homeScreenFragment_to_locationBottomSheetDialogFragment)
        }
    }

}