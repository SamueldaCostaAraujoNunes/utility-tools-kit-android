package com.samuelnunes.di

import android.content.Context
import com.samuelnunes.utility_tool_kit.sensor.AccelerometerSensor
import com.samuelnunes.utility_tool_kit.sensor.LightSensor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SensorModule {

    @Singleton
    @Provides
    fun providerAccelerometerSensor(@ApplicationContext appContext: Context): AccelerometerSensor = AccelerometerSensor(appContext)

    @Singleton
    @Provides
    fun providerLightSensor(@ApplicationContext appContext: Context): LightSensor = LightSensor(appContext)

}