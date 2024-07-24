package com.easyEats.ezeats.base


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.easyEats.ezeats.ui.auth.main.MainActivity

abstract class BaseFragment<VB : ViewDataBinding> : Fragment() {

    private var _binding: VB? = null
    protected val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = inflateBinding(inflater, container)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding?.lifecycleOwner = viewLifecycleOwner
        setUpViews()
    }


    abstract fun inflateBinding(inflater: LayoutInflater, container: ViewGroup?): VB
    abstract fun setUpViews()

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    protected fun showLoading() {
        (activity as? MainActivity)?.binding?.loader?.visibility = View.VISIBLE
    }

    protected fun hideLoading() {
        (activity as? MainActivity)?.binding?.loader?.visibility = View.GONE
    }

}

