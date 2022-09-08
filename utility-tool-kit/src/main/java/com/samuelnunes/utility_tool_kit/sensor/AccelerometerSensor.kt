package com.samuelnunes.utility_tool_kit.sensor

import android.content.Context
import android.content.pm.PackageManager.FEATURE_SENSOR_ACCELEROMETER
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


class AccelerometerSensor @Inject constructor(context: Context) {

    data class Axies(val x: Float, val y: Float, val z: Float)

    private var sensorManager: SensorManager =
        context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    private var sensor: Sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
    private val doesSensorExist: Boolean =
        context.packageManager.hasSystemFeature(FEATURE_SENSOR_ACCELEROMETER)

    fun convert(event: SensorEvent): Axies = Axies(
        x = event.values[0],
        y = event.values[1],
        z = event.values[2]
    )

    fun observe(): Flow<Axies> {
        if (!doesSensorExist) {
            return emptyFlow()
        }
        return channelFlow {
            val listener = object : SensorEventListener {
                override fun onSensorChanged(event: SensorEvent?) {
                    if (event != null) {
                        launch { send(convert(event)) }
                    }

                }

                override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) = Unit
            }
            sensorManager.registerListener(listener, sensor, SensorManager.SENSOR_DELAY_UI)

            awaitClose {
                sensorManager.unregisterListener(listener)
            }
        }
    }
}