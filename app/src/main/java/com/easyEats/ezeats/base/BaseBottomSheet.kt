package com.easyEats.ezeats.base

import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import android.app.Dialog
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.View
import android.widget.FrameLayout
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding

abstract class BaseBottomSheetFragment<VB : ViewDataBinding> : BottomSheetDialogFragment() {

    protected lateinit var binding: VB

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = inflateBinding(inflater, container)
        setUpViews()
        listenToObservers()
        return binding.root
    }

    abstract fun setUpViews()
    abstract fun listenToObservers()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.setOnShowListener { dialogInterface ->
            val bottomSheetDialog = dialogInterface as BottomSheetDialog
            setupFullHeight(bottomSheetDialog)
        }
        dialog.setCancelable(false)
        dialog.setCanceledOnTouchOutside(false)
        return dialog
    }

    private fun setupFullHeight(bottomSheetDialog: BottomSheetDialog) {
        val bottomSheet =
            bottomSheetDialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet) as FrameLayout?
        val behavior: BottomSheetBehavior<*> = BottomSheetBehavior.from(bottomSheet!!)
        val layoutParams = bottomSheet.layoutParams
        val windowHeight = getWindowHeight()

        if (layoutParams != null) {
            layoutParams.height = windowHeight - 20
        }
        bottomSheet.layoutParams = layoutParams

        behavior.state = BottomSheetBehavior.STATE_EXPANDED
        behavior.isDraggable = true
//        behavior.skipCollapsed = true // Skip collapsed state, go directly to hidden

//         Handle bottom sheet dragging and closing in one step
        behavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_DRAGGING -> {
                        // Optional: Prevent the quick flickering movement
//                        behavior.state = BottomSheetBehavior.STATE_HIDDEN
                    }
                    BottomSheetBehavior.STATE_SETTLING -> {
                        // Optionally handle settling state
                    }
                    BottomSheetBehavior.STATE_EXPANDED -> {
                        // Fully expanded
                    }
                    BottomSheetBehavior.STATE_COLLAPSED -> {
                        // Fully collapsed
//                        behavior.state = BottomSheetBehavior.STATE_HIDDEN
                    }
                    BottomSheetBehavior.STATE_HALF_EXPANDED -> {
                        // Fully collapsed
//                        behavior.state = BottomSheetBehavior.STATE_HIDDEN
                    }
                    BottomSheetBehavior.STATE_HIDDEN -> {
                        // Dismiss the dialog if hidden
                        dismiss()
                    }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                // Optionally handle sliding
                    // Fully collapsed
//                    behavior.state = BottomSheetBehavior.STATE_HIDDEN

            }
        })
    }

    private fun getWindowHeight(): Int {
        val displayMetrics = DisplayMetrics()
        requireActivity().windowManager.defaultDisplay.getMetrics(displayMetrics)
        return displayMetrics.heightPixels
    }

    abstract fun inflateBinding(inflater: LayoutInflater, container: ViewGroup?): VB
}
