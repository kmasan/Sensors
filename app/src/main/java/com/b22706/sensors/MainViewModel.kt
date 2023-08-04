package com.b22706.sensors

import android.app.Activity
import android.content.Context
import android.os.Environment
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.b22706.sensors.sensors.Acceleration
import com.b22706.sensors.sensors.MyAudioSensor
import com.b22706.sensors.sensors.SensorBase
import com.kmasan.audiosensor.AudioSensor

class MainViewModel(
    val externalFilePath: String,
    val acceleration: Acceleration,
    val audioSensor: MyAudioSensor
    ) : ViewModel()
{
    val sensorRun = mutableStateOf(false)
    val sensorButtonText = mutableStateOf("sensor off")
    val csvRun = mutableStateOf(false)
    val csvButtonText = mutableStateOf("csv start")

    val accelerationText = acceleration.dataText
    val audioText = mutableStateOf("null")

    fun startSensors(){
        acceleration.start()
//        audioSensor.start(10)
    }

    fun stopSensors(){
        acceleration.stop()
//        audioSensor.stop()
    }

    fun startCSV(fileName: String){
        acceleration.csvWriterStart(externalFilePath, fileName)
    }

    fun stopCSV(){
        acceleration.csvRun = false
    }

    companion object{
        fun factory(context: Context): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            val externalFilePath = context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS).toString()
            val acceleration = Acceleration.create(context)
            val audioSensor = MyAudioSensor(context)
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(
                modelClass: Class<T>
            ): T {
                return MainViewModel(externalFilePath, acceleration, audioSensor) as T
            }
        }
    }
}