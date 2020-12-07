package com.pavesid.androidacademy.ui.movies

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import com.pavesid.androidacademy.R
import com.pavesid.androidacademy.databinding.FragmentMoviesBinding
import com.pavesid.androidacademy.ui.MainActivity
import com.pavesid.androidacademy.ui.MainViewModel
import com.pavesid.androidacademy.utils.Status
import com.pavesid.androidacademy.utils.viewBinding

class MoviesFragment : Fragment(R.layout.fragment_movies) {

    private val binding: FragmentMoviesBinding by viewBinding(FragmentMoviesBinding::bind)

    private val mainActivity by lazy { activity as MainActivity }

    private val moviesAdapter by lazy {
        MoviesAdapter {
            mainActivity.changeFragment(it)
        }
    }

    private val callback by lazy { MoviesItemTouchHelper(moviesAdapter) }

    private val viewModel: MainViewModel by activityViewModels()

    override fun onStart() {
        super.onStart()
        mainActivity.window.statusBarColor =
            ContextCompat.getColor(requireContext(), R.color.background_color)
    }

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
    }

    private fun initView() {

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

    private fun subscribeToObservers() {
        viewModel.moviesPreview.observe(
            viewLifecycleOwner,
            { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        binding.progress.visibility = View.GONE
                        resource.data?.let { previews ->
                            moviesAdapter.movies = previews
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
