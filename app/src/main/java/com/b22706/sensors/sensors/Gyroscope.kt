package com.b22706.sensors.sensors

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.util.Log
import androidx.compose.runtime.mutableStateOf

class Gyroscope(context: Context): SensorBase(context) {
    companion object{
        const val LOG_NAME = "Sensor.Gyroscope"
        data class GyroscopeData(
            val time: Long,
            val x: Float,
            val y: Float,
            val z: Float
        ){
            override fun toString(): String {
                return "$time,$x,$y,$z"
            }
        }

        fun create(context: Context): Gyroscope{
            return Gyroscope(context).apply { init() }
        }
    }
    override val sensorType = Sensor.TYPE_GYROSCOPE
    override val sensorName = "gyroscope"
    override val sensorDelay = SensorManager.SENSOR_DELAY_GAME
    override val csvHeader = "time,x,y,z"

    override fun onSensorChanged(event: SensorEvent) {
        val data: () -> GyroscopeData = {
            val time = System.currentTimeMillis()
            val x = event.values[0]
            val y = event.values[1]
            val z = event.values[2]

            GyroscopeData(time, x, y, z)
        }

        queue.add(data().toString())
        Log.d(LOG_NAME,"${event.values.toList()}")
        dataText.value = "${data().x}, ${data().y}, ${data().z}"
    }
}