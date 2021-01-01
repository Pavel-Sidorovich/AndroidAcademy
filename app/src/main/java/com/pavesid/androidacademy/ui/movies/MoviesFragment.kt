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
import com.pavesid.androidacademy.databinding.RecyclerLayoutBinding
import com.pavesid.androidacademy.ui.MainActivity
import com.pavesid.androidacademy.ui.MoviesViewModel
import com.pavesid.androidacademy.utils.Status
import com.pavesid.androidacademy.utils.extensions.getColorFromAttr
import com.pavesid.androidacademy.utils.viewBinding
import timber.log.Timber

class MoviesFragment : Fragment(R.layout.fragment_movies) {

    private val binding: FragmentMoviesBinding by viewBinding(FragmentMoviesBinding::bind)

    private val recyclerLayoutBinding: RecyclerLayoutBinding by viewBinding { RecyclerLayoutBinding.bind(binding.root) }

    private val moviesItemDecoration: MoviesItemDecoration by lazy {
        MoviesItemDecoration(
            spaceSize = requireContext().resources.getDimensionPixelSize(R.dimen.spacing_normal_16)
        )
    }

    private val mainActivity by lazy { activity as MainActivity }

    private lateinit var moviesAdapter: MoviesAdapter

    private val callback by lazy { MoviesItemTouchHelper(moviesAdapter) }

    override fun onStart() {
        super.onStart()
        mainActivity.window.statusBarColor = requireContext().theme.getColorFromAttr(
            R.attr.backgroundColor
        )
        viewModel.getMovies()
    }

    private val viewModel by lazy { ViewModelProvider(requireActivity()).get(MoviesViewModel::class.java) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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

        moviesAdapter = MoviesAdapter(
            { movie ->
                viewModel.updateMovies(movie = movie)
            },
            { movie, cX, cY ->
                mainActivity.changeFragment(false, movie, cX, cY)
            }
        )

        recyclerLayoutBinding.moviesRecycler.apply {
            setHasFixedSize(true)
            layoutManager =
                GridLayoutManager(requireContext(), resources.getInteger(R.integer.grid_count))
            adapter = moviesAdapter

            addItemDecoration(moviesItemDecoration)
        }

        val touchHelper = ItemTouchHelper(callback)
        touchHelper.attachToRecyclerView(recyclerLayoutBinding.moviesRecycler)
    }

    private fun subscribeToObservers() {
        viewModel.movies.observe(
            viewLifecycleOwner,
            { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        recyclerLayoutBinding.progress.visibility = View.GONE
                        Timber.d(recyclerLayoutBinding.progress.toString())
                        resource.data?.let { movies ->
                            moviesAdapter.movies = movies
                        }
                    }
                    Status.ERROR -> {
                        recyclerLayoutBinding.progress.visibility = View.GONE
                        Toast.makeText(requireContext(), resource.message, Toast.LENGTH_SHORT)
                            .show()
                    }
                    Status.LOADING -> recyclerLayoutBinding.progress.visibility = View.VISIBLE
                }
            }
        )
    }

    companion object {
        fun newInstance() = MoviesFragment()
    }
}
