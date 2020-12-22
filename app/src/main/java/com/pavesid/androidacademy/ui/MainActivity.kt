package com.pavesid.androidacademy.ui

import android.os.Bundle
import android.os.Parcelable
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.pavesid.androidacademy.R
import com.pavesid.androidacademy.ui.details.MoviesDetailsFragment
import com.pavesid.androidacademy.ui.movies.MoviesFragment
import com.pavesid.androidacademy.ui.splash.SplashScreenFragment
import com.pavesid.androidacademy.utils.extensions.ExitWithAnimation
import com.pavesid.androidacademy.utils.extensions.exitCircularReveal
import com.pavesid.androidacademy.utils.extensions.exitCircularRevealToLeft
import com.pavesid.androidacademy.utils.extensions.open
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_AndroidAcademy)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val rootFragment = SplashScreenFragment()

        savedInstanceState ?: supportFragmentManager.open {
            add(R.id.container, rootFragment, null)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
            }
        }
        return super.onOptionsItemSelected(item)
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

    fun changeFragment(toMovies: Boolean = false, parcelable: Parcelable? = null, cX: Int = 0, cY: Int = 0) {
        if (toMovies) {
            supportFragmentManager.open {
                replace(R.id.container, MoviesFragment.newInstance(), null)
            }
        } else {
            val detailFragment = MoviesDetailsFragment.newInstance(parcelable!!, cX, cY)
            supportFragmentManager.open {
                add(R.id.container, detailFragment, null)
                addToBackStack(null)
            }
        }
    }
}
