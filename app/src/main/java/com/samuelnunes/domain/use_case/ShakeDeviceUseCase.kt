package com.samuelnunes.domain.use_case

import android.hardware.SensorManager
import com.samuelnunes.utility_tool_kit.sensor.AccelerometerSensor
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import kotlin.math.pow
import kotlin.math.sqrt

class ShakeDeviceUseCase @Inject constructor(
    private var sensor: AccelerometerSensor
) {
    private var acceleration = 10f
    private var currentAcceleration = SensorManager.GRAVITY_EARTH
    private var lastAcceleration = SensorManager.GRAVITY_EARTH
    private var avgShaking: Int = 0


    operator fun invoke(): Flow<Boolean> = sensor.observe().map {
        it.apply {
            currentAcceleration = sqrt(x.pow(2) + y.pow(2) + z.pow(2))
        }
        acceleration = acceleration * 0.9f + (currentAcceleration - lastAcceleration)
        if(acceleration !in -7F..7F) avgShaking++ else avgShaking = 0
        return@map avgShaking >= 8
    }

}