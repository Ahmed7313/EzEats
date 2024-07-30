package com.easyEats.ezeats.ui.main.location

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.easyEats.ezeats.R
import com.easyEats.ezeats.ui.main.main.MainViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LocationBottomSheetDialogFragment : BottomSheetDialogFragment() {

    val mainViewModel: MainViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =
            inflater.inflate(
                R.layout.fragment_location_bottom_sheet_dialog,
                container,
                false
            )

        return view
    }


}