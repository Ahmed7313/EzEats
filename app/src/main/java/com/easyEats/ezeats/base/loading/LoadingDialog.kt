package com.easyEats.ezeats.base.loading

import android.animation.Animator
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.airbnb.lottie.LottieDrawable
import com.easyEats.ezeats.R
import com.easyEats.ezeats.databinding.LoadingDialogBinding

class LoadingDialog : DialogFragment() {

    private var _binding: LoadingDialogBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = LoadingDialogBinding.inflate(inflater, container, false)
        binding.lottieAnimationView.setAnimation(R.raw.loading)

        binding.lottieAnimationView.playAnimation()
//        binding.lottieAnimationView.apply {
//            setAnimation(R.raw.loading)
//            repeatCount = LottieDrawable.INFINITE
//            playAnimation()
//        }
//
//        // Optional: To restart animation
//        binding.lottieAnimationView.addAnimatorListener(object : Animator.AnimatorListener {
//            override fun onAnimationStart(animation: Animator) {}
//            override fun onAnimationEnd(animation: Animator) {
//                binding.lottieAnimationView.playAnimation() // Restart when ended
//            }
//            override fun onAnimationCancel(animation: Animator) {}
//            override fun onAnimationRepeat(animation: Animator) {}
//        })
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        // Set the dialog to have a transparent background
        dialog?.window?.setBackgroundDrawableResource(android.R.color.transparent)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
