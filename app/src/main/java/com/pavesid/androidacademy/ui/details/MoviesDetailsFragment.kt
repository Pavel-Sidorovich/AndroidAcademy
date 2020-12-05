package com.pavesid.androidacademy.ui.details

import android.content.res.Configuration
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import android.view.animation.RotateAnimation
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import com.pavesid.androidacademy.R
import com.pavesid.androidacademy.data.local.model.Movie
import com.pavesid.androidacademy.data.local.model.MoviePreview
import com.pavesid.androidacademy.databinding.FragmentMoviesDetailsBinding
import com.pavesid.androidacademy.ui.MainActivity
import com.pavesid.androidacademy.ui.MainViewModel
import com.pavesid.androidacademy.utils.Status
import com.pavesid.androidacademy.utils.Utils
import com.pavesid.androidacademy.utils.setShaderForGradient
import com.pavesid.androidacademy.utils.viewBinding

class MoviesDetailsFragment : Fragment(R.layout.fragment_movies_details) {

    private val binding: FragmentMoviesDetailsBinding by viewBinding(FragmentMoviesDetailsBinding::bind)

    private val mainActivity by lazy { activity as MainActivity }

    private val castAdapter by lazy { CastAdapter() }

    private val layoutManager by lazy {
        LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.HORIZONTAL,
            false
        )
    }

    private var movieId = 0

    private val viewModel: MainViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        arguments?.let {
            movieId = it.getInt(PARAM_ID)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        savedInstanceState ?: viewModel.getMovie(movieId)

        subscribeToObservers()
        initActionBar()
        initView()
    }

    override fun onStart() {
        super.onStart()
        mainActivity.window.statusBarColor = Color.TRANSPARENT
    }

    private fun initActionBar() {
        mainActivity.setSupportActionBar(binding.toolbar)
        mainActivity.supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
        }
    }

    private fun initMovie(movie: Movie) {
        if (movie.image == "") {
            binding.detailsOrig.load(
                Drawable.createFromStream(
                    requireActivity().assets.open("orig.png"),
                    null
                )
            )
        } else {
            binding.detailsOrig.load(movie.image)
        }
        binding.detailsStoryline.text = movie.storyline
        castAdapter.actors = movie.actors
    }

    private fun initPreview(preview: MoviePreview) {

        binding.collapsingToolbar.title = preview.name
        binding.detailsTag.text = preview.tags.joinToString()
        binding.detailsRating.rating = preview.rating.toFloat()
        binding.detailsReviews.text =
            resources.getQuantityString(R.plurals.review, preview.reviews, preview.reviews)

        binding.detailsRectangle.text = getString(R.string.pg, preview.pg)
    }

    private fun subscribeToObservers() {
        viewModel.moviesPreview.observe(viewLifecycleOwner) { resource ->
            when (resource.status) {
                Status.SUCCESS -> {
                    binding.progress.visibility = View.GONE
                    resource.data?.let { previews ->
                        initPreview(previews[movieId])
                    }
                }
                Status.ERROR -> {
                    binding.progress.visibility = View.GONE
                    Toast.makeText(requireContext(), resource.message, Toast.LENGTH_SHORT).show()
                }
                Status.LOADING -> binding.progress.visibility = View.VISIBLE
            }
        }
        viewModel.movie.observe(viewLifecycleOwner) { resource ->
            when (resource.status) {
                Status.SUCCESS -> {
                    resource.data?.let { movie ->
                        if (movie.id == movieId % 4) { // TODO change 4 later
                            binding.progressMovie.visibility = View.GONE
                            initMovie(movie)
                        }
                    }
                }
                Status.ERROR -> {
                    binding.progressMovie.visibility = View.GONE
                    Toast.makeText(requireContext(), resource.message, Toast.LENGTH_SHORT).show()
                }
                Status.LOADING -> binding.progressMovie.visibility = View.VISIBLE
            }
        }

        animateRecycler()
    }

    private fun animateRecycler() {
        var prev = 0.0
        mainActivity.angle.observe(viewLifecycleOwner) {
            val rotate = RotateAnimation(
                prev.toFloat(),
                it.toFloat(),
                Animation.RELATIVE_TO_SELF,
                0.5f,
                Animation.RELATIVE_TO_SELF,
                0f
            )
            rotate.interpolator = LinearInterpolator()
            rotate.duration = 250
            rotate.fillAfter = true
            rotate.isFillEnabled = true

            val first = layoutManager.findFirstVisibleItemPosition()
            val last = layoutManager.findLastVisibleItemPosition()

            if (first != -1 && last != -1) {
                for (i in first..last) {
                    val view = layoutManager.findViewByPosition(i)
                    view?.startAnimation(rotate)
                }
                prev = it
            }
        }
    }

    private fun initView() {
        binding.detailsStorylineTitle.setShaderForGradient()

        binding.detailsHeading.setShaderForGradient()

        binding.detailsCastRecycler.apply {
            layoutManager = this@MoviesDetailsFragment.layoutManager
            adapter = castAdapter
            addItemDecoration(
                CastItemDecoration(
                    spaceSize = resources.getDimensionPixelSize(R.dimen.spacing_extra_small_4),
                    bigSpaceSize = resources.getDimensionPixelSize(R.dimen.spacing_normal_16)
                )
            )
        }

        val param = binding.scrollView.layoutParams as ViewGroup.MarginLayoutParams
        if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            param.setMargins(
                0,
                0,
                -resources.getDimensionPixelSize(R.dimen.spacing_extra_large_36),
                0
            )
        } else {
            param.setMargins(
                0,
                0,
                0,
                0
            )
        }
        binding.scrollView.layoutParams = param

        val point = Utils.getNavigationBarSize(requireContext())

        if (point?.y != 0) {
            binding.detailsNav.visibility = View.VISIBLE
        } else {
            binding.detailsNav.visibility = View.GONE
        }
    }

    companion object {
        private const val PARAM_ID = "id"

        fun newInstance(
            id: Int
        ): MoviesDetailsFragment {
            val fragment =
                MoviesDetailsFragment()
            val args = Bundle()
            args.putInt(PARAM_ID, id)
            fragment.arguments = args
            return fragment
        }
    }
}
