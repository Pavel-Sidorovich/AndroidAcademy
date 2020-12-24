package com.pavesid.androidacademy.utils.extensions

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.view.View
import android.view.ViewAnimationUtils
import android.widget.ImageView
import com.pavesid.androidacademy.ui.custom.CubicBezierInterpolator

/**
 * Animation for change theme.
 */
fun ImageView.startCircularReveal(startX: Int, startY: Int, startRadius: Float) {
    ViewAnimationUtils.createCircularReveal(this, startX, startY, startRadius, 0f).apply {
        duration = 1000
        interpolator = CubicBezierInterpolator.EASE_BOTH
        val animationListener = object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator?) {
                this@startCircularReveal.setImageDrawable(null)
                this@startCircularReveal.visibility = View.GONE
            }
        }
        addListener(animationListener)
        start()
    }
}
