package com.pavesid.androidacademy.ui

import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Bundle
import android.os.Parcelable
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.pavesid.androidacademy.R
import com.pavesid.androidacademy.databinding.ActivityMainBinding
import com.pavesid.androidacademy.ui.details.MoviesDetailsFragment
import com.pavesid.androidacademy.ui.movies.MoviesFragment
import com.pavesid.androidacademy.ui.splash.SplashScreenFragment
import com.pavesid.androidacademy.utils.extensions.ExitWithAnimation
import com.pavesid.androidacademy.utils.extensions.edit
import com.pavesid.androidacademy.utils.extensions.exitCircularReveal
import com.pavesid.androidacademy.utils.extensions.exitCircularRevealToLeft
import com.pavesid.androidacademy.utils.extensions.open
import com.pavesid.androidacademy.utils.extensions.setSafeOnMenuItemClickListener
import com.pavesid.androidacademy.utils.extensions.startCircularReveal
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlin.math.hypot

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private var isDarkTheme = false

    @Inject
    lateinit var prefs: SharedPreferences

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_AndroidAcademy)

        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        isDarkTheme = prefs.getBoolean(THEME, false)
        savedInstanceState ?: changeTheme()

        val rootFragment = if (prefs.getBoolean(ANIMATION, false)) {
            MoviesFragment.newInstance()
        } else {
            prefs.edit {
                putBoolean(ANIMATION, true)
            }
            SplashScreenFragment.newInstance()
        }

        savedInstanceState ?: supportFragmentManager.open {
            add(R.id.container, rootFragment, TAG)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        menu?.findItem(R.id.theme)?.setSafeOnMenuItemClickListener {
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            R.id.theme -> {
                isDarkTheme = !isDarkTheme
                prefs.edit {
                    putBoolean(THEME, isDarkTheme)
                }
                changeThemeWithAnimation(findViewById(R.id.theme))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onBackPressed() {
        with(supportFragmentManager.findFragmentById(R.id.container)) {
            if ((this as? ExitWithAnimation)?.isToBeExitedWithAnimation() == true) {
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

    fun changeFragment(
        toMovies: Boolean = false,
        parcelable: Parcelable? = null,
        cX: Int = 0,
        cY: Int = 0
    ) {
        if (toMovies) {
            supportFragmentManager.open {
                replace(R.id.container, MoviesFragment.newInstance(), TAG)
            }
        } else {
            val detailFragment = MoviesDetailsFragment.newInstance(parcelable!!, cX, cY)
            supportFragmentManager.open {
                add(R.id.container, detailFragment, null)
                addToBackStack(null)
            }
        }
    }

    private fun changeTheme() {
        delegate.localNightMode =
            if (isDarkTheme) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO
        recreate()
    }

    private fun changeThemeWithAnimation(view: View) {
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
        supportFragmentManager.findFragmentByTag(TAG)?.let {
            supportFragmentManager.open {
                detach(it)
                attach(it)
            }
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

    private companion object {
        private const val THEME = "switchTheme"
        private const val TAG = "Movies"
        private const val ANIMATION = "animationPlayed"
    }
}
