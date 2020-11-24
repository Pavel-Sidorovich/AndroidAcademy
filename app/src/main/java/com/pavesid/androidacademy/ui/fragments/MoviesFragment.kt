package com.pavesid.androidacademy.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.pavesid.androidacademy.R
import com.pavesid.androidacademy.data.local.FakeRepository
import com.pavesid.androidacademy.databinding.FragmentMoviesBinding
import com.pavesid.androidacademy.ui.activities.MainActivity
import com.pavesid.androidacademy.ui.adapters.MoviesAdapter
import com.pavesid.androidacademy.ui.decorations.MoviesItemDecoration

class MoviesFragment : Fragment() {

    private var _binding: FragmentMoviesBinding? = null
    private val binding: FragmentMoviesBinding
        get() = _binding!!

    private val moviesAdapter by lazy {
        MoviesAdapter {
            (activity as MainActivity).changeFragment(it)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMoviesBinding.inflate(layoutInflater)

        initView()

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun initView() {
        moviesAdapter.movies = FakeRepository.getAllMovies()
        binding.moviesRecycler.apply {
            layoutManager = GridLayoutManager(requireContext(), resources.getInteger(R.integer.grid_count))
            adapter = moviesAdapter
            addItemDecoration(
                MoviesItemDecoration(
                    spaceSize = resources.getDimensionPixelSize(R.dimen.spacing_small_8),
                    bigSpaceSize = resources.getDimensionPixelSize(R.dimen.spacing_nav_48),
                    gridSize = resources.getInteger(R.integer.grid_count)
                )
            )
        }
    }
}
