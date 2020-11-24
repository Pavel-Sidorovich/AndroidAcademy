package com.pavesid.androidacademy.ui.fragments

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import com.pavesid.androidacademy.R
import com.pavesid.androidacademy.data.local.FakeRepository
import com.pavesid.androidacademy.databinding.FragmentMoviesDetailsBinding
import com.pavesid.androidacademy.ui.activities.MainActivity
import com.pavesid.androidacademy.ui.adapters.CastAdapter
import com.pavesid.androidacademy.ui.decorations.CastItemDecoration
import com.pavesid.androidacademy.utils.Utils

class MoviesDetailsFragment : Fragment() {

    private var _binding: FragmentMoviesDetailsBinding? = null
    private val binding: FragmentMoviesDetailsBinding
        get() = _binding!!

    private val castAdapter by lazy { CastAdapter() }

    private var addTitle = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            addTitle = it.getString(PARAM_TITLE, "")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMoviesDetailsBinding.inflate(layoutInflater)

        initView()

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun initView() {

        val movie = FakeRepository.getEndGameMovie()

        if (movie.image == "") {
            binding.detailsOrig.load(Drawable.createFromStream(requireActivity().assets.open("orig.png"), null))
        } else {
            binding.detailsOrig.load(movie.image)
        }

        binding.detailsName.apply {
            text = resources.getString(R.string.name_custom, movie.title, addTitle)
            paint.shader = Utils.getShaderForGradientTextView(this)
        }

        binding.detailsStorylineTitle.apply { paint.shader = Utils.getShaderForGradientTextView(this) }

        binding.detailsHeading.apply { paint.shader = Utils.getShaderForGradientTextView(this) }

        binding.detailsCastRecycler.apply {
            layoutManager = LinearLayoutManager(
                requireContext(),
                LinearLayoutManager.HORIZONTAL,
                false
            )
            adapter = castAdapter
            addItemDecoration(
                CastItemDecoration(
                    spaceSize = resources.getDimensionPixelSize(R.dimen.spacing_extra_small_4),
                    bigSpaceSize = resources.getDimensionPixelSize(R.dimen.spacing_normal_16)
                )
            )
        }

        binding.detailsBack.setOnClickListener {
            (activity as MainActivity).onBackPressed()
        }

        castAdapter.actors = movie.actors

        val point = Utils.getNavigationBarSize(requireContext())

        if (point?.y != 0) {
            binding.detailsNav.visibility = View.VISIBLE
        } else {
            binding.detailsNav.visibility = View.GONE
        }
    }

    companion object {
        private const val PARAM_TITLE = "add to title"

        fun newInstance(
            title: String
        ): MoviesDetailsFragment {
            val fragment = MoviesDetailsFragment()
            val args = Bundle()
            args.putString(PARAM_TITLE, title)
            fragment.arguments = args
            return fragment
        }
    }
}
