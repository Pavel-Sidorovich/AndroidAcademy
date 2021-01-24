package com.pavesid.androidacademy.ui.details

import android.graphics.Color
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Build
import android.os.Bundle
import android.view.Display
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.WindowManager
import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import android.view.animation.RotateAnimation
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.children
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import com.pavesid.androidacademy.R
import com.pavesid.androidacademy.data.actors.Cast
import com.pavesid.androidacademy.data.actors.Crew
import com.pavesid.androidacademy.data.details.DetailsResponse
import com.pavesid.androidacademy.data.movies.Movie
import com.pavesid.androidacademy.databinding.FragmentDetailsBinding
import com.pavesid.androidacademy.ui.MainActivity
import com.pavesid.androidacademy.utils.CalculationAngle
import com.pavesid.androidacademy.utils.Status
import com.pavesid.androidacademy.utils.extensions.ExitWithAnimation
import com.pavesid.androidacademy.utils.extensions.setShaderForGradient
import com.pavesid.androidacademy.utils.extensions.startCircularReveal
import com.pavesid.androidacademy.utils.extensions.startCircularRevealFromLeft
import com.pavesid.androidacademy.utils.extensions.toOriginalUrl
import com.pavesid.androidacademy.utils.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlin.properties.Delegates
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

@SuppressWarnings("deprecation")
@AndroidEntryPoint
class DetailsFragment :
    Fragment(R.layout.fragment_details),
    ExitWithAnimation {

    private val binding: FragmentDetailsBinding by viewBinding(FragmentDetailsBinding::bind)

    @Inject
    lateinit var sensorManager: SensorManager

    @Inject
    lateinit var defaultAccelerometer: Sensor

    private val viewModel by lazy { ViewModelProvider(requireActivity()).get(DetailsViewModel::class.java) }

    private val mainActivity by lazy { activity as MainActivity }

    private val castAdapter: CastAdapter = CastAdapter()

    private val crewAdapter: CrewAdapter = CrewAdapter()

    private val layoutManagerCast: LinearLayoutManager by lazy {
        LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.HORIZONTAL,
            false
        )
    }

    private val layoutManagerCrew: LinearLayoutManager by lazy {
        LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.HORIZONTAL,
            false
        )
    }

    private val customItemDecoration: CustomItemDecoration by lazy {
        CustomItemDecoration(
            spaceSize = requireContext().resources.getDimensionPixelSize(R.dimen.spacing_extra_small_4),
            bigSpaceSize = requireContext().resources.getDimensionPixelSize(R.dimen.spacing_normal_16)
        )
    }

    private val thisDisplay: Display? by lazy {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {
            (requireContext().getSystemService(AppCompatActivity.WINDOW_SERVICE) as WindowManager).defaultDisplay
        } else {
            requireContext().display
        }
    }

    private var angle by Delegates.observable(0) { _, oldValue, newValue ->
        animateRecycler(oldValue, newValue)
    }

    private lateinit var currentMovie: Movie

    private val eventListener: SensorEventListener = object : SensorEventListener {

        override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {}

        override fun onSensorChanged(event: SensorEvent) {

            var angleTemp = 0

            when (event.sensor.type) {
                Sensor.TYPE_ACCELEROMETER ->
                    angleTemp = CalculationAngle.getAngle(event.values, thisDisplay?.rotation ?: 0)
            }

            if (angleTemp != Int.MAX_VALUE) {
                angle = angleTemp
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        arguments?.let {
            currentMovie = Json.decodeFromString(it.getString(PARAM_MOVIE).orEmpty())
        }
        viewModel.loadDetails(currentMovie.id)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initActionBar()
        initView()
        subscribeToObservers()

        savedInstanceState ?: if (posX != null && posY != null) {
            view.startCircularReveal(posX!!, posY!!)
        } else {
            view.startCircularRevealFromLeft()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.findItem(R.id.theme).isVisible = false
        menu.findItem(R.id.action_search).isVisible = false
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onStart() {
        super.onStart()
        mainActivity.window.statusBarColor = Color.TRANSPARENT
        initListeners()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        sensorManager.unregisterListener(eventListener)
    }

    override fun onStop() {
        super.onStop()
        mainActivity.setSupportActionBar(null)
    }

    override var posX: Int? = null
    override var posY: Int? = null

    override fun isToBeExitedWithAnimation(): Boolean = true

    private fun initListeners() {
        defaultAccelerometer.let {
            sensorManager.registerListener(eventListener, it, SensorManager.SENSOR_DELAY_UI)
        }
    }

    private fun initActionBar() {
        mainActivity.setSupportActionBar(binding.toolbar)
        mainActivity.supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
        }
    }

    private fun initDetails(details: DetailsResponse) {
        binding.detailsRuntime.text = resources.getString(R.string.runtime, details.runtime)
        if (!details.backdropPicture.isNullOrBlank()) {
            binding.detailsOrig.load(details.backdropPicture.toOriginalUrl()) {
                crossfade(true)
                placeholder(R.drawable.hws_placeholder)
            }
        } else {
            binding.detailsOrig.load(R.drawable.hws_placeholder) {
                crossfade(true)
            }
        }

        binding.apply {
            detailsStoryline.text = details.overview
            collapsingToolbar.title = details.title
            detailsTag.text = details.genres.joinToString { it.name }
        }
    }

    private fun initCast(cast: List<Cast>) {
        if (cast.isNotEmpty()) {
            castAdapter.actors = cast
            binding.detailsCastHeading.visibility = View.VISIBLE
            binding.detailsCastRecycler.visibility = View.VISIBLE
        } else {
            binding.detailsCastHeading.visibility = View.GONE
            binding.detailsCastRecycler.visibility = View.GONE
        }
    }

    private fun initCrew(crew: List<Crew>) {
        if (crew.isNotEmpty()) {
            crewAdapter.crews = crew
            binding.detailsCrewHeading.visibility = View.VISIBLE
            binding.detailsCrewRecycler.visibility = View.VISIBLE
        } else {
            binding.detailsCrewHeading.visibility = View.GONE
            binding.detailsCrewRecycler.visibility = View.GONE
        }
    }

    private fun subscribeToObservers() {
        viewModel.details.observe(
            viewLifecycleOwner,
            { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        binding.progress.isVisible = false
                        resource.data?.let { details ->
                            if (details.detailsResponse.id == currentMovie.id) {
                                initDetails(details.detailsResponse)
                                initCast(details.cast)
                                initCrew(details.crew)
                            }
                        }
                    }
                    Status.ERROR -> {
                        binding.progress.isVisible = false
                        Toast.makeText(requireContext(), resource.message, Toast.LENGTH_SHORT)
                            .show()
                    }
                    Status.LOADING -> {
                        binding.progress.isVisible = true
                        initCast(emptyList())
                        initCrew(emptyList())
                    }
                }
            }
        )
    }

    private fun animateRecycler(prev: Int, current: Int) {
        val rotate = RotateAnimation(
            prev.toFloat(),
            current.toFloat(),
            Animation.RELATIVE_TO_SELF,
            0.5f,
            Animation.RELATIVE_TO_SELF,
            0f
        )
        rotate.interpolator = LinearInterpolator()
        rotate.fillAfter = true
        rotate.isFillEnabled = true

        binding.detailsCastRecycler.children.forEach {
            it.startAnimation(rotate)
        }

        binding.detailsCrewRecycler.children.forEach {
            it.startAnimation(rotate)
        }
    }

    private fun initView() {
        binding.apply {
            detailsStorylineTitle.setShaderForGradient()
            detailsCastHeading.setShaderForGradient()
            detailsCastRecycler.apply {
                layoutManager = this@DetailsFragment.layoutManagerCast
                adapter = castAdapter
                addItemDecoration(customItemDecoration)
            }
            detailsCrewRecycler.apply {
                layoutManager = this@DetailsFragment.layoutManagerCrew
                adapter = crewAdapter
                addItemDecoration(customItemDecoration)
            }

            detailsStoryline.text = currentMovie.overview
            collapsingToolbar.title = currentMovie.title
            detailsTag.text = currentMovie.genres.joinToString { it.name }
            detailsRating.rating = currentMovie.ratings / 2
            detailsReviews.text =
                resources.getQuantityString(
                    R.plurals.review,
                    currentMovie.numberOfRatings,
                    currentMovie.numberOfRatings
                )
            detailsRectangle.text = getString(R.string.pg, currentMovie.minimumAge)
        }
    }

    companion object {
        private const val PARAM_MOVIE = "movie"

        @JvmStatic
        fun newInstance(
            movie: String,
            cX: Int,
            cY: Int
        ): DetailsFragment = DetailsFragment().apply {
            posX = cX
            posY = cY
            val args = Bundle()
            args.putString(PARAM_MOVIE, movie)
            arguments = args
        }
    }
}
