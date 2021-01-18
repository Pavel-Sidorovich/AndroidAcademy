package com.pavesid.androidacademy.ui.movies

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.AppBarLayout
import com.pavesid.androidacademy.R
import com.pavesid.androidacademy.databinding.FragmentMoviesBinding
import com.pavesid.androidacademy.databinding.RecyclerLayoutBinding
import com.pavesid.androidacademy.ui.MainActivity
import com.pavesid.androidacademy.utils.Status
import com.pavesid.androidacademy.utils.extensions.getColorFromAttr
import com.pavesid.androidacademy.utils.extensions.hideKeyboard
import com.pavesid.androidacademy.utils.viewBinding
import kotlin.math.abs

class MoviesFragment : Fragment(R.layout.fragment_movies) {

    private val binding: FragmentMoviesBinding by viewBinding(FragmentMoviesBinding::bind)

    private val recyclerLayoutBinding: RecyclerLayoutBinding by viewBinding {
        RecyclerLayoutBinding.bind(
            binding.root
        )
    }

    private val moviesItemDecoration: MoviesItemDecoration by lazy {
        MoviesItemDecoration(
            spaceSize = requireContext().resources.getDimensionPixelSize(R.dimen.spacing_normal_16)
        )
    }

    private val mainActivity by lazy { activity as MainActivity }

    private lateinit var moviesAdapter: MoviesAdapter

    private lateinit var genresAdapter: GenresAdapter

    override fun onStart() {
        super.onStart()
        mainActivity.window.statusBarColor = requireContext().theme.getColorFromAttr(
            R.attr.backgroundColor
        )
    }

    private val viewModel by lazy { ViewModelProvider(requireActivity()).get(MoviesViewModel::class.java) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.init()

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

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        if (viewModel.searchQuery.isNotEmpty()) {
            val searchItem = menu.findItem(R.id.action_search)
            val searchView = searchItem?.actionView as SearchView
            searchItem.expandActionView()
            searchView.setQuery(viewModel.searchQuery, true)
            searchView.clearFocus()
        }
    }

    private fun initView() {

        genresAdapter = GenresAdapter {
            viewModel.loadMovies(it)
        }

        moviesAdapter = MoviesAdapter(
            { },
            { movie, cX, cY ->
                mainActivity.changeToDetailsFragment(movie, cX, cY)
            },
            {
                viewModel.loadMovies()
            }
        )

        val moviesLayoutManager =
            GridLayoutManager(requireContext(), resources.getInteger(R.integer.grid_count))

        recyclerLayoutBinding.moviesRecycler.apply {
            setHasFixedSize(true)
            layoutManager = moviesLayoutManager
            adapter = moviesAdapter

            addItemDecoration(moviesItemDecoration)

            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                        recyclerView.hideKeyboard()
                    }
                }
            })
        }

        val genresLayoutManager =
            LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
        binding.tagsRecycler.apply {
            layoutManager = genresLayoutManager
            adapter = genresAdapter
        }

        binding.appBar.addOnOffsetChangedListener(
            AppBarLayout.OnOffsetChangedListener { appBarLayout, verticalOffset ->
                if (abs(verticalOffset) >= appBarLayout.totalScrollRange) {
                    binding.tagsRecycler.visibility = View.INVISIBLE
                } else {
                    binding.tagsRecycler.visibility = View.VISIBLE
                }
            }
        )
    }

    private fun subscribeToObservers() {
        viewModel.movies.observe(
            viewLifecycleOwner
        ) { resource ->
            when (resource.status) {
                Status.SUCCESS -> {
                    recyclerLayoutBinding.progress.visibility = View.GONE
                    resource.data?.let { movies ->
                        moviesAdapter.movies = movies
                    }
                }
                Status.ERROR -> {
                    recyclerLayoutBinding.progress.visibility = View.GONE
                    Toast.makeText(requireContext(), resource.message, Toast.LENGTH_SHORT)
                        .show()
                }
                Status.LOADING -> {
                    moviesAdapter.movies = emptyList()
                    recyclerLayoutBinding.progress.visibility = View.VISIBLE
                }
            }
        }

        viewModel.genres.observe(
            viewLifecycleOwner
        ) { resources ->
            when (resources.status) {
                Status.SUCCESS -> {
                    resources.data?.let {
                        genresAdapter.setData(it)
                    }
                    binding.tagsRecycler.visibility = View.VISIBLE
                }
                else -> {
                    binding.tagsRecycler.visibility = View.GONE
                }
            }
        }
    }

    companion object {
        fun newInstance() = MoviesFragment()
    }
}
