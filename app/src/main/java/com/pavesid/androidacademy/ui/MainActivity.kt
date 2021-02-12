package com.pavesid.androidacademy.ui

import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import com.pavesid.androidacademy.App.Companion.THEME
import com.pavesid.androidacademy.R
import com.pavesid.androidacademy.data.movies.Movie
import com.pavesid.androidacademy.databinding.ActivityMainBinding
import com.pavesid.androidacademy.ui.details.DetailsFragment
import com.pavesid.androidacademy.ui.dialogs.DateDialogFragment
import com.pavesid.androidacademy.ui.movies.MoviesFragment
import com.pavesid.androidacademy.ui.movies.MoviesViewModel
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
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private var isDarkTheme = false

    @Inject
    lateinit var prefs: SharedPreferences

    private lateinit var binding: ActivityMainBinding

    private val viewModel by lazy { ViewModelProvider(this).get(MoviesViewModel::class.java) }

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_AndroidAcademy)

        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        isDarkTheme = prefs.getBoolean(THEME, false)

        val rootFragment = if (prefs.getBoolean(ANIMATION, false)) {
            MoviesFragment.newInstance()
        } else {
            prefs.edit {
                putBoolean(ANIMATION, true)
            }
            SplashScreenFragment.newInstance()
        }

        savedInstanceState ?: run {
            supportFragmentManager.open {
                add(R.id.container, rootFragment, TAG)
            }
            intent?.let(::handleIntent)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        menu?.findItem(R.id.theme)?.setSafeOnMenuItemClickListener {
        }
        val searchItem = menu?.findItem(R.id.action_search)
        val searchView = searchItem?.actionView as SearchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                viewModel.searchMovies(query.orEmpty())
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                viewModel.searchMovies(newText.orEmpty())
                return true
            }
        })
        return true
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        intent?.let {
            handleIntent(it)
        }
    }

    private fun handleIntent(intent: Intent) {
        when (intent.action) {
            Intent.ACTION_VIEW -> {
                val id = intent.data?.lastPathSegment?.toLongOrNull() ?: Long.MIN_VALUE
                val movie = Movie(id = id)

                if (id != Long.MIN_VALUE) {
                    changeToDetailsFragment(
                        Json.encodeToString(movie), 0, 0
                    )
                }
            }
        }
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

    fun changeToMoviesFragment() = supportFragmentManager.open {
        replace(R.id.container, MoviesFragment.newInstance(), TAG)
    }

    fun openDialog(title: String, overview: String, duration: Int) {
        val dialog = DateDialogFragment.newInstance(title, overview, duration)
        dialog.show(supportFragmentManager, null)
    }

    fun changeToDetailsFragment(
        movieString: String = "",
        cX: Int = 0,
        cY: Int = 0
    ) {
        val detailFragment = DetailsFragment.newInstance(movieString, cX, cY)
        supportFragmentManager.open {
            add(R.id.container, detailFragment, null)
            addToBackStack(null)
        }
    }

    private fun changeThemeWithAnimation(view: View) {
        delegate.localNightMode =
            if (isDarkTheme) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO
        val windowBitmap = Bitmap.createBitmap(
            binding.container.measuredWidth,
            binding.container.measuredHeight,
            Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(windowBitmap!!)
        binding.container.draw(canvas)

        val location = IntArray(2)
        view.getLocationOnScreen(location)

        binding.screen.apply {
            setImageBitmap(windowBitmap)
            scaleType = ImageView.ScaleType.MATRIX
            isVisible = true
        }

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
                binding.container.measuredWidth.toDouble(),
                binding.container.measuredHeight.toDouble(),
            ).toFloat()
        )
    }

    private companion object {
        private const val TAG = "Movies"
        private const val ANIMATION = "animationPlayed"
    }
}
