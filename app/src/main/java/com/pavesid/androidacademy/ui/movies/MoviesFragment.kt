package com.pavesid.androidacademy.ui.movies

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import com.pavesid.androidacademy.R
import com.pavesid.androidacademy.databinding.FragmentMoviesBinding
import com.pavesid.androidacademy.ui.MainActivity
import com.pavesid.androidacademy.ui.MainViewModel
import com.pavesid.androidacademy.utils.Status
import com.pavesid.androidacademy.utils.getColorFromAttr
import com.pavesid.androidacademy.utils.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MoviesFragment @Inject constructor(
    var viewModel: MainViewModel?
) : Fragment(R.layout.fragment_movies) {

    constructor() : this(null)

    private val binding: FragmentMoviesBinding by viewBinding(FragmentMoviesBinding::bind)

    @Inject
    internal lateinit var moviesItemDecoration: MoviesItemDecoration

    private val mainActivity by lazy { activity as MainActivity }

    private val moviesAdapter by lazy {
        MoviesAdapter {
            mainActivity.changeFragment(it)
        }
    }

    private val callback by lazy { MoviesItemTouchHelper(moviesAdapter) }

    override fun onStart() {
        super.onStart()
        mainActivity.window.statusBarColor = requireContext().theme.getColorFromAttr(
            R.attr.backgroundColor
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = viewModel ?: ViewModelProvider(requireActivity()).get(MainViewModel::class.java)

        addMarginTopToToolbar(view)
        initActionBar()
        initView()
        subscribeToObservers()
    }

    private fun addMarginTopToToolbar(view: View) {
        ViewCompat.setOnApplyWindowInsetsListener(view) { _, insets ->
            val systemWindowInsets = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            val params = binding.toolbar.layoutParams as ViewGroup.MarginLayoutParams
            params.setMargins(0, systemWindowInsets.top, 0, 0)
            return@setOnApplyWindowInsetsListener insets
        }
    }

    private fun initActionBar() {
        mainActivity.apply {
            setSupportActionBar(binding.toolbar)
            title = ""
        }
    }

    private fun initView() {

        binding.moviesRecycler.apply {
            setHasFixedSize(true)
            layoutManager =
                GridLayoutManager(requireContext(), resources.getInteger(R.integer.grid_count))
            adapter = moviesAdapter

            addItemDecoration(moviesItemDecoration)
        }

        val touchHelper = ItemTouchHelper(callback)
        touchHelper.attachToRecyclerView(binding.moviesRecycler)
    }

    private fun subscribeToObservers() {
        viewModel?.movies?.observe(
            viewLifecycleOwner,
            { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        binding.progress.visibility = View.GONE
                        resource.data?.let { movies ->
                            moviesAdapter.movies = movies
                        }
                    }
                    Status.ERROR -> {
                        binding.progress.visibility = View.GONE
                        Toast.makeText(requireContext(), resource.message, Toast.LENGTH_SHORT)
                            .show()
                    }
                    Status.LOADING -> binding.progress.visibility = View.VISIBLE
                }
            }
        )
    }
}
