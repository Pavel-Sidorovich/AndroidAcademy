package com.pavesid.androidacademy.di

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorManager
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.pavesid.androidacademy.R
import com.pavesid.androidacademy.ui.details.CastAdapter
import com.pavesid.androidacademy.ui.details.CastItemDecoration
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.FragmentComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.FragmentScoped

@Module
@InstallIn(FragmentComponent::class)
object MoviesDetailsModule {

    @Provides
    @FragmentScoped
    fun provideSensorManager(
        @ApplicationContext context: Context
    ) = context.getSystemService(AppCompatActivity.SENSOR_SERVICE) as SensorManager

    @Provides
    @FragmentScoped
    fun provideAccelerometer(
        sensorManager: SensorManager
    ): Sensor = sensorManager.getDefaultSensor(
        Sensor.TYPE_ACCELEROMETER
    )

    @Provides
    @FragmentScoped
    internal fun provideAdapter() = CastAdapter()

    @Provides
    @FragmentScoped
    fun provideLayoutManager(
        @ApplicationContext context: Context
    ) = LinearLayoutManager(
        context,
        LinearLayoutManager.HORIZONTAL,
        false
    )

    @Provides
    @FragmentScoped
    internal fun provideDecoration(
        @ApplicationContext context: Context
    ) = CastItemDecoration(
        spaceSize = context.resources.getDimensionPixelSize(R.dimen.spacing_extra_small_4),
        bigSpaceSize = context.resources.getDimensionPixelSize(R.dimen.spacing_normal_16)
    )
}
