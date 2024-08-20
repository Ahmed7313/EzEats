package com.easyEats.ezeats.ui.main.points


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.easyEats.ezeats.base.BaseFragment
import com.easyEats.ezeats.databinding.FragmentPointScreenBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class PointScreenFragment : BaseFragment<FragmentPointScreenBinding>() {
    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentPointScreenBinding.inflate(layoutInflater)

    override fun setUpViews() {
    }

}