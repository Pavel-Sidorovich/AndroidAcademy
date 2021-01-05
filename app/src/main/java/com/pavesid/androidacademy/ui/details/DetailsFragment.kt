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
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import com.pavesid.androidacademy.R
import com.pavesid.androidacademy.data.actors.Cast
import com.pavesid.androidacademy.data.details.DetailsResponse
import com.pavesid.androidacademy.databinding.FragmentDetailsBinding
import com.pavesid.androidacademy.ui.MainActivity
import com.pavesid.androidacademy.utils.CalculationAngle
import com.pavesid.androidacademy.utils.Status
import com.pavesid.androidacademy.utils.extensions.ExitWithAnimation
import com.pavesid.androidacademy.utils.extensions.setShaderForGradient
import com.pavesid.androidacademy.utils.extensions.startCircularReveal
import com.pavesid.androidacademy.utils.extensions.startCircularRevealFromLeft
import com.pavesid.androidacademy.utils.extensions.toRightUrl
import com.pavesid.androidacademy.utils.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlin.properties.Delegates

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

    private val layoutManager: LinearLayoutManager by lazy {
        LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.HORIZONTAL,
            false
        )
    }

    private val castItemDecoration: CastItemDecoration by lazy {
        CastItemDecoration(
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

    private var movieId = 0

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
            movieId = it.getInt(PARAM_ID)
        }
        savedInstanceState ?: viewModel.loadDetails(movieId)
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
        if (!details.backdropPicture.isNullOrBlank()) {
            binding.detailsOrig.load(details.backdropPicture.toRightUrl()) {
                crossfade(true)
            }
        } else {
            binding.detailsOrig.load(BACKDROP_PLACEHOLDER) {
                crossfade(true)
            }
        }

        binding.apply {
            detailsStoryline.text = details.overview
            collapsingToolbar.title = details.title
            detailsTag.text = details.genres.joinToString { it.name }
            detailsRating.rating = details.ratings / 2
            detailsReviews.text =
                resources.getQuantityString(
                    R.plurals.review,
                    details.votesCount,
                    details.votesCount
                )
            detailsRectangle.text = if (details.adult) {
                getString(R.string.pg, 16)
            } else {
                getString(R.string.pg, 13)
            }
        }
    }

    private fun initCast(cast: List<Cast>) {
        if (cast.isNotEmpty()) {
            castAdapter.setData(cast.take(COUNT_OF_ACTORS))
            binding.detailsHeading.visibility = View.VISIBLE
            binding.detailsCastRecycler.visibility = View.VISIBLE
        } else {
            binding.detailsHeading.visibility = View.GONE
            binding.detailsCastRecycler.visibility = View.GONE
        }
    }

    private fun subscribeToObservers() {
        viewModel.details.observe(
            viewLifecycleOwner,
            { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        binding.progress.visibility = View.GONE
                        resource.data?.let { details ->
                            initDetails(details.detailsResponse)
                            initCast(details.cast)
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
        binding.apply {
            detailsStorylineTitle.setShaderForGradient()
            detailsHeading.setShaderForGradient()
            detailsCastRecycler.apply {
                layoutManager = this@DetailsFragment.layoutManager
                adapter = castAdapter
                addItemDecoration(castItemDecoration)
            }
        }
    }

    companion object {
        private const val PARAM_ID = "movie_id"
        private const val COUNT_OF_ACTORS = 6
        private const val BACKDROP_PLACEHOLDER = "https://hollywoodsuite.ca/wp-content/uploads/poster/hws-placeholder.jpg"

        @JvmStatic
        fun newInstance(
            id: Int,
            cX: Int,
            cY: Int
        ): DetailsFragment = DetailsFragment().apply {
            posX = cX
            posY = cY
            val args = Bundle()
            args.putInt(PARAM_ID, id)
            arguments = args
        }
    }
}
