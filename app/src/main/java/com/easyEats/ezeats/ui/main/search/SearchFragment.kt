package com.easyEats.ezeats.ui.main.search

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.easyEats.ezeats.R
import com.easyEats.ezeats.base.BaseBottomSheetFragment
import com.easyEats.ezeats.databinding.FragmentSearchBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class SearchFragment : BaseBottomSheetFragment<FragmentSearchBinding>() {
    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentSearchBinding.inflate(layoutInflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.mcvSearch.setOnClickListener {
            Toast.makeText(requireContext(), "kenawy ", Toast.LENGTH_SHORT).show()
            binding.SearchContainer.visibility = View.VISIBLE
            binding.reTop.visibility = View.GONE
        }
    }


}