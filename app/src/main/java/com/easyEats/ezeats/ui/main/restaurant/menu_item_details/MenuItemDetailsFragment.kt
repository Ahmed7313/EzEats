package com.easyEats.ezeats.ui.main.restaurant.menu_item_details

import android.view.LayoutInflater
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.easyEats.ezeats.base.BaseBottomSheetFragment
import com.easyEats.ezeats.databinding.FragmentMenuItemDetailsBinding
import com.easyEats.ezeats.ui.main.restaurant.RestaurantViewModel
import com.easyEats.ezeats.utils.Resource
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MenuItemDetailsFragment : BaseBottomSheetFragment<FragmentMenuItemDetailsBinding>() {

    private val viewModel: RestaurantViewModel by activityViewModels()
    private val args by navArgs<MenuItemDetailsFragmentArgs>()

    override fun setUpViews() {
        binding.menuItem =args.MenuItem
        binding.ivBack.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    override fun listenToObservers() {

    }



    override fun inflateBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentMenuItemDetailsBinding.inflate(layoutInflater)


}