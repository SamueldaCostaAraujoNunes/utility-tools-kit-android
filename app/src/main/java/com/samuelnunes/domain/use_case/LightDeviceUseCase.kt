package com.samuelnunes.domain.use_case

import com.samuelnunes.utility_tool_kit.sensor.LightSensor
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject


class LightDeviceUseCase @Inject constructor(
    private var sensor: LightSensor
) {

    operator fun invoke(): Flow<Boolean> = sensor.observe().map {
        return@map it < 60f
    }

}