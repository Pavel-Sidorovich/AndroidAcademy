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

    private var animateProgress = 0f

    private val viewModel by lazy { ViewModelProvider(requireActivity()).get(MoviesViewModel::class.java) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        animateProgress = savedInstanceState?.getFloat(PROGRESS) ?: animateProgress

//        ViewModelProvider(requireActivity()).get(MoviesViewModel::class.java)
        viewModel.getMovies()

        binding.animation.apply {
            setMinProgress(animateProgress)
            playAnimation()
            addAnimatorListener(object : AnimatorListenerAdapter() {

                override fun onAnimationEnd(p0: Animator?) {
                    mainActivity.changeFragment(true)
                }
            })
            addAnimatorUpdateListener { animator ->
                val progress = animator.animatedValue as Float
                animateProgress = progress
            }
        }
    }

    override fun onStart() {
        super.onStart()
        mainActivity.window.statusBarColor = Color.TRANSPARENT
    }

    override fun onStop() {
        super.onStop()
        binding.animation.apply {
            pauseAnimation()
            removeAllAnimatorListeners()
            removeAllUpdateListeners()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putFloat(PROGRESS, animateProgress)
        super.onSaveInstanceState(outState)
    }

    companion object {
        @JvmStatic
        fun newInstance(): SplashScreenFragment = SplashScreenFragment()

        private const val PROGRESS = "lottie_progress"
    }
}
