package com.easyEats.ezeats.base


import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.easyEats.ezeats.base.loading.LoadingDialog
import com.easyEats.ezeats.ui.auth.main.AuthActivity
import com.easyEats.ezeats.ui.main.main.HomeActivity

/**
 * BaseFragment is an abstract class that provides a base implementation for fragments using ViewDataBinding.
 *
 * @param VB The type of ViewDataBinding.
 */
abstract class BaseFragment<VB : ViewDataBinding> : Fragment() {

    // Backing property for the ViewDataBinding instance
    var _binding: VB? = null

    // Non-nullable property to access the ViewDataBinding instance
    protected val binding get() = _binding ?: throw IllegalStateException("View binding is only valid between onCreateView and onDestroyView.")


    private var loadingDialog: LoadingDialog? = null

    protected open val shouldRetainView: Boolean = false  // <-- Add this flag



    /**
     * Inflates the binding and sets the root view for this fragment.
     *
     * @param inflater The LayoutInflater object that can be used to inflate any views in the fragment.
     * @param container If non-null, this is the parent view that the fragment's UI should be attached to.
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous saved state.
     * @return The root view for the fragment's UI.
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (_binding == null || !shouldRetainView) {
            _binding = inflateBinding(inflater, container)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpViews()
        listenToObservers()
        _binding?.apply {
            lifecycleOwner = viewLifecycleOwner
        }
    }


    /**
     * Abstract method to inflate the ViewDataBinding.
     *
     * @param inflater The LayoutInflater object that can be used to inflate any views in the fragment.
     * @param container If non-null, this is the parent view that the fragment's UI should be attached to.
     * @return The inflated ViewDataBinding instance.
     */
    abstract fun inflateBinding(inflater: LayoutInflater, container: ViewGroup?): VB

    /**
     * Abstract method to set up the views. This should be implemented by subclasses.
     */
    abstract fun setUpViews()

    /**
     * Called when the view is destroyed. Nullifies the binding to avoid memory leaks.
     */
    override fun onDestroyView() {
        super.onDestroyView()
        if (!shouldRetainView) {
            _binding = null
        }
    }

    /**
     * Shows a loading indicator if the activity is of type MainActivity.
     */
    protected fun showLoading() {
        if (loadingDialog == null) {
            loadingDialog = LoadingDialog()
            loadingDialog?.isCancelable = false
            loadingDialog?.show(parentFragmentManager, "LoadingDialog")
        }
    }

    protected fun showMainLoading() {
        if (loadingDialog == null) {
            loadingDialog = LoadingDialog()
            loadingDialog?.isCancelable = false
            loadingDialog?.show(parentFragmentManager, "LoadingDialog")
        }
    }

    protected fun hideMainLoading() {
        loadingDialog?.dismiss()
        loadingDialog = null
    }


    /**
     * Hides the loading indicator if the activity is of type MainActivity.
     */
    protected fun hideLoading() {
        loadingDialog?.dismiss()
        loadingDialog = null
    }

    protected fun authenticateUser() {
        requireActivity().startActivity(Intent(requireActivity(), AuthActivity::class.java))
        requireActivity().finish()
    }

    open fun listenToObservers(){}
}

