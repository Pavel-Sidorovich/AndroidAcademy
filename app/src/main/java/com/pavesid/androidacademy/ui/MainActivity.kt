package com.pavesid.androidacademy.ui

import android.hardware.Sensor
import android.hardware.Sensor.TYPE_ACCELEROMETER
import android.hardware.Sensor.TYPE_MAGNETIC_FIELD
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Build
import android.os.Bundle
import android.view.Display
import android.view.MenuItem
import android.view.Surface
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.pavesid.androidacademy.R
import com.pavesid.androidacademy.databinding.ActivityMainBinding
import com.pavesid.androidacademy.ui.details.MoviesDetailsFragment
import com.pavesid.androidacademy.ui.movies.MoviesFragment
import kotlin.math.atan2
import kotlin.math.round
import kotlin.math.sqrt

class MainActivity : AppCompatActivity(), MoviesFragment.Listener, SensorEventListener {

    private var _binding: ActivityMainBinding? = null
    private val binding: ActivityMainBinding
        get() = _binding!!

    private val sensorManager by lazy { getSystemService(SENSOR_SERVICE) as SensorManager }

    private val defaultMagnetometer: Sensor? by lazy {
        sensorManager.getDefaultSensor(
            TYPE_MAGNETIC_FIELD
        )
    }
    private val defaultAccelerometer: Sensor? by lazy {
        sensorManager.getDefaultSensor(
            TYPE_ACCELEROMETER
        )
    }

    private var gravity = FloatArray(3)
    private var geomagnetic = FloatArray(3)

    // System display. Need this for determining rotation.
    private val thisDisplay: Display? by lazy {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {
            (getSystemService(WINDOW_SERVICE) as WindowManager).defaultDisplay
        } else {
            this.display
        }
    }

    private val _angle = MutableLiveData<Double>()
    val angle: LiveData<Double> = _angle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val rootFragment = MoviesFragment().apply { setListener(this@MainActivity) }

        savedInstanceState ?: supportFragmentManager.beginTransaction().apply {
            add(R.id.container, rootFragment, null)
            commit()
        }
    }

    override fun onStart() {
        super.onStart()
        initListeners()
    }

    override fun onStop() {
        super.onStop()
        sensorManager.unregisterListener(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {}

    override fun onSensorChanged(event: SensorEvent) {

        // The sensorEvent object is reused across calls to onSensorChanged().
        // clone() gets a copy so the data doesn't change out from under us
        when (event.sensor.type) {
            TYPE_ACCELEROMETER ->
                gravity = event.values.clone()
            TYPE_MAGNETIC_FIELD ->
                geomagnetic = event.values.clone()
        }

        val normalizerGravity =
            sqrt(gravity[0] * gravity[0] + gravity[1] * gravity[1] + gravity[2] * gravity[2])

        gravity[0] = gravity[0] / normalizerGravity
        gravity[1] = gravity[1] / normalizerGravity
        gravity[2] = gravity[2] / normalizerGravity

        var rotation = when (thisDisplay?.rotation) {
            Surface.ROTATION_0 ->
                round(Math.toDegrees(atan2(gravity[0].toDouble(), gravity[1].toDouble())))
            Surface.ROTATION_90 ->
                round(Math.toDegrees(atan2(gravity[0].toDouble(), gravity[1].toDouble()))) - 90
            Surface.ROTATION_180 ->
                round(Math.toDegrees(atan2(gravity[0].toDouble(), gravity[1].toDouble())))
            Surface.ROTATION_270 ->
                round(Math.toDegrees(atan2(gravity[0].toDouble(), gravity[1].toDouble()))) + 90
            else -> round(Math.toDegrees(atan2(gravity[0].toDouble(), gravity[1].toDouble())))
        }
        rotation = when (rotation) {
            in 20.0..Double.MAX_VALUE -> 20.0
            in Double.NEGATIVE_INFINITY..-20.0 -> -20.0
            else -> rotation
        }

        _angle.postValue(rotation)
    }

    override fun changeFragmentById(id: Int) {
        supportFragmentManager.beginTransaction().apply {
            add(R.id.container, MoviesDetailsFragment.newInstance(id), null)
            addToBackStack(null)
            commit()
        }
    }

    private fun initListeners() {
        defaultMagnetometer?.let {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_NORMAL)
        }
        defaultAccelerometer?.let {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_NORMAL)
        }
    }

    fun changeFragment(id: Int) {
        val detailFragment = MoviesDetailsFragment.newInstance(id)
        supportFragmentManager.beginTransaction().apply {
            add(R.id.container, detailFragment, null)
            addToBackStack(null)
            commit()
        }
    }
}
