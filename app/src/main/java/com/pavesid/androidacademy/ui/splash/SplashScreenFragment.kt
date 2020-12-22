package com.pavesid.androidacademy.ui.splash

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.pavesid.androidacademy.R
import com.pavesid.androidacademy.databinding.FragmentSplashScreenBinding
import com.pavesid.androidacademy.ui.MainActivity
import com.pavesid.androidacademy.ui.MoviesViewModel
import com.pavesid.androidacademy.utils.viewBinding

class SplashScreenFragment : Fragment(R.layout.fragment_splash_screen) {

    private val mainActivity by lazy { activity as MainActivity }

    private val binding by viewBinding(FragmentSplashScreenBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ViewModelProvider(requireActivity()).get(MoviesViewModel::class.java)

        savedInstanceState ?: binding.animation.apply {
            playAnimation()
            addAnimatorListener(object : AnimatorListenerAdapter() {

                override fun onAnimationEnd(p0: Animator?) {
                    mainActivity.changeFragment(true)
                }
            })
        }
    }

    override fun onStart() {
        super.onStart()
        mainActivity.window.statusBarColor = Color.TRANSPARENT
    }

    override fun onPause() {
        super.onPause()
        mainActivity.changeFragment(true)
    }

    companion object {
        @JvmStatic
        fun newInstance(): SplashScreenFragment = SplashScreenFragment()
    }
}
