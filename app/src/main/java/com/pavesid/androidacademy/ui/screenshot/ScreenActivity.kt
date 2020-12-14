package com.pavesid.androidacademy.ui.screenshot

import android.animation.Animator
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.View.VISIBLE
import android.view.ViewAnimationUtils
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.preference.PreferenceManager
import com.pavesid.androidacademy.App
import com.pavesid.androidacademy.App.Companion.POS_X
import com.pavesid.androidacademy.App.Companion.POS_Y
import com.pavesid.androidacademy.App.Companion.RADIUS
import com.pavesid.androidacademy.R
import com.pavesid.androidacademy.utils.CompressBitmap

class ScreenActivity : AppCompatActivity() {

    private val prefs by lazy { PreferenceManager.getDefaultSharedPreferences(applicationContext) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_screen)

        val image = CompressBitmap.uncompressFromString(prefs.getString(App.SCREEN, "") ?: "")
        val posX: Int = intent?.extras?.getInt(POS_X) ?: 0
        val posY: Int = intent?.extras?.getInt(POS_Y) ?: 0
        val radius: Float = intent?.extras?.getFloat(RADIUS) ?: 0f
        val screen = findViewById<ImageView>(R.id.screen)
        screen.setImageBitmap(image)
        screen.scaleType = ImageView.ScaleType.MATRIX
        screen.visibility = VISIBLE

        LocalBroadcastManager.getInstance(this).sendBroadcast(Intent(App.INIT))

        val listener = object : View.OnAttachStateChangeListener {
            override fun onViewAttachedToWindow(v: View?) {
                (v as? ImageView)?.startCircularReveal(posX, posY, radius)
            }

            override fun onViewDetachedFromWindow(v: View?) {}
        }
        screen.addOnAttachStateChangeListener(listener)
    }

    fun ImageView.startCircularReveal(startX: Int, startY: Int, startRadius: Float) {
        ViewAnimationUtils.createCircularReveal(this, startX, startY, startRadius, 0f).apply {
            duration = 1000
            interpolator = CubicBezierInterpolator.EASE_BOTH
            val animationListener = object : Animator.AnimatorListener {
                override fun onAnimationEnd(animation: Animator?) {
                    this@startCircularReveal.setImageDrawable(null)
                    this@startCircularReveal.visibility = View.GONE
                    LocalBroadcastManager.getInstance(this@ScreenActivity)
                        .sendBroadcast(Intent(App.FINISH))
                    finish()
                }

                override fun onAnimationCancel(animation: Animator?) {}
                override fun onAnimationRepeat(animation: Animator?) {}
                override fun onAnimationStart(animation: Animator?) {}
            }
            addListener(animationListener)
            start()
        }
    }
}
