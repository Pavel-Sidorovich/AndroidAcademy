package com.pavesid.androidacademy.ui

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import com.pavesid.androidacademy.R
import com.pavesid.androidacademy.databinding.ActivityMainBinding
import com.pavesid.androidacademy.ui.details.MoviesDetailsFragment
import com.pavesid.androidacademy.ui.movies.MoviesFragment

class MainActivity : AppCompatActivity(), MoviesFragment.Listener {

    private var _binding: ActivityMainBinding? = null
    private val binding: ActivityMainBinding
        get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        hideUi()

        val rootFragment = MoviesFragment().apply { setListener(this@MainActivity) }

        savedInstanceState ?: supportFragmentManager.beginTransaction().apply {
            add(R.id.container, rootFragment, null)
            commit()
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

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun hideUi() {
        WindowCompat.setDecorFitsSystemWindows(window, false)
    }

    override fun changeFragmentById(id: Int) {
        supportFragmentManager.beginTransaction().apply {
            add(R.id.container, MoviesDetailsFragment.newInstance(id), null)
            addToBackStack(null)
            commit()
        }
    }

    fun changeFragment(id: Int) {
        supportFragmentManager.beginTransaction().apply {
            add(R.id.container, MoviesDetailsFragment.newInstance(id), null)
            addToBackStack(null)
            commit()
        }
    }
}
