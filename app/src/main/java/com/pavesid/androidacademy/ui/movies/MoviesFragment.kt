package com.pavesid.androidacademy.ui.movies

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import com.pavesid.androidacademy.R
import com.pavesid.androidacademy.data.loadMovies
import com.pavesid.androidacademy.databinding.FragmentMoviesBinding
import com.pavesid.androidacademy.ui.MainActivity
import com.pavesid.androidacademy.utils.Utils.getColorFromAttr
import com.pavesid.androidacademy.utils.viewBinding
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import timber.log.Timber

class MoviesFragment : Fragment(R.layout.fragment_movies) {

    private val binding: FragmentMoviesBinding by viewBinding(FragmentMoviesBinding::bind)

    private val mainActivity by lazy { activity as MainActivity }

    private val moviesAdapter by lazy {
        MoviesAdapter {
            mainActivity.changeFragment(it)
        }
    }

    private val callback by lazy { MoviesItemTouchHelper(moviesAdapter) }

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        binding.progress.visibility = View.GONE
        coroutineScope = createScope().apply {
            launch {
                Timber.d(throwable)
            }
        }
    }

    private var coroutineScope = createScope()

    override fun onStart() {
        super.onStart()
        mainActivity.window.statusBarColor = getColorFromAttr(
            R.attr.backgroundColor,
            requireContext().theme
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ViewCompat.setOnApplyWindowInsetsListener(view) { _, insets ->
            val systemWindowInsets = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            val params = binding.toolbar.layoutParams as ViewGroup.MarginLayoutParams
            params.setMargins(0, systemWindowInsets.top, 0, 0)
            return@setOnApplyWindowInsetsListener insets
        }

        initActionBar()
        initView()
    }

    private fun createScope() = CoroutineScope(
        Dispatchers.Main + Job()
    )

    private fun initActionBar() {
        mainActivity.apply {
            setSupportActionBar(binding.toolbar)
            title = ""
        }
    }

    private fun initView() {
        coroutineScope.launch(exceptionHandler) {
            val movies = loadMovies(requireContext())
            moviesAdapter.movies = movies
            binding.progress.visibility = View.GONE
        }

        binding.moviesRecycler.apply {
            setHasFixedSize(true)
            layoutManager =
                GridLayoutManager(requireContext(), resources.getInteger(R.integer.grid_count))
            adapter = moviesAdapter

            addItemDecoration(
                MoviesItemDecoration(
                    spaceSize = resources.getDimensionPixelSize(R.dimen.spacing_normal_16)
                )
            )
        }

        val touchHelper = ItemTouchHelper(callback)
        touchHelper.attachToRecyclerView(binding.moviesRecycler)
    }
}
