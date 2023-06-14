package com.b22706.sensors.sensors

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.os.Handler
import android.os.Looper
import android.text.format.DateUtils
import android.util.Log
import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVPrinter
import java.io.FileWriter
import java.io.IOException

class Acceleration(context: Context): SensorBase(context) {
    companion object{
        const val LOG_NAME = "Sensor.Acceleration"
        data class AccelerationData(
            val time: Long,
            val x: Float,
            val y: Float,
            val z: Float
        )
    }
    override val sensorType = Sensor.TYPE_LINEAR_ACCELERATION
    override val sensorName = "linerAcceleration"
    override val csvHeader = "time,x,y,z"

    override fun onSensorChanged(event: SensorEvent) {
        val x = event.values[0]
        val y = event.values[1]
        val z = event.values[2]
        super.queue.add("${System.currentTimeMillis()},$x,$y,$z")
//        Log.d(LOG_NAME,"${event.values.toList()}")
    }
}