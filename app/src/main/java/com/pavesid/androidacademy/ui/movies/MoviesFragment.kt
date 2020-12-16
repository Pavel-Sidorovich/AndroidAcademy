package com.pavesid.androidacademy.ui.movies

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import com.pavesid.androidacademy.R
import com.pavesid.androidacademy.databinding.FragmentMoviesBinding
import com.pavesid.androidacademy.ui.MainActivity
import com.pavesid.androidacademy.ui.MoviesViewModel
import com.pavesid.androidacademy.utils.Status
import com.pavesid.androidacademy.utils.extensions.getColorFromAttr
import com.pavesid.androidacademy.utils.viewBinding
import javax.inject.Inject

class MoviesFragment @Inject constructor(
    var viewModel: MoviesViewModel?
) : Fragment(R.layout.fragment_movies) {

    constructor() : this(null)

    private val binding: FragmentMoviesBinding by viewBinding(FragmentMoviesBinding::bind)

    private val moviesItemDecoration: MoviesItemDecoration by lazy {
        MoviesItemDecoration(
            spaceSize = requireContext().resources.getDimensionPixelSize(R.dimen.spacing_normal_16)
        )
    }

    private val mainActivity by lazy { activity as MainActivity }

    private val moviesAdapter by lazy {
        MoviesAdapter { movie, cX, cY ->
            mainActivity.changeFragment(movie, cX, cY)
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
        viewModel = viewModel ?: ViewModelProvider(requireActivity()).get(MoviesViewModel::class.java)

        initActionBar()
        initView()
        subscribeToObservers()
    }

    private fun initActionBar() {
        mainActivity.apply {
            setSupportActionBar(binding.toolbar)
            title = ""
        }

        binding.toolbar.inflateMenu(R.menu.menu)
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
