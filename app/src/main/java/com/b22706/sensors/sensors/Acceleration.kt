package com.b22706.sensors.sensors

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.util.Log
import androidx.compose.runtime.mutableStateOf

class Acceleration(context: Context): SensorBase(context) {
    companion object{
        const val LOG_NAME = "Sensor.Acceleration"
        data class AccelerationData(
            val time: Long,
            val x: Float,
            val y: Float,
            val z: Float
        ){
            override fun toString(): String {
                return "$time,$x,$y,$z"
            }
        }

        fun create(context: Context): Acceleration{
            return Acceleration(context).apply { init() }
        }
    }
    override val sensorType = Sensor.TYPE_LINEAR_ACCELERATION
    override val sensorName = "linerAcceleration"
    override val sensorDelay = SensorManager.SENSOR_DELAY_GAME
    override val csvHeader = "time,x,y,z"

    val dataText = mutableStateOf("null")

    override fun onSensorChanged(event: SensorEvent) {
        val data: () -> AccelerationData = {
            val time = System.currentTimeMillis()
            val x = event.values[0]
            val y = event.values[1]
            val z = event.values[2]

            AccelerationData(time, x, y, z)
        }

        super.queue.add(data().toString())
        Log.d(LOG_NAME,"${event.values.toList()}")
        dataText.value = data().toString()
    }
}