package com.pavesid.androidacademy.ui.details

import android.content.res.Configuration
import android.graphics.Color
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import android.view.Display
import android.view.Surface
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import android.view.animation.RotateAnimation
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.children
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import com.pavesid.androidacademy.R
import com.pavesid.androidacademy.data.Movie
import com.pavesid.androidacademy.databinding.FragmentMoviesDetailsBinding
import com.pavesid.androidacademy.ui.MainActivity
import com.pavesid.androidacademy.utils.setShaderForGradient
import com.pavesid.androidacademy.utils.viewBinding
import javax.inject.Inject
import kotlin.math.abs
import kotlin.math.atan2
import kotlin.math.round
import kotlin.math.sqrt
import kotlin.properties.Delegates

class MoviesDetailsFragment @Inject constructor() : Fragment(R.layout.fragment_movies_details) {

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

    private val defaultAccelerometer: Sensor? by lazy {
        sensorManager.getDefaultSensor(
            Sensor.TYPE_ACCELEROMETER
        )
    }

    private val sensorManager by lazy { requireContext().getSystemService(AppCompatActivity.SENSOR_SERVICE) as SensorManager }

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

    private var movie: Movie? = null

    private val eventListener: SensorEventListener = object : SensorEventListener {

        override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {}

        override fun onSensorChanged(event: SensorEvent) {

            // The sensorEvent object is reused across calls to onSensorChanged().
            // clone() gets a copy so the data doesn't change out from under us
            when (event.sensor.type) {
                Sensor.TYPE_ACCELEROMETER ->
                    gravity = event.values.clone()
            }

            getAngle()
        }
    }

    private var gravity = FloatArray(3)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        arguments?.let {
            movie = it.getParcelable(PARAM_PARCELABLE)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initActionBar()
        initView()
    }

    override fun onStart() {
        super.onStart()
        mainActivity.window.statusBarColor = Color.TRANSPARENT
        initListeners()
    }

    override fun onStop() {
        super.onStop()
        sensorManager.unregisterListener(eventListener)
    }

    private fun getAngle() {
        val normalizerGravity =
            sqrt(gravity[0] * gravity[0] + gravity[1] * gravity[1] + gravity[2] * gravity[2])

        gravity[0] = gravity[0] / normalizerGravity
        gravity[1] = gravity[1] / normalizerGravity
        gravity[2] = gravity[2] / normalizerGravity

        var rotation =
            round(Math.toDegrees(atan2(gravity[0].toDouble(), gravity[1].toDouble()))).toInt()

        rotation = when (thisDisplay?.rotation) {
            Surface.ROTATION_0, Surface.ROTATION_180 ->
                rotation
            Surface.ROTATION_90 ->
                rotation - 90
            Surface.ROTATION_270 ->
                rotation + 90
            else -> rotation
        }

        rotation /= ANGLE_DIVIDER

        if (abs(rotation) < MAX_ANGLE) {
            angle = rotation
        }
    }

    private fun initListeners() {
        defaultAccelerometer?.let {
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

    private fun initMovie() {
        movie?.let { movie ->
            if (movie.poster.isNotBlank()) {
                binding.detailsOrig.load(movie.backdrop) {
                    crossfade(true)
                }
            }
            if (movie.actors.isNotEmpty()) {
                castAdapter.setData(movie.actors)
            } else {
                binding.detailsHeading.visibility = View.GONE
            }

            binding.apply {
                detailsStoryline.text = movie.overview
                collapsingToolbar.title = movie.title
                detailsTag.text = movie.genres.joinToString { it.name }
                detailsRating.rating = movie.ratings / 2
                detailsReviews.text =
                    resources.getQuantityString(
                        R.plurals.review,
                        movie.numberOfRatings,
                        movie.numberOfRatings
                    )
                detailsRectangle.text = getString(R.string.pg, movie.minimumAge)
            }
        }
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
    }

    private fun initView() {
        initMovie()
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
    }

    companion object {
        private const val ANGLE_DIVIDER = 5
        private const val MAX_ANGLE = 10
        private const val PARAM_PARCELABLE = "ParcelableElement"

        fun newInstance(
            parcelable: Parcelable
        ): MoviesDetailsFragment {
            val fragment =
                MoviesDetailsFragment()
            val args = Bundle()
            args.putParcelable(PARAM_PARCELABLE, parcelable)
            fragment.arguments = args
            return fragment
        }
    }
}
