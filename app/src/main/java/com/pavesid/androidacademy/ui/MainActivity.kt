package com.pavesid.androidacademy.ui

import android.animation.Animator
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Bundle
import android.os.Parcelable
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewAnimationUtils
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.pavesid.androidacademy.R
import com.pavesid.androidacademy.databinding.ActivityMainBinding
import com.pavesid.androidacademy.ui.details.MoviesDetailsFragment
import com.pavesid.androidacademy.ui.movies.MoviesFragment
import com.pavesid.androidacademy.ui.screenshot.CubicBezierInterpolator
import com.pavesid.androidacademy.utils.extensions.ExitWithAnimation
import com.pavesid.androidacademy.utils.extensions.exitCircularReveal
import com.pavesid.androidacademy.utils.extensions.exitCircularRevealToLeft
import com.pavesid.androidacademy.utils.extensions.open
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlin.math.hypot

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private var isDarkTheme = false

    private var detailsIsOpen = false

    private var themeIsChanging = false

    @Inject
    lateinit var prefs: SharedPreferences

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        isDarkTheme = prefs.getBoolean(THEME, false)
        savedInstanceState ?: changeTheme()

        val rootFragment = MoviesFragment()

        savedInstanceState ?: supportFragmentManager.open {
            add(R.id.container, rootFragment, null)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            R.id.theme -> {
                if (!detailsIsOpen) {
                    isDarkTheme = !isDarkTheme
                    prefs.edit().putBoolean(THEME, isDarkTheme).apply()
                    changeThemeWithAnimation(findViewById(R.id.theme))
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onBackPressed() {
        with(supportFragmentManager.findFragmentById(R.id.container)) {
            if ((this as? ExitWithAnimation)?.isToBeExitedWithAnimation() == true) {
                detailsIsOpen = false
                if (this.posX == null || this.posY == null) {
                    this.view?.exitCircularRevealToLeft {
                        super.onBackPressed()
                    }
                } else {
                    this.view?.exitCircularReveal(this.posX!!, this.posY!!) {
                        super.onBackPressed()
                    } ?: super.onBackPressed()
                }
            } else {
                super.onBackPressed()
            }
        }
    }

    private fun changeTheme() {
        delegate.localNightMode =
            if (isDarkTheme) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO
        recreate()
    }

    private fun changeThemeWithAnimation(view: View) {
        if (!themeIsChanging) {
            themeIsChanging = true
            delegate.localNightMode =
                if (isDarkTheme) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO
            val windowBitmap = Bitmap.createBitmap(
                window.decorView.width,
                window.decorView.height,
                Bitmap.Config.ARGB_8888
            )
            val canvas = Canvas(windowBitmap!!)
            window.decorView.draw(canvas)

            binding.screen.setImageBitmap(windowBitmap)

            val location = IntArray(2)
            view.getLocationOnScreen(location)

            binding.screen.scaleType = ImageView.ScaleType.MATRIX
            binding.screen.visibility = View.VISIBLE
            supportFragmentManager.open {
                replace(R.id.container, MoviesFragment(), null)
            }
            binding.screen.startCircularReveal(
                location[0] + view.width / 2,
                location[1] + view.width / 2,
                hypot(
                    window.decorView.width.toDouble(),
                    window.decorView.height.toDouble()
                ).toFloat()
            )
        }
    }

    fun changeFragment(parcelable: Parcelable, cX: Int, cY: Int) {
        detailsIsOpen = true
        val detailFragment = MoviesDetailsFragment.newInstance(parcelable, cX, cY)
        supportFragmentManager.open {
            add(R.id.container, detailFragment, null)
            addToBackStack(null)
        }
    }

    fun ImageView.startCircularReveal(startX: Int, startY: Int, startRadius: Float) {
        ViewAnimationUtils.createCircularReveal(this, startX, startY, startRadius, 0f).apply {
            duration = 1000
            interpolator = CubicBezierInterpolator.EASE_BOTH
            val animationListener = object : Animator.AnimatorListener {
                override fun onAnimationEnd(animation: Animator?) {
                    this@startCircularReveal.setImageDrawable(null)
                    this@startCircularReveal.visibility = View.GONE
                    themeIsChanging = false
                }

                override fun onAnimationCancel(animation: Animator?) {}
                override fun onAnimationRepeat(animation: Animator?) {}
                override fun onAnimationStart(animation: Animator?) {}
            }
            addListener(animationListener)
            start()
        }
    }

    private companion object {
        const val THEME = "switchTheme"
    }
}
