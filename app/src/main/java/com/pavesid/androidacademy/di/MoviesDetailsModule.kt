package com.pavesid.androidacademy.di

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorManager
import androidx.appcompat.app.AppCompatActivity
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
}
