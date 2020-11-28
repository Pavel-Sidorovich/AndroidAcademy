package com.pavesid.androidacademy.ui.movies

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.observe
import androidx.recyclerview.widget.GridLayoutManager
import com.pavesid.androidacademy.R
import com.pavesid.androidacademy.databinding.FragmentMoviesBinding
import com.pavesid.androidacademy.ui.MainActivity
import com.pavesid.androidacademy.ui.MainViewModel
import com.pavesid.androidacademy.utils.Status

class MoviesFragment : Fragment() {

    private var _binding: FragmentMoviesBinding? = null
    private val binding: FragmentMoviesBinding
        get() = _binding!!

    private val mainActivity by lazy { activity as MainActivity }

    private var listener: Listener? = null

    private val moviesAdapter by lazy {
        MoviesAdapter {
            mainActivity.changeFragment(it)
            // TODO
            // listener?.changeFragmentById(it)
        }
    }

    private val viewModel: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMoviesBinding.inflate(layoutInflater)

        initActionBar()
        initView()
        subscribeToObservers()

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        listener = null
        _binding = null
    }

    private fun initActionBar() {
        mainActivity.apply {
            setSupportActionBar(binding.toolbar)
            title = ""
        }
    }

    private fun initView() {

        binding.moviesRecycler.apply {
            layoutManager =
                GridLayoutManager(requireContext(), resources.getInteger(R.integer.grid_count))
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

    private fun subscribeToObservers() {
        viewModel.moviesPreview.observe(viewLifecycleOwner) { resource ->
            when (resource.status) {
                Status.SUCCESS -> {
                    binding.progress.visibility = View.GONE
                    resource.data?.let { previews ->
                        moviesAdapter.movies = previews
                    }
                }
                Status.ERROR -> {
                    binding.progress.visibility = View.GONE
                    Toast.makeText(requireContext(), resource.message, Toast.LENGTH_SHORT).show()
                }
                Status.LOADING -> binding.progress.visibility = View.VISIBLE
            }
        }
    }

    fun setListener(l: Listener) {
        listener = l
    }

    interface Listener {
        fun changeFragmentById(id: Int)
    }
}
