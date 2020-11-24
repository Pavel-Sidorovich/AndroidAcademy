package com.pavesid.androidacademy.ui.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import com.pavesid.androidacademy.R
import com.pavesid.androidacademy.databinding.ActivityMainBinding
import com.pavesid.androidacademy.ui.fragments.MoviesDetailsFragment
import com.pavesid.androidacademy.ui.fragments.MoviesFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        savedInstanceState ?: supportFragmentManager.beginTransaction().apply {
            add(R.id.container, MoviesFragment(), null)
            commit()
        }

        hideUi()
    }

    private fun hideUi() {
        WindowCompat.setDecorFitsSystemWindows(window, false)
    }

    fun changeFragment(title: String) {
        supportFragmentManager.beginTransaction().apply {
            add(R.id.container, MoviesDetailsFragment.newInstance(title), null)
            addToBackStack(null)
            commit()
        }
    }
}
