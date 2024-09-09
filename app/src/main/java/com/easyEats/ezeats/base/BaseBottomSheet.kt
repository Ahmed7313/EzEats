package com.easyEats.ezeats.base

import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import android.app.Activity
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
import com.easyEats.ezeats.ui.main.main.HomeActivity


/**
 * An abstract base class for Bottom Sheet Dialog Fragments that provides common functionality
 * for displaying bottom sheets with full screen height and using data binding.
 *
 * This class should be extended by specific Bottom Sheet Dialog Fragments that need to include
 * a full-height bottom sheet and use data binding.
 *
 * @param VB Type parameter that extends [ViewDataBinding]. Represents the data binding class
 *           associated with the fragment's layout.
 */
abstract class BaseBottomSheetFragment<VB : ViewDataBinding> : BottomSheetDialogFragment() {

    /**
     * A lateinit property for the data binding instance. This property is initialized in the
     * [onCreateView] method and holds the reference to the fragment's binding.
     */
    protected lateinit var binding: VB

    /**
     * Inflates the view using the provided [LayoutInflater] and initializes the [binding] property
     * with the result of the [inflateBinding] method. The root view of the binding is then returned.
     *
     * @param inflater The [LayoutInflater] used to inflate the layout.
     * @param container The container view group that the fragment’s UI should be attached to.
     * @param savedInstanceState A [Bundle] containing the saved state of the fragment, if any.
     *
     * @return The root view of the binding.
     */
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

    /**
     * Creates and configures the [BottomSheetDialog]. Sets the dialog to be full screen and prevents
     * it from being canceled by clicking outside or by the back button.
     *
     * @param savedInstanceState A [Bundle] containing the saved state of the fragment, if any.
     *
     * @return The configured [BottomSheetDialog].
     */
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

    /**
     * Configures the [BottomSheetDialog] to occupy the full height of the screen. Adjusts the height
     * of the bottom sheet and sets its behavior to be expanded and non-draggable.
     *
     * @param bottomSheetDialog The [BottomSheetDialog] instance to be configured.
     */
    private fun setupFullHeight(bottomSheetDialog: BottomSheetDialog) {
        val bottomSheet =
            bottomSheetDialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet) as FrameLayout?
        val behavior: BottomSheetBehavior<*> = BottomSheetBehavior.from(bottomSheet!!)
        val layoutParams = bottomSheet.layoutParams
        val windowHeight = getWindowHeight()
        if (layoutParams != null) {
            layoutParams.height = windowHeight-200
        }
        bottomSheet.layoutParams = layoutParams
        behavior.state = BottomSheetBehavior.STATE_EXPANDED
        behavior.isDraggable = true
    }

    /**
     * Calculates and returns the height of the window (screen height) in pixels.
     *
     * @return The height of the window in pixels.
     */
    private fun getWindowHeight(): Int {
        val displayMetrics = DisplayMetrics()
        requireActivity().windowManager.defaultDisplay.getMetrics(displayMetrics)
        return displayMetrics.heightPixels
    }

    /**
     * An abstract method that should be implemented by subclasses to return the appropriate data
     * binding instance for the fragment.
     *
     * @param inflater The [LayoutInflater] used to inflate the layout.
     * @param container The container view group that the fragment’s UI should be attached to.
     *
     * @return The data binding instance for the fragment’s layout.
     */
    abstract fun inflateBinding(inflater: LayoutInflater, container: ViewGroup?): VB
}